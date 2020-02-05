// CLIJ example macro: getsize.ijm
//
// This macro shows how to get the dimensions 
// of an image on the GPU
//
// Author: Robert Haase
//         September 2019
// ---------------------------------------------


// Get test data
run("Blobs (25K)");
input = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push data to GPU
Ext.CLIJ2_push(input);

Ext.CLIJ2_getSize(input);
width = getResult("Width", nResults() - 1);
height = getResult("Height", nResults() - 1);

IJ.log("Width: " + width);
IJ.log("Height: " + height);
