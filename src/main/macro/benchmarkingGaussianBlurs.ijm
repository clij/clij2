// CLIJ example macro: benchmarking.ijm
//
// This macro shows how to measure performance of GPU and CPU based ImageJ macro code.
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------

// Get test data
run("T1 Head (2.4M, 16-bits)");
//open("C:/structure/teaching/neubias_ts13/Nantes_000500.tif");

input = getTitle();
getDimensions(width, height, channels, slices, frames);

blurred = "Blurred";

radius = 10;

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// filter in CPU
for (i = 1; i <= 10; i++) {
	time = getTime();
	run("Gaussian Blur 3D...", "x=" + radius + " y=" + radius + " z=" + radius + "");
	print("CPU Gaussian Blur filter no " + i + " took " + (getTime() - time) + " msec");
}

// push images to GPU
time = getTime();
Ext.CLIJ_push(input);
print("Pushing one image to the GPU took " + (getTime() - time) + " msec");

// cleanup ImageJ
run("Close All");


// filter in GPU
for (i = 1; i <= 10; i++) {
	time = getTime();
	Ext.CLIJ_blur3D(input, blurred, radius, radius, radius);
	print("GPU Gaussian blur clij1 filter no " + i + " took " + (getTime() - time) + " msec");
}


// filter in GPU
for (i = 1; i <= 10; i++) {
	time = getTime();
	Ext.CLIJ_blur3D(input, blurred, radius, radius, radius);
	print("GPU Gaussian blur clij1 with clear filter no " + i + " took " + (getTime() - time) + " msec");
	Ext.CLIJ_release(blurred);
}

// filter in GPU
for (i = 1; i <= 10; i++) {
	time = getTime();
	Ext.CLIJx_blurBuffers3D(input, blurred, radius, radius, radius);
	print("GPU Gaussian blur buffers filter no " + i + " took " + (getTime() - time) + " msec");
}

// filter in GPU
for (i = 1; i <= 10; i++) {
	time = getTime();
	Ext.CLIJx_blurImages3D(input, blurred, radius, radius, radius);
	print("GPU Gaussian blur images filter no " + i + " took " + (getTime() - time) + " msec");
}

// filter in GPU
for (i = 1; i <= 10; i++) {
	time = getTime();
	Ext.CLIJx_blurInplace3D(input, radius, radius, radius);
	print("GPU Gaussian blur inplace filter no " + i + " took " + (getTime() - time) + " msec");
}


// Get results back from GPU
time = getTime();
Ext.CLIJ_pull(blurred);

print("Pulling one image from the GPU took " + (getTime() - time) + " msec");

// Cleanup GPU 
Ext.CLIJ_clear();
