// eddEven.ijm
//
// This script demonstrates performance differences 
// when processing images in the GPU which are 
// odd/even in size.
//
// Thanks to Peter Haub for suggesting this example
//
// Author: Robert Haase
//         November 2019
// ---------------------------------------------
run("Close All");

// Get test data
run("T1 Head (2.4M, 16-bits)");
//run("Duplicate...", "use");

//open("C:/structure/data/t1-head.tif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();

minimum = "minimum";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push data to GPU
Ext.CLIJx_push(input);

// cleanup ImageJ
run("Close All");

cropped = "cropped";
Ext.CLIJ2_crop3D(input, cropped, 0, 0, 0, 100, 100, 100);

// reslice
for (i = 0; i < 10; i++) {
	time = getTime();
	Ext.CLIJ2_minimum3DSphere(cropped, minimum, 10, 10, 10);
	print("min even took " + (getTime() - time));
}

Ext.CLIJ2_release(cropped);
Ext.CLIJ2_crop3D(input, cropped, 0, 0, 0, 101, 101, 101);
for (i = 0; i < 10; i++) {
	time = getTime();
	Ext.CLIJ2_minimum3DSphere(cropped, minimum, 10, 10, 10);
	print("min odd-xyz took " + (getTime() - time));
}

Ext.CLIJ2_release(cropped);
Ext.CLIJ2_crop3D(input, cropped, 0, 0, 0, 101, 100, 100);
for (i = 0; i < 10; i++) {
	time = getTime();
	Ext.CLIJ2_minimum3DSphere(cropped, minimum, 10, 10, 10);
	print("min odd-x took " + (getTime() - time));
}


Ext.CLIJ2_release(cropped);
Ext.CLIJ2_crop3D(input, cropped, 0, 0, 0, 100, 101, 100);
for (i = 0; i < 10; i++) {
	time = getTime();
	Ext.CLIJ2_minimum3DSphere(cropped, minimum, 10, 10, 10);
	print("min odd-y took " + (getTime() - time));
}


Ext.CLIJ2_release(cropped);
Ext.CLIJ2_crop3D(input, cropped, 0, 0, 0, 100, 100, 101);
for (i = 0; i < 10; i++) {
	time = getTime();
	Ext.CLIJ2_minimum3DSphere(cropped, minimum, 10, 10, 10);
	print("min odd-z took " + (getTime() - time));
}
