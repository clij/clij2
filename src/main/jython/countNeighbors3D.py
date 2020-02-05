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
from net.haesleinhuepf.clij2 import CLIJ2;
from net.haesleinhuepf.clijx import CLIJx;

IJ.run("Close All");

# load/create example images
# imp = IJ.openImage("C:/structure/data/Nantes_000646.tif");
# imp = IJ.openImage("C:/structure/data/Riga_000512.tif");
# imp = IJ.openImage("C:/structure/data/Pau_001130.tif");
# imp = IJ.openImage("C:/structure/data/Finsterwalde_001250.tif");
imp = IJ.openImage("C:/structure/data/2018-05-23-16-18-13-89-Florence_multisample/processed/tif/000450.raw.tif");

IJ.run(imp, "32-bit", "");
IJ.run(imp, "Rotate 90 Degrees Right", "");
imp.show();

# Init GPU
clij2 = CLIJ2.getInstance("2060");
clijx.stopWatch("");

# push data to GPU
inputImage = clij2.push(imp);

# blur a bit and detect maxima
blurred = clij2.create(inputImage);
thresholded = clij2.create(inputImage);
detected_spots = clij2.create(inputImage);
masked = clij2.create(inputImage);
clijx.stopWatch("alloc");

# background / noise removal
clij2.differenceOfGaussian(inputImage, blurred, 3, 3, 0, 15, 15, 0);
# clijx.op().blur(inputImage, blurred, 3, 3, 0);

clijx.stopWatch("dog");
#clijx.show(blurred, "blurred");

# spot detection
clij2.detectMaximaBox(blurred, detected_spots, 1);
clijx.stopWatch("detect");
# remove spots in background
clij2.automaticThreshold(blurred, thresholded, "Otsu");
clijx.stopWatch("threshold");
#clijx.show(thresholded, "thresholded");
clij2.mask(detected_spots, thresholded, masked);
clij2.copy(masked, detected_spots);
#clijx.show(detected_spots, "spots");
clijx.stopWatch("spots");

# convert spots image to spot list
number_of_spots = clij2.sumOfAllPixels(detected_spots);
pointlist = clij2.create([number_of_spots, 3]);
clij2.spotsToPointList(detected_spots, pointlist);
clijx.stopWatch("pointlist");

###########################################################################
tempSpots1 = clij2.create(detected_spots);
tempSpots2 = clij2.create(detected_spots);
flag = clij2.create([1, 1, 1]);

clij2.connectedComponentsLabeling(detected_spots, tempSpots1);
clijx.stopWatch("cca");

for j in range(0, 15):
	#print("helllo " + str(j));
	clij2.onlyzeroOverwriteMaximumDiamond(tempSpots1, flag, tempSpots2);
	clij2.onlyzeroOverwriteMaximumBox(tempSpots2, flag, tempSpots1);
clijx.stopWatch("cells");

#tempSpots3 = clijx.create(detected_spots);
#clijx.op().threshold(tempSpots1, tempSpots2, 1);
#for j in range(0, 6):
#	clijx.op().erodeBox(tempSpots2, tempSpots3);
#	clijx.op().erodeBox(tempSpots3, tempSpots2);
#clijx.op().erodeBox(tempSpots2, tempSpots3);
#clijx.copy(tempSpots1, tempSpots2);
#clijx.mask(tempSpots2, tempSpots3, tempSpots1);
#tempSpots3.close();

clijx.show(tempSpots1, "tempSpots1");

# determine which labels touch
touch_matrix = clij2.create([number_of_spots+1, number_of_spots+1]);
clij2.generateTouchMatrix(tempSpots1, touch_matrix);
clijx.stopWatch("touch matrix");

clij2.show(touch_matrix, "touch_matrix");


neighbor_count_vector = clij2.create([touch_matrix.getWidth(), 1, 1]);
clij2.countTouchingNeighbors( touch_matrix, neighbor_count_vector );

neighbor_count_map = clij2.create(detected_spots);
clij2.replaceIntensities( tempSpots1, neighbor_count_vector, neighbor_count_map );

clij2.show(neighbor_count_map, "neighbor_count_map");
clij2.show(neighbor_count_vector, "neighbor_count_vector");
neighbor_count_vector.close();


# find n closes spots for every spot
#n_closest_points = 6;
#closestPointsIndices = clijx.create([number_of_spots, n_closest_points]);
#clijx.op().nClosestPoints(distance_matrix, closestPointsIndices);

# build and visualise mesh
pointCoodinates = clij2.pull(pointlist).getProcessor();
touchFlags = clij2.pull(touch_matrix).getProcessor();

mesh = clij2.create(inputImage);
clijx.stopWatch("points");

#clijx.set(neighbor_count_map, 0);

for p in range(0, pointCoodinates.getWidth()):
	x1 = pointCoodinates.getf(p, 0);
	y1 = pointCoodinates.getf(p, 1);
	z1 = pointCoodinates.getf(p, 2);

	for q in range(p, pointCoodinates.getWidth()):
		touching = int(touchFlags.getf(p+1, q+1));
		if (touching > 0):
			x2 = pointCoodinates.getf(q, 0);
			y2 = pointCoodinates.getf(q, 1);
			z2 = pointCoodinates.getf(q, 2);
	
			thickness = 1;
			clij2.drawLine(mesh, x1, y1, z1, x2, y2, z2, thickness);
			#clijx.drawTwoValueLine(neighbor_count_map, x1, y1, z1, x2, y2, z2, 10, p, q);

###########################################################################
clijx.stopWatch("mesh");

clij2.show(mesh, "mesh");
clijx.stopWatch("show mesh");


# make spots bigger for visualisation
temp = clij2.create(detected_spots);
clij2.dilateBox(detected_spots, temp);
clij2.dilateSphere(temp, detected_spots);

# generate maximum projections of all results
maximumInput = clij2.create([inputImage.getWidth(), inputImage.getHeight()]);
maximumBackgroundSubtracted = clij2.create(maximumInput);
maximumSpots = clij2.create(maximumInput);
maximumMesh = clij2.create(maximumInput);
maximumNeighborCount = clij2.create(maximumInput);
clij2.maximumZProjection(inputImage, maximumInput);
clij2.maximumZProjection(blurred, maximumBackgroundSubtracted);
clij2.maximumZProjection(detected_spots, maximumSpots);
clij2.maximumZProjection(mesh, maximumMesh);
clij2.maximumZProjection(neighbor_count_map, maximumNeighborCount);

clijx.stopWatch("maxproj");


# intermediate cleanup
inputImage.close();
blurred.close();
detected_spots.close();
pointlist.close();
touch_matrix.close();
#closestPointsIndices.close();
mesh.close();
thresholded.close();
masked.close();
tempSpots1.close();
tempSpots2.close();
neighbor_count_map.close();

clijx.stopWatch("cleanup1");


# show results
clij2.show(maximumInput, "maximumInput");
clij2.show(maximumBackgroundSubtracted, "maximumBackgroundSubtracted");
clij2.show(maximumSpots, "maximumSpots");
clij2.show(maximumMesh, "maximumMesh");
clij2.show(maximumNeighborCount, "maximumNeighborCount");

# blending visualisation
temp = clij2.create(maximumInput);
imp = clij2.pull(temp);
temp.close();
ip = imp.getProcessor();
bandwidth = ip.getWidth() / 5
for x in range(0, bandwidth):
	for y in range(0, ip.getHeight()):
		ip.setf(x, y, 1.0 * x / bandwidth);
		ip.setf(x + bandwidth, y, 1);
		ip.setf(x + bandwidth * 2, y, 1.0 - 1.0 * x / bandwidth);
		ip.setf(x + bandwidth * 3, y, 0);
imp.show();

shift2 = clij2.push(imp);
IJ.run(imp, "Translate...", "x=" + str(bandwidth) + " y=0 interpolation=None");
shift3 = clij2.push(imp);
IJ.run(imp, "Translate...", "x=" + str(bandwidth) + " y=0 interpolation=None");
shift4 = clij2.push(imp);
IJ.run(imp, "Translate...", "x=" + str(bandwidth) + " y=0 interpolation=None");
shift5 = clij2.push(imp);
shift1 = clij2.create(shift5);
clij2.flip( shift5, shift1, True, False, False);

temp = clij2.create(shift1);

clij2.multiplyImages(maximumInput, shift1, temp);
clij2.show(temp, "shift1");
clij2.multiplyImages(maximumBackgroundSubtracted, shift2, temp);
clij2.show(temp, "shift2");
clij2.multiplyImages(maximumSpots, shift3, temp);
clij2.show(temp, "shift3");
clij2.multiplyImages(maximumMesh, shift4, temp);
clij2.show(temp, "shift4");
clij2.multiplyImages(maximumNeighborCount, shift5, temp);
clij2.show(temp, "shift5");

IJ.run(imp, "Merge Channels...", "c1=shift1 c2=shift2 c3=shift3 c4=shift4 c5=shift5 create keep ignore");

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
imp.setC(5);
IJ.run(imp, "Red", "");

# cleanup
maximumInput.close();
maximumBackgroundSubtracted.close();
maximumSpots.close();
maximumMesh.close();
maximumNeighborCount.close();
temp.close();
shift1.close();
shift2.close();
shift3.close();
shift4.close();

clij2.clear();
