# CLIJ example macro: mesh_closest_points.py
#
# This script shows how to draw lines between closest points in the GPU.
#
# Author: Robert Haase
#         September 2019
# ---------------------------------------------

# Get test data
from ij import IJ;
imp = IJ.openImage("http://wsr.imagej.net/images/blobs.gif");
# IJ.openImage("C:/structure/data/blobs.gif");

# Init GPU
from net.haesleinhuepf.clijx import CLIJx;
clijx = CLIJx.getInstance();

# push data to GPU
inputImage = clijx.push(imp);

# blur a bit and detect maxima
from java.lang import Float;
blurred = clijx.create(inputImage);
detected_spots = clijx.create(inputImage);

clijx.op().blur(inputImage, blurred, Float(15), Float(15), Float(0));

print(str(blurred));

from java.lang import Integer;
clijx.op().detectMaximaBox(blurred, detected_spots, Integer(10));

clijx.show(detected_spots, "spots");

# convert spots image to spot list
number_of_spots = clijx.op().sumPixels(detected_spots);
pointlist = clijx.create([number_of_spots, 2]);
clijx.op().spotsToPointList(detected_spots, pointlist);

distance_matrix = clijx.create([number_of_spots, number_of_spots]);
clijx.op().generateDistanceMatrix(pointlist, pointlist, distance_matrix);

n_closest_points = 5;
closestPointsIndices = clijx.create([number_of_spots, n_closest_points]);
clijx.op().nClosestPoints(distance_matrix, closestPointsIndices);

pointCoodinates = clijx.pull(pointlist).getProcessor();
pointIndices = clijx.pull(closestPointsIndices).getProcessor();

mesh = clijx.create(inputImage);

for p in range(0, pointIndices.getWidth()):
	x1 = pointCoodinates.getf(p, 0);
	y1 = pointCoodinates.getf(p, 1);

	for q in range(1, n_closest_points):
		pointIndex = int(pointIndices.getf(p, q));
		x2 = pointCoodinates.getf(pointIndex, 0);
		y2 = pointCoodinates.getf(pointIndex, 1);

		thickness = 1;
		clijx.op().drawLine(mesh, x1, y1, 0, x2, y2, 0, thickness);

# show result
clijx.show(mesh, "mesh");