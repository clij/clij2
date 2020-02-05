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
Ext.CLIJ2_clear();

// push data to GPU
Ext.CLIJ2_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJ2_automaticThreshold(input, mask, "Otsu");

Ext.CLIJ2_connectedComponentsLabeling(mask, labelmap);

Ext.CLIJ2_maximumOfAllPixels(labelmap);
numberOfObjects = getResult("Max", nResults() - 1);

for (i = 0; i < numberOfObjects; i++) {
	Ext.CLIJ2_maskLabel(input, labelmap, singleLabel, i + 1);
	
	Ext.CLIJ2_sumOfAllPixels(singleLabel);
	sum = getResult("Sum", nResults() - 1);
	
	Ext.CLIJ2_create2D("white", width, height, 8);
	Ext.CLIJ2_set("white", 1);
	
	Ext.CLIJ2_maskLabel("white", labelmap, singleLabel, i + 1);
	Ext.CLIJ2_countNonZeroPixels(singleLabel);
	count = getResult("CountNonZero", nResults() - 1);
	
	mean = sum / count;
	IJ.log("Object " + i + ": pixelcount: " + count + " mean " + mean );
}

// show result
Ext.CLIJ2_pull(mask);

Ext.CLIJ2_pull(singleLabel);



