// CLIJ example macro: measure_area_per_label.ijm
//
// This macro shows how to apply an automatic 
// threshold method and connected components labeling
// to an image on the GPU to measure size of objects 
// in the image
//
// Author: Robert Haase
// September 2019
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
Ext.CLIJx_clear();

// push data to GPU
Ext.CLIJx_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJx_automaticThreshold(input, mask, "Otsu");

Ext.CLIJx_connectedComponentsLabeling(mask, labelmap);

// determine how many objects were found
Ext.CLIJx_maximumOfAllPixels(labelmap);
numberOfObjects = getResult("Max", nResults() - 1);

// go through all objects
for (i = 0; i < numberOfObjects; i++) {
	// get a mask for a single object
	Ext.CLIJx_labelToMask(labelmap, singleLabelMask, i + 1);

	// all pixels in a binary image are 1, 
	// thus the sum can be used to count them:
	Ext.CLIJx_sumOfAllPixels(singleLabelMask);
	num_of_pixels = getResult("Sum", nResults() - 1);

	// print out object number and size
	IJ.log("Object " + (i + 1) + ": area: " + num_of_pixels + " pixels");
}

// show resulting label map
Ext.CLIJx_pull(labelmap);



