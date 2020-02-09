// CLIJ example macro: rotating_sphere.ijm
//
// This macro shows how to make a sphere mesh
// rotate in  the GPU.
//
// Author: Robert Haase
//         February 2020
// ---------------------------------------------
run("Close All");

// Get test data
input = "input";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// create test data on GPU
Ext.CLIJ2_create3D(input, 200, 200, 200, 8);

// create a black stack with white pixels sitting on a sphere
numPoints = 200;
radius = 90;
center = 100;
for (i = 0; i < numPoints; i ++) {
	phi = (random * 2 - 1) * PI;
	theta = (random * 2 - 1) * PI;
	
	x = sin(phi) * cos(theta) * radius + center;
	y = sin(phi) * sin(theta) * radius + center;
	z = cos(phi) * radius + center;

	Ext.CLIJ2_drawBox(input, x, y, z, 2, 2, 2, 1);
}

// label all points in the stack with 1, 2, 3, ...
labelled_spots = "labelled_spots";
Ext.CLIJ2_connectedComponentsLabeling(input, labelled_spots);

// increase the size of the points until they touch
temp1 = "temp1";
temp2 = "temp2";
Ext.CLIJ2_copy(labelled_spots, temp1);
for (i = 0; i < 20; i++) {
	Ext.CLIJ2_onlyzeroOverwriteMaximumBox(temp1, temp2);
	Ext.CLIJ2_onlyzeroOverwriteMaximumBox(temp2, temp1);
}

// determine touch matrix from label map
touch_matrix = "touch_matrix";
Ext.CLIJ2_create2D(touch_matrix, numPoints + 1, numPoints + 1, 8);
Ext.CLIJ2_generateTouchMatrix(temp1, touch_matrix);
// ignore that all labels are touching the background
Ext.CLIJ2_setColumn(touch_matrix, 0, 0);

// get a point list: an image with coordinates
pointlist = "pointlist";
Ext.CLIJ2_create2D(pointlist, numPoints, 3, 8);
Ext.CLIJ2_labelledSpotsToPointList(labelled_spots, pointlist);

// generate a mesh from pointlist and touch_matrix
sphere3d = "sphere3D";
Ext.CLIJ2_copy(input, sphere3d);
Ext.CLIJ2_touchMatrixToMesh(pointlist, touch_matrix, sphere3d);

// show the mesh as 3D stack
Ext.CLIJ2_pullBinary(sphere3d);

// rotate the sphere around the Y axis and make a video of it
rotated_ball = "rotated_ball";
maxProjection = "maxProjection";
result_animation = "result_animation";
Ext.CLIJ2_create3D(result_animation, 200, 200, 200, 8);
for (r = 0; r < 200; r += 1) {
	Ext.CLIJ2_rotate3D(sphere3d, rotated_ball, 0, r/200*360, 0, true);
	
	Ext.CLIJ2_maximumZProjection(rotated_ball, maxProjection);

	Ext.CLIJ2_copySlice(maxProjection, result_animation, r);
}

// show animation
Ext.CLIJ2_pullBinary(result_animation);

