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
Ext.CLIJ_clear();

// push data to GPU
Ext.CLIJ_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJ_automaticThreshold(input, mask, "Otsu");

Ext.CLIJx_connectedComponentsLabeling(mask, labelmap);

Ext.CLIJx_statisticsOfLabelledPixels(input, labelmap);

// show result
Ext.CLIJ_pull(mask);
run("glasbey on dark");



