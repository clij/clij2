// meshTouchingNeighbors.ijm
//
// This macro shows how to generate a mesh between
// all points which are neighbors.
//
// Author: Robert Haase
//         October 2019
// ---------------------------------------------
run("Close All");

// Get test data
run("Blobs (25K)");
input = getTitle();
getDimensions(width, height, channels, slices, frames);

blurred = "Blurred";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// push images to GPU
Ext.CLIJ_push(input);

// cleanup ImageJ
run("Close All");

// Blur in GPU
Ext.CLIJ_blur3D(input, blurred, 5, 5, 1);
Ext.CLIJ_pull(blurred);

detected_spots = "detected_spots";
Ext.CLIJ_detectMaximaBox(blurred, detected_spots, 3);
Ext.CLIJ_pull(detected_spots);

// get spot positions as pointlist
pointlist = "pointlist";
Ext.CLIJx_spotsToPointList(detected_spots, pointlist);

Ext.CLIJx_getSize(pointlist);
number_of_detected_spots = getResult("Width", nResults() - 1);
IJ.log("number of spots: " + number_of_detected_spots);

labelled_spots = "labelled_spots";
//Ext.CLIJ_copy(detected_spots, labelled_spots);
//Ext.CLIJ_set(labelled_spots, 0);

Ext.CLIJx_connectedComponentsLabeling(detected_spots, labelled_spots);
Ext.CLIJ_pull(labelled_spots);

temp = "temp";
for (i = 0; i < 20; i++) {
	//print("hll wrld");
	Ext.CLIJx_onlyzeroOverwriteMaximumDiamond(labelled_spots, temp);
	Ext.CLIJx_onlyzeroOverwriteMaximumDiamond(temp, labelled_spots);
	//Ext.CLIJ_maximum2DSphere(labelled_spots, temp, 1, 1);
	//Ext.CLIJ_maximum2DSphere(temp, labelled_spots, 1, 1);	
}

touch_matrix = "touch_matrix";
Ext.CLIJx_generateTouchMatrix(labelled_spots, touch_matrix);
Ext.CLIJ_pull(labelled_spots);

// empty results table
run("Clear Results");

// we build a table with 2+n rows:
// x and y of the points and n rows with indices to closes points. 
// as every points is the closest to itself, row number 3 will always be 0, 1, 3, 4 ...
Ext.CLIJx_image2DToResultsTable(pointlist);
Ext.CLIJx_image2DToResultsTable(touch_matrix);

mesh = "mesh";
Ext.CLIJ_create2D(mesh, width, height, 32);
Ext.CLIJ_set(mesh, 0);

print("points: " + number_of_detected_spots);
for (p = 0; p < number_of_detected_spots; p++) {
	x1 = getResult("X" + (p), 0);
	y1 = getResult("X" + (p), 1);

    // we start at 1 (and not at 0) because we 
    // don't want to draw line from the point
    // to itself
	for (q = p; q < number_of_detected_spots; q++) {
		touching = getResult("X" + (p+1), 3 + q);
		if (touching > 0) {
		    //print("hhh");
			x2 = getResult("X" + (q), 0);
			y2 = getResult("X" + (q), 1);
	
			thickness = 1;
			Ext.CLIJx_drawLine(mesh, x1, y1, 0, x2, y2, 0, thickness);
		}
	}
}


// Get results back from GPU
Ext.CLIJ_pull(labelled_spots);
Ext.CLIJ_pull(touch_matrix);

Ext.CLIJ_pull(mesh);


// Cleanup by the end
Ext.CLIJ_clear();
Ext.CLIJ_reportMemory();



