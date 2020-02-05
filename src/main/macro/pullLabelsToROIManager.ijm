// CLIJ example macro: pullLabelsToROIManager.ijm
//
// This macro shows how to apply an automatic 
// threshold method, run connected components analysis
// and get all labels as ROI into the 
// ROIManager from the GPU
//
// Author: Robert Haase
//         September 2019
// ---------------------------------------------

run("Close All");

// Get test data
run("Blobs (25K)");
//open("C:/structure/data/blobs.gif");
input = getTitle();

mask = "mask";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push data to GPU
Ext.CLIJ2_push(input);

// create a mask using a fixed threshold
Ext.CLIJ2_automaticThreshold(input, mask, "Otsu");

// differentiate labels
label_map = "label_map";
Ext.CLIJ2_connectedComponentsLabeling(mask, label_map);

// remove labels touching edges
removed_edge_labels = "removed_edge_labels";
Ext.CLIJ2_excludeLabelsOnEdges(label_map, removed_edge_labels);

// send results to ROI manager
Ext.CLIJ2_pullLabelsToROIManager(removed_edge_labels);
roiManager("show all");

