// CLIJ example macro: outline.ijm
//
// This macro shows how to apply an automatic 
// threshold method and get the outline of the 
// found objects an image on the GPU
//
// Author: Robert Haase
//         September 2019
// ---------------------------------------------


// Get test data
run("Blobs (25K)");
//open("C:/structure/data/blobs.gif");
input = getTitle();

mask = "mask";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push data to GPU
Ext.CLIJx_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJx_automaticThreshold(input, mask, "Otsu");

outline = "outline";
Ext.CLIJx_binaryEdgeDetection(mask, outline);

Ext.CLIJx_pullBinary(outline);

