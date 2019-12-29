// CLIJ example macro: motionCorrection,ijm
//
// This macro shows how to do motion correction 
// image stack where slices might be shifted to 
// each other in the GPU.
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------
run("Close All");

// define move to correct
file = "https://github.com/clij/clij-docs/raw/master/src/main/resources/motion_correction_Drosophila_DSmanila1.tif";
//file = "C:/structure/code/clij-docs/src/main/resources/motion_correction_Drosophila_DSmanila1.tif";

// define identifiers for intermediate results in the GPU
inputStack = "inputStack";
slice = "slice";
shifted = "shifted";
binary = "binary";

// define a threshold to differentiate object and background
threshold = 50;

// open image
open(file);
rename(inputStack);
run("32-bit");

// collect info about it
getDimensions(width, height, channels, slices, frames);


// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();


Ext.CLIJx_push(inputStack);

formerX = 0;
formerY = 0;

// process all slices; only the first stays where it is
for (z = 0; z  < slices; z++) {
	IJ.log("z: " + z);
	
	Ext.CLIJx_copySlice(inputStack, slice, z);

	// determine center of mass
	Ext.CLIJx_threshold(slice, binary, threshold);
	Ext.CLIJx_centerOfMass(binary);
	x = getResult("MassX", nResults() - 1);
	y = getResult("MassY", nResults() - 1);

	if (z > 0) {
		
		// determine shift
		deltaX = x - formerX;
		deltaY = y - formerY;

		// apply translation transformation
		Ext.CLIJx_affineTransform2D(slice, shifted, "translatex=" + deltaX + " translatey=" + deltaY);

		// copy result back
		Ext.CLIJx_copySlice(shifted, inputStack, z);
	
	} else {
		formerX = x;
		formerY = y;
	}
}

// show result
Ext.CLIJx_pull(inputStack);
rename("outputStack");
