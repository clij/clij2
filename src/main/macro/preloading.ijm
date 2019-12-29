// CLIJ example macro: preloading.ijm
//
// This macro demonstrates preloading the second 
// image while the first image is processed.
//
// Author: Robert Haase
//         August 2019
// ---------------------------------------------

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

imageFilenames = newArray(
	"https://bds.mpi-cbg.de/CLIJ_benchmarking_data/000300.raw.tif",
	"https://bds.mpi-cbg.de/CLIJ_benchmarking_data/000301.raw.tif",
	"https://bds.mpi-cbg.de/CLIJ_benchmarking_data/000302.raw.tif",
	"https://bds.mpi-cbg.de/CLIJ_benchmarking_data/000303.raw.tif",
	"https://bds.mpi-cbg.de/CLIJ_benchmarking_data/000304.raw.tif");

for (i = 0; i < lengthOf(imageFilenames); i++) {
	time = getTime();
	// loading image data
	open(imageFilenames[i]);
	rename("original");
	Ext.CLIJx_push("original");
	run("Close All");

	// some heavy processing
	Ext.CLIJx_blur3D("original", "processed", 15, 15, 15);
	Ext.CLIJx_meanOfAllPixels("processed");
	IJ.log("mean: " + getResult("Mean", nResults() - 1));
	IJ.log("opening and processing took: " + (getTime() - time) + " msec"); 
}

// cleanup
Ext.CLIJx_clear();

for (i = 0; i < lengthOf(imageFilenames); i++) {
	time = getTime();
	// pre-loading image data
	currentFilename = imageFilenames[i];
	nextFilename = imageFilenames[(i + 1) % lengthOf(imageFilenames)];
	Ext.CLIJx_preloadFromDisc("original", currentFilename, nextFilename, "");
	
	// some heavy processing
	Ext.CLIJx_blur3D("original", "processed", 15, 15, 15);
	Ext.CLIJx_meanOfAllPixels("processed");
	IJ.log("mean: " + getResult("Mean", nResults() - 1));
	IJ.log("preloading and processing took: " + (getTime() - time) + " msec"); 
}

// cleanup
Ext.CLIJx_clear();

