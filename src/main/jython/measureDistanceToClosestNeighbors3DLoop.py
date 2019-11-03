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
from net.haesleinhuepf.clij.coremem.enums import NativeTypeEnum

from java.lang import String



foldername = "C:/structure/data/2019-10-28-17-22-59-23-Finsterwalde_Tribolium_nGFP/stacks/C0opticsprefused/";
outputFolder = "C:/structure/temp/";

# Init GPU
clijx = CLIJx.getInstance();

pushedImage = clijx.create([512, 1024, 67], NativeTypeEnum.UnsignedShort);
for i in range(1265, 1299, 1):
	IJ.run("Close All");	
	clijx.stopWatch("");

	# load/create example images
	# imp = IJ.openImage("C:/structure/data/Nantes_000646.tif");
	# imp = IJ.openImage("C:/structure/data/Riga_000512.tif");
	# imp = IJ.openImage("C:/structure/data/Pau_001130.tif");
	# imp = IJ.openImage("C:/structure/data/Finsterwalde_001250.tif");

	
	filename = "0000000" + str(i)
	filename = String(filename).substring(len(filename) - 6) + ".raw";

	print(foldername + filename)
	# break;

	
	clijx.readRawImageFromDisc(pushedImage, foldername + filename)
	
	# IJ.run(imp, "32-bit", "");
	# IJ.run(imp, "Rotate 90 Degrees Right", "");
	# imp.show();
	
	
	factorX = 0.347 * 2 * 1.5;
	factorY = 0.347 * 2 * 1.5;
	factorZ = 3 * 1.5;
	
	# push data to GPU
	# pushedImage = clijx.push(imp);
	downsampledImage = clijx.create([pushedImage.getWidth() * factorX, pushedImage.getHeight() * factorY, pushedImage.getDepth() * factorZ]);

	clijx.downsample(pushedImage, downsampledImage, factorX, factorY, factorZ);

	inputImage = clijx.create([pushedImage.getHeight() * factorY, pushedImage.getWidth() * factorX, pushedImage.getDepth() * factorZ]);
	clijx.rotateRight( downsampledImage, inputImage);

	downsampledImage.close();
	#clijx.saveAsTIF( inputImage,                outputFolder + "_input/" + filename + ".tif");
	
	
	# blur a bit and detect maxima
	blurred = clijx.create(inputImage);
	thresholded = clijx.create(inputImage);
	detected_spots = clijx.create(inputImage);
	masked = clijx.create(inputImage);
	clijx.stopWatch("alloc");
	
	# background / noise removal
	clijx.differenceOfGaussian(inputImage, masked, 3, 3, 0, 15, 15, 0);
	clijx.absolute(masked, blurred);
	
	# clijx.op().blur(inputImage, blurred, 3, 3, 0);
	
	clijx.stopWatch("dog");
	#clijx.show(blurred, "blurred");
	
	# spot detection
	clijx.op().detectMaximaBox(blurred, detected_spots, 1);
	clijx.stopWatch("detect");
	# remove spots in background
	clijx.op().automaticThreshold(blurred, thresholded, "Otsu");
	clijx.stopWatch("threshold");
	#clijx.show(thresholded, "thresholded");
	clijx.op().mask(detected_spots, thresholded, masked);
	clijx.op().copy(masked, detected_spots);
	#clijx.show(detected_spots, "spots");
	clijx.stopWatch("spots");
	
	# convert spots image to spot list
	number_of_spots = clijx.op().sumPixels(detected_spots);
	pointlist = clijx.create([number_of_spots, 3]);
	clijx.op().spotsToPointList(detected_spots, pointlist);
	clijx.stopWatch("pointlist");
	
	###########################################################################
	tempSpots1 = clijx.create(detected_spots);
	tempSpots2 = clijx.create(detected_spots);
	flag = clijx.create([1, 1, 1]);
	
	clijx.op().connectedComponentsLabeling(detected_spots, tempSpots1);
	clijx.stopWatch("cca");
	
	for j in range(0, 10):
		#print("helllo " + str(j));
		clijx.op().onlyzeroOverwriteMaximumDiamond(tempSpots1, flag, tempSpots2);
		clijx.op().onlyzeroOverwriteMaximumBox(tempSpots2, flag, tempSpots1);
	clijx.stopWatch("cells");
	
	tempSpots3 = clijx.create(detected_spots);
	clijx.op().threshold(tempSpots1, tempSpots2, 1);
	for j in range(0, 3):
		clijx.op().erodeBox(tempSpots2, tempSpots3);
		clijx.op().erodeBox(tempSpots3, tempSpots2);
	clijx.op().erodeBox(tempSpots2, tempSpots3);
	clijx.copy(tempSpots1, tempSpots2);
	clijx.mask(tempSpots2, tempSpots3, tempSpots1);
	tempSpots3.close();
	
	#clijx.show(tempSpots1, "tempSpots1");
	
	# determine which labels touch
	touch_matrix = clijx.create([number_of_spots+1, number_of_spots+1]);
	clijx.op().generateTouchMatrix(tempSpots1, touch_matrix);
	clijx.stopWatch("touch matrix");
	#clijx.show(touch_matrix, "touch_matrix");
	
	# determine how close dots are
	distance_matrix = clijx.create([number_of_spots+1, number_of_spots+1]);
	clijx.generateDistanceMatrix(pointlist, pointlist, distance_matrix);
	
	#clijx.show(distance_matrix, "distance_matrix");
	
	
	
	neighbor_count_vector = clijx.create([distance_matrix.getWidth(), 1, 1]);
	# clijx.countTouchingNeighbors( touch_matrix, neighbor_count_vector );
	clijx.stopWatch("before");
	clijx.averageDistanceOfClosestPoints(distance_matrix, neighbor_count_vector, 5);
	clijx.stopWatch("after");
	
	neighbor_count_map = clijx.create(detected_spots);
	clijx.replaceIntensities( tempSpots1, neighbor_count_vector, neighbor_count_map );

	#clijx.saveAsTIF( neighbor_count_map,                outputFolder + "_neigh/" + filename + ".tif");
	
	
	#clijx.show(neighbor_count_map, "neighbor_count_map");
	#clijx.show(neighbor_count_vector, "neighbor_count_vector");
	neighbor_count_vector.close();
	
	
	
	
	
	
	
	# find n closes spots for every spot
	#n_closest_points = 6;
	#closestPointsIndices = clijx.create([number_of_spots, n_closest_points]);
	#clijx.op().nClosestPoints(distance_matrix, closestPointsIndices);
	distance_matrix.close();
	
	# build and visualise mesh
	pointCoodinates = clijx.pull(pointlist).getProcessor();
	touchFlags = clijx.pull(touch_matrix).getProcessor();
	
	mesh = clijx.create(inputImage);
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
				clijx.op().drawLine(mesh, x1, y1, z1, x2, y2, z2, thickness);
				#clijx.drawTwoValueLine(neighbor_count_map, x1, y1, z1, x2, y2, z2, 10, p, q);
	
	###########################################################################
	clijx.stopWatch("mesh");

	#clijx.saveAsTIF( mesh,                outputFolder + "_mesh/" + filename + ".tif");

	
	#clijx.show(mesh, "mesh");
	clijx.stopWatch("show mesh");
	
	
	# make spots bigger for visualisation
	temp = clijx.create(detected_spots);
	clijx.op().dilateBox(detected_spots, temp);
	clijx.op().dilateSphere(temp, detected_spots);
	
	# generate maximum projections of all results
	maximumInput = clijx.create([inputImage.getWidth(), inputImage.getHeight()]);
	maximumBackgroundSubtracted = clijx.create(maximumInput);
	maximumSpots = clijx.create(maximumInput);
	maximumMesh = clijx.create(maximumInput);
	maximumNeighborCount = clijx.create(maximumInput);
	clijx.op().maximumZProjection(inputImage, maximumInput);
	clijx.op().maximumZProjection(blurred, maximumBackgroundSubtracted);
	clijx.op().maximumZProjection(detected_spots, maximumSpots);
	clijx.op().maximumZProjection(mesh, maximumMesh);
	clijx.op().meanZProjection(neighbor_count_map, maximumNeighborCount);


	clijx.saveAsTIF( maximumInput,                outputFolder + "_inputMax/" + filename + ".tif");
	clijx.saveAsTIF( maximumBackgroundSubtracted, outputFolder + "_bgsubMax/" + filename + ".tif");
	clijx.saveAsTIF( maximumSpots,                outputFolder + "_spotsMax/" + filename + ".tif");
	clijx.saveAsTIF( maximumMesh,                 outputFolder + "_meshMax/" + filename + ".tif");
	clijx.saveAsTIF( maximumNeighborCount,        outputFolder + "_neighMax/" + filename + ".tif");
	
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
	clijx.show(maximumInput, "maximumInput");
	clijx.show(maximumBackgroundSubtracted, "maximumBackgroundSubtracted");
	clijx.show(maximumSpots, "maximumSpots");
	clijx.show(maximumMesh, "maximumMesh");
	clijx.show(maximumNeighborCount, "maximumNeighborCount");
	
	# blending visualisation
	temp = clijx.create(maximumInput);
	imp = clijx.pull(temp);
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
	
	shift2 = clijx.push(imp);
	IJ.run(imp, "Translate...", "x=" + str(bandwidth) + " y=0 interpolation=None");
	shift3 = clijx.push(imp);
	IJ.run(imp, "Translate...", "x=" + str(bandwidth) + " y=0 interpolation=None");
	shift4 = clijx.push(imp);
	IJ.run(imp, "Translate...", "x=" + str(bandwidth) + " y=0 interpolation=None");
	shift5 = clijx.push(imp);
	shift1 = clijx.create(shift5);
	clijx.flip( shift5, shift1, True, False, False);
	
	temp = clijx.create(shift1);
	
	clijx.op().multiplyImages(maximumInput, shift1, temp);
	clijx.show(temp, "shift1");
	clijx.op().multiplyImages(maximumBackgroundSubtracted, shift2, temp);
	clijx.show(temp, "shift2");
	clijx.op().multiplyImages(maximumSpots, shift3, temp);
	clijx.show(temp, "shift3");
	clijx.op().multiplyImages(maximumMesh, shift4, temp);
	clijx.show(temp, "shift4");
	clijx.op().multiplyImages(maximumNeighborCount, shift5, temp);
	# maximumNeighborCount.copyTo(temp, True);
	clijx.show(temp, "shift5");
	
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
	imp.setDisplayRange(-1, imp.getDisplayRangeMax());
	IJ.run(imp, "glow", "");
	imp.updateAndDraw();
	
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
	shift5.close();

	finalVis = clijx.push(IJ.getImage());
	clijx.saveAsTIF(finalVis, outputFolder + "_finalVis/" + filename + ".tif")
	finalVis.close();
	#break
pushedImage.close();
