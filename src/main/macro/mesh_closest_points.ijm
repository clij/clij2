// CLIJ example macro: mesh_closest_pointsijm
//
// This macro shows how to draw lines between closest points in the GPU.
//
// Author: Robert Haase
//         September 2019
// ---------------------------------------------


// Get test data
run("Blobs (25K)");
//open("C:/structure/data/blobs.gif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();
threshold = 128;

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push data to GPU
Ext.CLIJ2_push(input);

// cleanup ImageJ
run("Close All");

// blur
blurred = "blurred";
Ext.CLIJ2_blur2D(input, blurred, 15, 15);

// detect spots
detected_spots = "detected_spots";
Ext.CLIJ2_detectMaximaBox(blurred, detected_spots, 10);

Ext.CLIJ2_pullBinary(detected_spots);

// get spot positions as pointlist
pointlist = "pointlist";
Ext.CLIJ2_spotsToPointList(detected_spots, pointlist);
Ext.CLIJ2_pull(pointlist);

Ext.CLIJ2_getSize(pointlist);
number_of_detected_spots = getResult("Width", nResults() - 1);
IJ.log("number of spots: " + number_of_detected_spots);

// determine distances between points
distance_matrix = "distance_matrix";
Ext.CLIJ2_generateDistanceMatrix(pointlist, pointlist, distance_matrix);
Ext.CLIJ2_pull(distance_matrix);

// determine n closest points
n_closest_points = 5;
closestPointsIndices = "closestPointsIndices";
Ext.CLIJ2_nClosestPoints(distance_matrix, closestPointsIndices, n_closest_points);

// empty results table
run("Clear Results");

// we build a table with 2+n rows:
// x and y of the points and n rows with indices to closes points. 
// as every points is the closest to itself, row number 3 will always be 0, 1, 3, 4 ...
Ext.CLIJ2_image2DToResultsTable(pointlist);
Ext.CLIJ2_image2DToResultsTable(closestPointsIndices);

mesh = "mesh";
Ext.CLIJ2_create2D(mesh, width, height, 32);
Ext.CLIJ2_set(mesh, 0);

for (p = 0; p < number_of_detected_spots; p++) {
	x1 = getResult("X" + p, 0);
	y1 = getResult("X" + p, 1);

    // we start at 1 (and not at 0) because we 
    // don't want to draw line from the point
    // to itself
	for (q = 1; q < n_closest_points; q++) {
		pointIndex = getResult("X" + p, q + 2);
		x2 = getResult("X" + pointIndex, 0);
		y2 = getResult("X" + pointIndex, 1);

		thickness = 1;
		Ext.CLIJ2_drawLine(mesh, x1, y1, 0, x2, y2, 0, thickness);
	}
}

// show result
Ext.CLIJ2_pullBinary(mesh);


