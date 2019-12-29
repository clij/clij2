// CLIJ example macro: labeling.ijm
//
// This macro shows how to apply an automatic 
// threshold method and connected components labeling
// to an image on the GPU
//
// Author: Robert Haase
// June 2019
// ---------------------------------------------


// Get test data
run("Blobs (25K)");
//open("C:/structure/data/blobs.gif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();

mask = "mask";
labelmap = "labelmap";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push data to GPU
Ext.CLIJx_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJx_automaticThreshold(input, mask, "Otsu");

Ext.CLIJx_connectedComponentsLabeling(mask, labelmap);

// show result
Ext.CLIJx_pull(mask);

Ext.CLIJx_pull(labelmap);
run("glasbey on dark");


