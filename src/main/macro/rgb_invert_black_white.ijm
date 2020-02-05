// CLIJ example macro: rgb_invert_black_white.ijm
//
// This macro shows how replace black by white in 
// plots in the GPU.
//
// Author: Robert Haase
//         February 2020
// ---------------------------------------------
run("Close All");

// get example data
open("https://clij.github.io/clij-benchmarking/plotting_jmh/images/imagesize/clij_ij_comparison_BinaryAnd2D.png");
run("RGB Stack");
run("32-bit"); // we do it in 32 bit because we need negative values
run("Make Composite", "display=Composite");

input = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// send input to GPU
time = getTime();
Ext.CLIJ2_push(input);

// crop three 2D single channel images
R = "R";
Ext.CLIJ2_copySlice(input, R, 0);
G = "G";
Ext.CLIJ2_copySlice(input, G, 1);
B = "B";
Ext.CLIJ2_copySlice(input, B, 2);

// do the math Jan Eglinger suggested: https://forum.image.sc/t/invert-rgb-image-without-changing-colors/33571

function mixChannels(c1, c2) {
	temp1 = "temp1";
	temp2 = "temp2";

	// c1 + c2
	Ext.CLIJ2_addImages(c1, c2, temp1);
	// divide by 2
	Ext.CLIJ2_multiplyImageAndScalar(temp1, temp2, 0.5);
	// -
	Ext.CLIJ2_invert(temp2, temp1);
	// add 255
	Ext.CLIJ2_addImageAndScalar(temp1, temp2, 255);
	return temp2;
}

// -------------------------------------------------
// G + B
result = mixChannels(G, B);
Ext.CLIJ2_copySlice(result, input, 0);

// R + B
result = mixChannels(R, B);
Ext.CLIJ2_copySlice(result, input, 1);

// R + G
result = mixChannels(R, G);
Ext.CLIJ2_copySlice(result, input, 2);

// -------------------------------------------------
Ext.CLIJ2_pull(input);
print("The workflow execution took " + (getTime() - time) + " ms");
run("8-bit");
run("Make Composite", "display=Composite");
