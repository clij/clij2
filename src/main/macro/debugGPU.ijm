// CLIJ example macro: getsize.ijm
//
// This macro shows how to get the dimensions 
// of an image on the GPU
//
// Author: Robert Haase
//         September 2019
// ---------------------------------------------


// Get test data
run("Blobs (25K)");
input = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");

// get info from currently selected GPU
Ext.CLIJx_getGPUProperties();

setResult("GPUName", nResults(), "-----------------");

// get info from all GPUs
Ext.CLIJx_listAvailableGPUs();



