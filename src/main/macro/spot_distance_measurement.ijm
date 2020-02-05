// CLIJ example macro: spot_distance_measurement.ijm
//
// This macro shows how find spots in an image, shifts
// the spots to simulate motion and measures the shift 
// afterwards by determining closest neighbors between
// the images.
//
// Author: Robert Haase
// July 2019 in Woods Hole
// ---------------------------------------------


// Get test data
// run("Blobs (25K)");
open("C:/structure/data/blobs.tif");

getDimensions(width, height, channels, slices, frames);
input = getTitle();

mask = "mask";
labelmap = "labelmap";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push data to GPU
Ext.CLIJ2_push(input);

// cleanup ImageJ
run("Close All");

blurred = "blurred";

Ext.CLIJ2_gaussianBlur2D(input, blurred, 5, 5);

detected = "detected";
Ext.CLIJ2_detectMaximaBox(blurred, detected, 3);
Ext.CLIJ2_pull(detected);


shiftDetected = "shiftDetected";
Ext.CLIJ2_translate2D(detected, shiftDetected, 1, 0);
Ext.CLIJ2_pull(shiftDetected);

pointlist1 = "pointlist1";
Ext.CLIJ2_spotsToPointList(detected, pointlist1);
Ext.CLIJ2_pull(pointlist1);

pointlist2 = "pointlist2";
Ext.CLIJ2_spotsToPointList(shiftDetected, pointlist2);
Ext.CLIJ2_pull(pointlist2);

distance_matrix = "distance_matrix";
Ext.CLIJ2_generateDistanceMatrix(pointlist1, pointlist2, distance_matrix);

Ext.CLIJ2_pull(distance_matrix);

Ext.CLIJ2_transposeXZ(distance_matrix, "test");
Ext.CLIJ2_pull("test");

minimum_distances = "minimum_distances";
Ext.CLIJ2_shortestDistances(distance_matrix, minimum_distances);

Ext.CLIJ2_meanOfAllPixels(minimum_distances);
meanDistance = getResult("Mean", nResults() - 1);
IJ.log("mean distance: " + meanDistance);


Ext.CLIJ2_pull(minimum_distances);



