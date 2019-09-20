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
Ext.CLIJ_clear();

for (i = 0; i < 10000; i++) {
	IJ.log(i);

	input = "input" + i;
	
	rename(input);
	
	// push data to GPU
	Ext.CLIJ_push(input);
	
	// cleanup ImageJ
	//run("Close All");
	
	// create a mask using a fixed threshold
	Ext.CLIJ_automaticThreshold(input, mask, "Otsu");
	
	eroded = "eroded" + i;
	Ext.CLIJ_erodeBox(mask, eroded);
	
	dilated = "dilated" + i;
	Ext.CLIJ_dilateBox(eroded, dilated);
	
	subtracted = "subtracted" + i;
	Ext.CLIJ_subtractImages(mask, dilated, subtracted);
	
	downsampled = "downsampled" + i;
	Ext.CLIJ_downsample2D(subtracted, downsampled, 0.5, 0.5);
	
	// show result
	Ext.CLIJ_pullBinary(downsampled);
	Ext.CLIJ_reportMemory();
	//Ext.CLIJ_clear();
	
	close();
	//break;
}
