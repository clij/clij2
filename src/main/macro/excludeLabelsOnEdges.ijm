// CLIJ example macro: excludeLabelsOnEdges.ijm
//
// This macro shows how to remove objects in
// label maps which touch the image edge on the GPU
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
Ext.CLIJx_clear();

// push data to GPU
Ext.CLIJx_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJx_automaticThreshold(input, mask, "Otsu");

Ext.CLIJx_connectedComponentsLabeling(mask, labelmap);

labelmap_without_edges = "labelmap_without_edges";
Ext.CLIJx_excludeLabelsOnEdges(labelmap, labelmap_without_edges);

// show result
Ext.CLIJx_pull(labelmap);
run("glasbey on dark");

Ext.CLIJx_pull(labelmap_without_edges);
run("glasbey on dark");


