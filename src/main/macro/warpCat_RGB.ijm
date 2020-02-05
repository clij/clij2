// This script demonstrates how to apply a vector field
// to an RGB image in order to transform it non-rigidly
//
// It uses a picture showing Pixel. 
// Yes, the cats name is Pixel.
//
// Make sure to activate the "CLIJ" update site before 
// running this macro in Fiji.
//
// Author: Robert Haase, rhaase@mpi-cbg.de
//         November 2019
//


run("Close All");

// get test image
open("https://github.com/clij/clij-docs/raw/master/src/main/resources/pixel_cat_rgb.jpg");
run("RGB Stack");
rename("catRGB");

getDimensions(width, height, channels, slices, frames)

// create two images describing local shift
newImage("shiftX", "32-bit black", width, height, 1);
newImage("shiftY", "32-bit black", width, height, 1);

// reserve memory for the result video
newImage("resultStack", "32-bit black", width, height, 36 * 3);


// shift some of the pixels in X
selectImage("shiftX");
makeOval(350, 660, 200, 200);
run("Add...", "value=50");
run("Select None");
run("Gaussian Blur...", "sigma=50");
run("Enhance Contrast", "saturated=0.35");

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_push("catRGB");
Ext.CLIJ2_push("shiftX");
Ext.CLIJ2_copy("shiftX", "shiftY");
Ext.CLIJ2_push("resultStack");

// create a 32-bit float image for processing individual channels
Ext.CLIJ2_create2D("cat", width, height, 32);

// clean up imagej
run("Close All");

for (i = 0; i < 36; i++) {
	// iterate over channels
	for (c = 0; c < 3; c++) {
		Ext.CLIJ2_copySlice("catRGB", "cat", c);
	
		// change the shift from slice to slice
		Ext.CLIJ2_affineTransform2D("shiftX", "rotatedShiftX", "center translateX=" + (i * 20) + " translateY=" + (i * -10) + " -center");
		Ext.CLIJ2_affineTransform2D("shiftY", "rotatedShiftY", "center translateX=" + (i * 20) + " translateY=" + (i * -10) + " -center");
		
		// apply transform
		Ext.CLIJ2_applyVectorField2D("cat", "rotatedShiftX", "rotatedShiftY", "transformed");
	
		// put resulting 2D image in the right plane
		Ext.CLIJ2_copySlice("transformed", "resultStack", i * 3 + c);
	}
	//break;
}


// get result back from GPU
Ext.CLIJ2_pull("resultStack");

// correct visualisation
run("Stack to Hyperstack...", "order=xyczt(default) channels=3 slices=1 frames=36 display=Composite");
run("Enhance Contrast", "saturated=0.35");
Stack.setChannel(1);
resetMinAndMax();
Stack.setChannel(2);
resetMinAndMax();
Stack.setChannel(3);
resetMinAndMax();

Ext.CLIJ2_clear();
