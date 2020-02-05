// CLIJ example macro: labeling.ijm
//
// This macro shows how to apply an automatic 
// threshold method and connected components labeling
// to an image on the GPU
//
// Author: Robert Haase
// June 2019
// ---------------------------------------------
run("Close All");

// Get test data
run("Blobs (25K)");

getDimensions(width, height, channels, slices, frames);
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

time = getTime();
Ext.CLIJ2_connectedComponentsLabeling(mask, labelmap);

print("CLIJ took" + (getTime() - time) + " msec"); 
// show result
Ext.CLIJ2_pull(labelmap);
run("glasbey on dark");

Ext.CLIJ2_pull(mask);
setThreshold(1, 1);
setOption("BlackBackground", true);
run("Convert to Mask");

time = getTime();
run("Analyze Particles...", "  show=[Count Masks]");
print("IJ took" + (getTime() - time) + " msec"); 


