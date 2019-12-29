// CLIJ example macro: workflow.ijm
//
// A massive loop to check long-term stability
//
// Author: Robert Haase
//         September 2018
// ---------------------------------------------


// Get test data
run("Blobs (25K)");
//open("C:/structure/data/blobs.gif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();

mask = "Mask";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

for (i = 0; i < 10000; i++) {
	IJ.log(i);

	input = "input" + i;
	
	rename(input);
	
	// push data to GPU
	Ext.CLIJx_push(input);
	
	// cleanup ImageJ
	//run("Close All");
	
	// create a mask using a fixed threshold
	Ext.CLIJx_automaticThreshold(input, mask, "Otsu");
	
	eroded = "eroded" + i;
	Ext.CLIJx_erodeBox(mask, eroded);
	
	dilated = "dilated" + i;
	Ext.CLIJx_dilateBox(eroded, dilated);
	
	subtracted = "subtracted" + i;
	Ext.CLIJx_subtractImages(mask, dilated, subtracted);
	
	downsampled = "downsampled" + i;
	Ext.CLIJx_downsample2D(subtracted, downsampled, 0.5, 0.5);
	
	// show result
	Ext.CLIJx_pullBinary(downsampled);
	Ext.CLIJx_reportMemory();
	//Ext.CLIJ_clear();
	
	close();
	//break;
}
