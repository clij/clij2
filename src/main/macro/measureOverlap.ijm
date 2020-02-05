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
Ext.CLIJx_clear();

// push data to GPU
Ext.CLIJx_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJx_automaticThreshold(input, mask1, "Otsu");

// create another mask to compare to 
Ext.CLIJx_automaticThreshold(input, mask2, "MinError");

// measure overlap
Ext.CLIJx_jaccardIndex(mask1, mask2);
jaccardIndex = getResult("Jaccard_Index", nResults() - 1);
Ext.CLIJx_sorensenDiceCoefficient(mask1, mask2);
diceIndex = getResult("Sorensen_Dice_coefficient", nResults() - 1);

// cleanup GPU memory
Ext.CLIJx_clear();

// output result
IJ.log("Overlap (Jaccard): " + (jaccardIndex*100) + "%");
IJ.log("Overlap (Dice): " + (diceIndex*100) + "%");

