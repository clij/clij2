// CLIJ example macro: weka.ijm
//
// This macro shows how to use Weka Trainable 
// Segmentation using CLIJ.
//
// Author: Robert Haase
// January 2020
// ---------------------------------------------

scaleFactor = 2;

run("Close All");

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// -------------------------------------------------------------------
// Get test data
run("Blobs (25K)");
//open("C:/structure/data/blobs.tif");


run("32-bit"); // interplation works better with float images

getDimensions(width, height, channels, slices, frames);
run("Scale...", "x=" + scaleFactor + " y=" + scaleFactor + " width=" + width * scaleFactor + " height=" + height * scaleFactor + " interpolation=Bilinear average create");

// push images to GPU
original = "original";
rename(original);
Ext.CLIJ_push(original);

// -------------------------------------------------------------------
// generate some ground truth
ground_truth = "ground_truth";
getDimensions(width, height, channels, slices, frames)
newImage(ground_truth, "32-bit black", width, height, 1);
// true pixels
makeRectangle(21 * scaleFactor,51 * scaleFactor,17 * scaleFactor,13 * scaleFactor);
run("Add...", "value=2");
// false pixels
makeRectangle(101 * scaleFactor,37 * scaleFactor,20 * scaleFactor,16 * scaleFactor);
run("Add...", "value=1");

// edges
makeRectangle(176 * scaleFactor, 88 * scaleFactor, 12 * scaleFactor, 2 * scaleFactor);
run("Add...", "value=3");

run("Select None");
Ext.CLIJ_push(ground_truth);




// -------------------------------------------------------------------
// compute features on original image
feature_stack = "feature_stack";
feature_slice = "feature_slice";
number_of_features = 10;
Ext.CLIJ_create3D(feature_stack, width, height, number_of_features, 32);
Ext.CLIJ_create2D(feature_slice, width, height, 32);

feature = 0;
// 1. feature: original
Ext.CLIJ_copySlice(original, feature_stack, feature);
feature++;

// 2. feature: sobel
Ext.CLIJx_sobel(original, feature_slice);
Ext.CLIJ_copySlice(feature_slice, feature_stack, feature);
feature++;

// 3. feature: blurred with given sigma
sigma = 2;
while (feature < number_of_features) {  
	Ext.CLIJ_blur2D(original, feature_slice, sigma, sigma);
	sigma = sigma + 2;
	Ext.CLIJ_copySlice(feature_slice, feature_stack, feature);
	feature++;
}

Ext.CLIJ_pull(ground_truth);
Ext.CLIJ_pull(feature_stack);

// -------------------------------------------------------------------
// train classifier
Ext.CLIJx_trainWekaModel(feature_stack, ground_truth, "test4.model");

// apply classifier
result = "result";
time = getTime();
Ext.CLIJx_applyWekaModel(feature_stack, result, "test4.model");
print("Apply weka model took " + (getTime() - time) + " msec");

Ext.CLIJ_pull(result);
run("glasbey on dark");



result1 = "result1";

time = getTime();
Ext.CLIJx_applyOCLWekaModel(feature_stack, result1, "test4.model");
print("Apply ocl weka model took " + (getTime() - time) + " msec");


Ext.CLIJ_pull(result1);
run("glasbey on dark");

// clean up by the end
Ext.CLIJ_clear();
