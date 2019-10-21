# spotDetection3D.py
# ==================
#
# This script demonstrates spot detection
# in 3D on the GPU
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         October 2019
#########################################

from ij import IJ;
from net.haesleinhuepf.clij import CLIJ;
from net.haesleinhuepf.clijx import CLIJx;
from ij.io import FileInfo
from ij.plugin import Raw

# load/create example images
# imp = IJ.openImage("C:/structure/data/Nantes_000646.tif");
# imp = IJ.openImage("C:/structure/data/Riga_000512.tif");
# imp = IJ.openImage("C:/structure/data/Pau_000512.tif");

# run("Raw...", "open=//fileserver/myersspimdata/IMAGING/archive_data_good/2019-07-16-13-30-14-91-Pau_Tribolium_nGFP_pole_/stacks/C0opticsprefused/001000.raw image=[16-bit Unsigned] width=1024 height=1024 number=1000 little-endian");

# init
initialized = False;

for i in range(1000, 1400, 25):
	IJ.run("Close All");
	
	fi = FileInfo();
	fi.fileType = FileInfo.GRAY16_UNSIGNED;
	fi.width = 1024;
	fi.height = 1024;
	fi.nImages = 1000;
	fi.intelByteOrder = True;
	imp = Raw.open("\\\\fileserver\\myersspimdata\\IMAGING\\archive_data_good\\2019-07-16-13-30-14-91-Pau_Tribolium_nGFP_pole_\\stacks\\C0opticsprefused\\00" + str(i) + ".raw", fi);
	imp.setRoi(129, 173, 715, 690);
	imp = imp.crop("stack");
	
	IJ.run(imp, "32-bit", "");
	IJ.run(imp, "Rotate 90 Degrees Right", "");
	imp.show();
	
	# Init GPU
	clijx = CLIJx.getInstance();
	
	# push data to GPU
	inputImage = clijx.push(imp);

	if not initialized:
		maximumInput = clijx.create([inputImage.getWidth(), inputImage.getHeight()]);
		maximumBackgroundSubtracted = clijx.create(maximumInput);
		maximumSpots = clijx.create(maximumInput);
		maximumMesh = clijx.create(maximumInput);
	
		# blur a bit and detect maxima
		blurred = clijx.create(inputImage);
		thresholded = clijx.create(inputImage);
		detected_spots = clijx.create(inputImage);
		masked = clijx.create(inputImage);
	
		mesh = clijx.create(inputImage);
		temp = clijx.create(detected_spots);

		
	# background / noise removal
	clijx.op().differenceOfGaussian(inputImage, blurred, 3, 3, 0, 15, 15, 0);
	#clijx.show(blurred, "blurred");
	
	# spot detection
	clijx.op().detectMaximaBox(blurred, detected_spots, 5);
	
	# remove spots in background
	clijx.op().automaticThreshold(blurred, thresholded, "Otsu");
	#clijx.show(thresholded, "thresholded");
	clijx.op().mask(detected_spots, thresholded, masked);
	clijx.op().copy(masked, detected_spots);
	#clijx.show(detected_spots, "spots");
	
	# convert spots image to spot list
	number_of_spots = clijx.op().sumPixels(detected_spots);
	pointlist = clijx.create([number_of_spots, 3]);
	clijx.op().spotsToPointList(detected_spots, pointlist);
	
	# determine distance between spots
	distance_matrix = clijx.create([number_of_spots, number_of_spots]);
	clijx.op().generateDistanceMatrix(pointlist, pointlist, distance_matrix);
	
	# find n closes spots for every spot
	n_closest_points = 6;
	closestPointsIndices = clijx.create([number_of_spots, n_closest_points]);
	clijx.op().nClosestPoints(distance_matrix, closestPointsIndices);
	
	# build and visualise mesh
	pointCoodinates = clijx.pull(pointlist).getProcessor();
	pointIndices = clijx.pull(closestPointsIndices).getProcessor();
	
	for p in range(0, pointIndices.getWidth()):
		x1 = pointCoodinates.getf(p, 0);
		y1 = pointCoodinates.getf(p, 1);
		z1 = pointCoodinates.getf(p, 2);
	
		for q in range(1, n_closest_points):
			pointIndex = int(pointIndices.getf(p, q));
			x2 = pointCoodinates.getf(pointIndex, 0);
			y2 = pointCoodinates.getf(pointIndex, 1);
			z2 = pointCoodinates.getf(pointIndex, 2);
	
			thickness = 1;
			clijx.op().drawLine(mesh, x1, y1, z1, x2, y2, z2, thickness);
	
	# make spots bigger for visualisation
	clijx.op().dilateBox(detected_spots, temp);
	clijx.op().dilateSphere(temp, detected_spots);
	#clijx.show(detected_spots, "e spots");
	
	# generate maximum projections of all results
	clijx.op().maximumZProjection(inputImage, maximumInput);
	clijx.op().maximumZProjection(blurred, maximumBackgroundSubtracted);
	clijx.op().maximumZProjection(detected_spots, maximumSpots);
	clijx.op().maximumZProjection(mesh, maximumMesh);
	
	# intermediate cleanup
	pointlist.close();
	distance_matrix.close();
	closestPointsIndices.close();
	
	# show results
	clijx.show(maximumInput, "maximumInput");
	clijx.show(maximumBackgroundSubtracted, "maximumBackgroundSubtracted");
	clijx.show(maximumSpots, "maximumSpots");
	clijx.show(maximumMesh, "maximumMesh");
	
	# blending visualisation
	temp3 = clijx.create(maximumInput);
	imp = clijx.pull(temp3);
	temp3.close();
	ip = imp.getProcessor();
	bandwidth = ip.getWidth() / 4
	for x in range(0, bandwidth):
		for y in range(0, ip.getHeight()):
			ip.setf(x, y, 1.0 * x / bandwidth);
			ip.setf(x + bandwidth, y, 1);
			ip.setf(x + bandwidth * 2, y, 1.0 - 1.0 * x / bandwidth);
			ip.setf(x + bandwidth * 3, y, 0);
	imp.show();
	
	shift2 = clijx.push(imp);
	IJ.run(imp, "Translate...", "x=-154 y=0 interpolation=None");
	shift1 = clijx.push(imp);
	shift3 = clijx.create(shift2);
	shift4 = clijx.create(shift2); 
	clijx.op().flip(shift1, shift4, True, False, False);
	clijx.op().flip(shift2, shift3, True, False, False);
	
	temp2 = clijx.create(shift1);
	
	clijx.op().multiplyImages(maximumInput, shift1, temp2);
	clijx.show(temp2, "shift1");
	clijx.op().multiplyImages(maximumBackgroundSubtracted, shift2, temp2);
	clijx.show(temp2, "shift2");
	clijx.op().multiplyImages(maximumSpots, shift3, temp2);
	clijx.show(temp2, "shift3");
	clijx.op().multiplyImages(maximumMesh, shift4, temp2);
	clijx.show(temp2, "shift4");
	
	IJ.run(imp, "Merge Channels...", "c1=shift1 c2=shift2 c3=shift3 c4=shift4 create keep ignore");
	
	# configure lookup tables and display ranges
	imp = IJ.getImage();
	imp.setC(1);
	imp.setDisplayRange(200, 800);
	IJ.run(imp, "Grays", "");
	imp.setC(2);
	IJ.run(imp, "Blue", "");
	imp.setC(3);
	IJ.run(imp, "Green", "");
	imp.setC(4);
	IJ.run(imp, "Grays", "");
	
	# cleanup
	temp2.close();
	shift1.close();
	shift2.close();
	shift3.close();
	shift4.close();

	IJ.run(imp, "RGB Color", "");
	imp = IJ.getImage();
	IJ.saveAs(imp, "Tiff", "C:/structure/temp/temp2/00" + str(i) + ".tif");

	inputImage.close();



blurred.close();
detected_spots.close();
mesh.close();
thresholded.close();
masked.close();
temp.close();


maximumInput.close();
maximumBackgroundSubtracted.close();
maximumSpots.close();
maximumMesh.close();
