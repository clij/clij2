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

Ext.CLIJx_connectedComponentsLabeling(mask, labelmap);

Ext.CLIJx_maximumOfAllPixels(labelmap);
numberOfObjects = getResult("Max", nResults() - 1);

for (i = 0; i < numberOfObjects; i++) {
	Ext.CLIJx_maskLabel(input, labelmap, singleLabel, i + 1);
	
	Ext.CLIJx_sumOfAllPixels(singleLabel);
	sum = getResult("Sum", nResults() - 1);
	
	Ext.CLIJx_create2D("white", width, height, 8);
	Ext.CLIJx_set("white", 1);
	
	Ext.CLIJx_maskLabel("white", labelmap, singleLabel, i + 1);
	Ext.CLIJx_countNonZeroPixels(singleLabel);
	count = getResult("CountNonZero", nResults() - 1);
	
	mean = sum / count;
	IJ.log("Object " + i + ": pixelcount: " + count + " mean " + mean );
}

// show result
Ext.CLIJx_pull(mask);

Ext.CLIJx_pull(singleLabel);



