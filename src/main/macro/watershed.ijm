// CLIJ example macro: watershed.ijm
//
// This macro shows how to apply watershed
// to a binary image on the GPU
//
// Author: Robert Haase
//         September 2019 in Prague
// ---------------------------------------------


// Get test data
run("Blobs (25K)");

//open("C:/structure/data/blobs.gif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();

mask = "mask";
watersheded = "watersheded";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// push data to GPU
Ext.CLIJ_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJ_automaticThreshold(input, mask, "Otsu");

// apply watershed
Ext.CLIJx_watershed(mask, watersheded);

// show result
Ext.CLIJ_pull(watersheded);



