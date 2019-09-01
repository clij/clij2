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
from net.haesleinhuepf.clij2 import CLIJ2;
clij2 = CLIJ2.getInstance();

# push data to GPU
inputImage = clij2.push(imp);

# blur a bit and detect maxima
from java.lang import Float;
blurred = clij2.create(inputImage);
detected_spots = clij2.create(inputImage);

clij2.op().blur(inputImage, blurred, Float(15), Float(15), Float(0));

print(str(blurred));

from java.lang import Integer;
clij2.op().detectMaximaBox(blurred, detected_spots, Integer(10));

clij2.show(detected_spots, "spots");

# convert spots image to spot list
number_of_spots = clij2.op().sumPixels(detected_spots);
pointlist = clij2.create([number_of_spots, 2]);
clij2.op().spotsToPointList(detected_spots, pointlist);

distance_matrix = clij2.create([number_of_spots, number_of_spots]);
clij2.op().generateDistanceMatrix(pointlist, pointlist, distance_matrix);

n_closest_points = 5;
closestPointsIndices = clij2.create([number_of_spots, n_closest_points]);
clij2.op().nClosestPoints(distance_matrix, closestPointsIndices);

pointCoodinates = clij2.pull(pointlist).getProcessor();
pointIndices = clij2.pull(closestPointsIndices).getProcessor();

mesh = clij2.create(inputImage);

for p in range(0, pointIndices.getWidth()):
	x1 = pointCoodinates.getf(p, 0);
	y1 = pointCoodinates.getf(p, 1);

	for q in range(1, n_closest_points):
		pointIndex = int(pointIndices.getf(p, q));
		x2 = pointCoodinates.getf(pointIndex, 0);
		y2 = pointCoodinates.getf(pointIndex, 1);

		thickness = 1;
		clij2.op().drawLine(mesh, x1, y1, 0, x2, y2, 0, thickness);

# show result
clij2.show(mesh, "mesh");