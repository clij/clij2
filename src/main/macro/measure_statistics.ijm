// CLIJ example macro: measure_statistics.ijm
//
// This macro shows how to apply an automatic 
// threshold method, connected components labeling 
// and do basic measurments using CLIJ
//
// Author: Robert Haase
//         September 2019
// ---------------------------------------------


// Get test data
run("Blobs (25K)");
run("32-bit");

//open("C:/structure/data/blobs.gif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();

mask = "mask";
labelmap = "labelmap";
singleLabel = "singleLabel";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push data to GPU
Ext.CLIJx_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJx_automaticThreshold(input, mask, "Otsu");

// connected components analysis
Ext.CLIJx_connectedComponentsLabeling(mask, labelmap);

// measurements
Ext.CLIJx_statisticsOfLabelledPixels(input, labelmap);

// show labelling
Ext.CLIJx_pull(labelmap);
run("glasbey on dark");

