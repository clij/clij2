// CLIJ example macro: boundingBoxes.ijm
//
// This macro shows how to apply an automatic 
// threshold method, use connected components labeling
// and measure bounding boxes of object 
//
// Author: Robert Haase
// August 2019
// ---------------------------------------------

run("Close All");
if (roiManager("count") > 0) {
	roiManager("deselect");
	roiManager("delete");
}


// Get test data
run("Blobs (25K)");
//open("C:/structure/data/blobs.gif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();

mask = "mask";
labelmap = "labelmap";
binaryImage = "binaryImage";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push data to GPU
Ext.CLIJ2_push(input);

// create a mask using a fixed threshold
Ext.CLIJ2_automaticThreshold(input, mask, "Otsu");

Ext.CLIJ2_connectedComponentsLabeling(mask, labelmap);

Ext.CLIJ2_maximumOfAllPixels(labelmap);
number_of_objects = getResult("Max", nResults() - 1);

for (i = 0; i < number_of_objects; i++) {
	// cut label map into individual masks
	Ext.CLIJ2_labelToMask(labelmap, binaryImage, i + 1); // 0 is background, 1 is the first label
	// put bounding boxes in the ROI manager
	Ext.CLIJ2_boundingBox(binaryImage);
	x = getResult("BoundingBoxX", nResults() - 1);
	y = getResult("BoundingBoxY", nResults() - 1);
	w = getResult("BoundingBoxWidth", nResults() - 1);
	h = getResult("BoundingBoxHeight", nResults() - 1);
	makeRectangle(x, y, w, h);
	roiManager("add");
}

// show all
roiManager("show all with labels");



