// CLIJ example macro: distanceMap.ijm
//
// This macro shows how to draw a distance map
// from a binary image on the GPU
//
// Author: Robert Haase
//         September 2019
// ---------------------------------------------


// Get test data
run("Blobs (25K)");
//open("C:/structure/data/blobs.gif");
input = getTitle();

mask = "mask";
labelmap = "labelmap";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// push data to GPU
Ext.CLIJ_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJx_automaticThreshold(input, mask, "Otsu");

distance_map = "distance_map";
Ext.CLIJx_distanceMap(mask, distance_map);


Ext.CLIJx_pullBinary(mask);
Ext.CLIJx_pull(distance_map);
//run("Fire");
run("Enhance Contrast", "saturated=0.35");
