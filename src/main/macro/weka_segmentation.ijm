// CLIJ example macro: weka_segmentation.ijm
//
// This macro shows how to train and apply weka models
// to feature stacks made by CLIJ.
//
// Author: Robert Haase
//         December 2019
// ---------------------------------------------

run("Close All");

// Get test data
run("Blobs (25K)");
run("32-bit");
original = "original";
rename(original);
getDimensions(width, height, channels, slices, frames)

// generate partial segmentation
partialGroundTruth = "partialGroundTruth";
newImage(partialGroundTruth, "32-bit black", width, height, slices);
makeRectangle(21,51,17,13);
run("Add...", "value=2");
makeRectangle(101,37,20,16);
run("Add...", "value=1");

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// push images to GPU
Ext.CLIJ_push(original);
Ext.CLIJ_push(partialGroundTruth);

// cleanup imagej
run("Close All");

featureStack = "featureStack";
Ext.CLIJ_create3D(featureStack, width, height, 11, 32);

featureCount = 0;

Ext.CLIJ_copySlice(original, featureStack, featureCount);
featureCount ++;

for (i = 0; i < 5; i++) {
    sigma = (i + 1);
    temp = "temp";
    Ext.CLIJ_blur2D(original, temp, sigma, sigma);

	temp2 = "temp2";
    Ext.CLIJx_sobel(temp, temp2);

    Ext.CLIJ_copySlice(temp, featureStack, featureCount);
    featureCount ++;
    Ext.CLIJ_copySlice(temp2, featureStack, featureCount);
    featureCount ++;

}

Ext.CLIJ_pull(original);
Ext.CLIJ_pull(partialGroundTruth);
Ext.CLIJ_pull(featureStack);

Ext.CLIJx_trainWekaModel(featureStack, partialGroundTruth, "test.model");

result = "result";
Ext.CLIJx_applyWekaModel(featureStack, result, "test.model");

Ext.CLIJ_pull(result);







Ext.CLIJ_clear();