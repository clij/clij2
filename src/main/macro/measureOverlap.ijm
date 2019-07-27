// CLIJ example macro: measureOverlap.ijm
//
// This macro shows how to measure the overlap 
// of two binary images in the GPU.
//
// Author: Robert Haase
// July 2019
// ---------------------------------------------


// Get test data
run("Blobs (25K)");
//open("C:/structure/data/blobs.gif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();

mask1 = "Mask1";
mask2 = "Mask2";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// push data to GPU
Ext.CLIJ_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJ_automaticThreshold(input, mask1, "Otsu");

// create another mask to compare to 
Ext.CLIJ_automaticThreshold(input, mask2, "Huang");

// measure overlap
Ext.CLIJ_sorensenDiceJaccardIndex(mask1, mask2);
jaccardIndex = getResult("Jaccard_Index", nResults() - 1);

// cleanup GPU memory
Ext.CLIJ_clear();

// output result
IJ.log("Overlap: " + jaccardIndex + "%");



