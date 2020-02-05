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
Ext.CLIJ2_clear();

// push data to GPU
Ext.CLIJ2_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJ2_automaticThreshold(input, mask, "Otsu");

Ext.CLIJ2_connectedComponentsLabeling(mask, labelmap);
Ext.CLIJ2_pull(labelmap);

labelmap_without_edges = "labelmap_without_edges";
Ext.CLIJ2_excludeLabelsOnEdges(labelmap, labelmap_without_edges);

// show result
Ext.CLIJ2_pull(labelmap);
run("glasbey on dark");

Ext.CLIJ2_pull(labelmap_without_edges);
run("glasbey on dark");


