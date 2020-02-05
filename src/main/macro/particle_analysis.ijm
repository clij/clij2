// CLIJ example macro: particle_analysis.ijm
//
// This macro shows how to apply an automatic 
// threshold method, apply connected components labeling
// and measure properties of the resulting segmentations
// in an image on the GPU
//
// Author: Robert Haase
//         September 2019 in Prague
// ---------------------------------------------


// Get test data
run("Blobs (25K)");
run("32-bit");

//open("C:/structure/data/blobs.gif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();

mask = "mask";
labelmap = "labelmap";
singleLabelMask = "singleLabelMask";

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

Ext.CLIJ2_statisticsOfLabelledPixels(input, labelmap);

// show result
Ext.CLIJ2_pull(labelmap);
run("glasbey on dark");



