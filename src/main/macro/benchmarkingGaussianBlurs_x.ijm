// CLIJ example macro: benchmarkingGaussianBlurs.ijm
//
// This macro shows how to measure performance of different 
// ways to call Gaussian blur on OpenCL-devices.
//
// Author: Robert Haase
//         November 2019
// ---------------------------------------------

run("Close All");

// config
radius = 10;
num_repeats = 2;
minsize = 10;
maxsize = 210;
stepsize = 50;

defaultsize = 30;
minradius = 1;
maxradius = 21;
stepradius = 5;

// define image name keys
input = "input";
blurred = "Blurred";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
run("Clear Results");
Ext.CLIJ2_listAvailableGPUs();

availableGPUs = Table.getColumn("GPUName");

colors = newArray("blue", "magenta", "#00BB00", "#888800", "#BBBBBB");

for (g = 0; g < lengthOf(availableGPUs); g++) {
	// select and initialize GPU
	run("CLIJ Macro Extensions", "cl_device=" + availableGPUs[g]);

	
	sizes = newArray((maxsize - minsize) / stepsize + 1);
	measurements1 = newArray((maxsize - minsize) / stepsize + 1);
	measurements2 = newArray((maxsize - minsize) / stepsize + 1);
	measurements3 = newArray((maxsize - minsize) / stepsize + 1);
	measurements4 = newArray((maxsize - minsize) / stepsize + 1);
	measurements5 = newArray((maxsize - minsize) / stepsize + 1);

    index = 0;
    maxTime = 0;
	for (size = minsize; size <= maxsize; size += stepsize) {
		sizes[index] = size * 2; // in MB
		
		Ext.CLIJ2_clear();
		Ext.CLIJ2_create3D("input", 1024, 1024, size, 16);

		// filter in GPU
		for (i = 0; i <= num_repeats; i++) {
			time = getTime();
			Ext.CLIJ2_gaussianBlur3D(input, blurred, radius, radius, radius);
			deltaTime = (getTime() - time);
			if (i > 0) {
				measurements1[index] += deltaTime / num_repeats / 1000;
			}
			print("GPU Gaussian blur clij1 filter no " + i + " took " + deltaTime + " msec");
		}
		if (maxTime < measurements1[index]) {
			maxTime = measurements1[index];
		}
			
		// filter in GPU
		for (i = 0; i <= num_repeats; i++) {
			time = getTime();
			Ext.CLIJ2_gaussianBlur3D(input, blurred, radius, radius, radius);
			deltaTime = (getTime() - time);
			if (i > 0) {
				measurements2[index] += deltaTime / num_repeats / 1000;
			}
			Ext.CLIJ2_release(blurred);
			print("GPU Gaussian blur clij1 with clear filter no " + i + " took " + deltaTime + " ms");
		}
		if (maxTime < measurements2[index]) {
			maxTime = measurements2[index];
		}
		
		// filter in GPU
		for (i = 0; i <= num_repeats; i++) {
			time = getTime();
			Ext.CLIJx_blurBuffers3D(input, blurred, radius, radius, radius);
			deltaTime = (getTime() - time);
			if (i > 0) {
				measurements3[index] += deltaTime / num_repeats / 1000;
			}
			print("GPU Gaussian blur buffers filter no " + i + " took " + deltaTime + " ms");
		}
		if (maxTime < measurements3[index]) {
			maxTime = measurements3[index];
		}
		
		// filter in GPU
		for (i = 0; i <= num_repeats; i++) {
			time = getTime();
			Ext.CLIJx_blurImages3D(input, blurred, radius, radius, radius);
			deltaTime = (getTime() - time);
			if (i > 0) {
				measurements4[index] += deltaTime / num_repeats / 1000;
			}
			print("GPU Gaussian blur images filter no " + i + " took " + deltaTime + " ms");
		}
		if (maxTime < measurements4[index]) {
			maxTime = measurements4[index];
		}
		
		// filter in GPU
		for (i = 0; i <= num_repeats; i++) {
			time = getTime();
			Ext.CLIJx_blurInplace3D(input, radius, radius, radius);
			deltaTime = (getTime() - time);
			if (i > 0) {
				measurements5[index] += deltaTime / num_repeats / 1000;
			}
			print("GPU Gaussian blur inplace filter no " + i + " took " + deltaTime + " ms");
		}
		if (maxTime < measurements5[index]) {
			maxTime = measurements5[index];
		}
		index++;
	}

	Plot.create(availableGPUs[g] + " - image size", "Image size / MB", "Time / s", sizes, measurements1);
	Plot.setColor(colors[0]);
	Plot.add("line", sizes, measurements1);
	Plot.setColor(colors[1]);
	Plot.add("line", sizes, measurements2);
	Plot.setColor(colors[2]);
	Plot.add("line", sizes, measurements3);
	Plot.setColor(colors[3]);
	Plot.add("line", sizes, measurements4);
	Plot.setColor(colors[4]);
	Plot.add("line", sizes, measurements5);
	Plot.setLegend("clij1\tclij1 w. release\tclij2 buffers\nclij2 images\tclij2 inplace", "bottom-right");
	Plot.setLimits(0, maxsize, 0, maxTime);
	Plot.show();/**/

	///////////////////////////
	radii = newArray((maxradius - minradius) / stepradius + 1);
	measurements1 = newArray((maxradius - minradius) / stepradius + 1);
	measurements2 = newArray((maxradius - minradius) / stepradius + 1);
	measurements3 = newArray((maxradius - minradius) / stepradius + 1);
	measurements4 = newArray((maxradius - minradius) / stepradius + 1);
	measurements5 = newArray((maxradius - minradius) / stepradius + 1);

    index = 0;
    maxTime = 0;
	for (radius = minradius; radius <= maxradius; radius += stepradius) {
		size = defaultsize; // in MB
		radii[index] = radius;
		
		Ext.CLIJx_clear();
		Ext.CLIJx_create3D("input", 1024, 1024, size, 16);

		// filter in GPU
		for (i = 0; i <= num_repeats; i++) {
			time = getTime();
			Ext.CLIJx_blur3D(input, blurred, radius, radius, radius);
			deltaTime = (getTime() - time);
			if (i > 0) {
				measurements1[index] += deltaTime / num_repeats / 1000;
			}
			print("GPU Gaussian blur clij1 filter no " + i + " took " + deltaTime + " msec");
		}
		if (maxTime < measurements1[index]) {
			maxTime = measurements1[index];
		}
		
	
		// filter in GPU
		for (i = 0; i <= num_repeats; i++) {
			time = getTime();
			Ext.CLIJx_blur3D(input, blurred, radius, radius, radius);
			deltaTime = (getTime() - time);
			if (i > 0) {
				measurements2[index] += deltaTime / num_repeats / 1000;
			}
			Ext.CLIJx_release(blurred);
			print("GPU Gaussian blur clij1 with clear filter no " + i + " took " + deltaTime + " ms");
		}
		if (maxTime < measurements2[index]) {
			maxTime = measurements2[index];
		}
		
		// filter in GPU
		for (i = 0; i <= num_repeats; i++) {
			time = getTime();
			Ext.CLIJx_blurBuffers3D(input, blurred, radius, radius, radius);
			deltaTime = (getTime() - time);
			if (i > 0) {
				measurements3[index] += deltaTime / num_repeats / 1000;
			}
			print("GPU Gaussian blur buffers filter no " + i + " took " + deltaTime + " ms");
		}
		if (maxTime < measurements3[index]) {
			maxTime = measurements3[index];
		}
		
		// filter in GPU
		for (i = 0; i <= num_repeats; i++) {
			time = getTime();
			Ext.CLIJx_blurImages3D(input, blurred, radius, radius, radius);
			deltaTime = (getTime() - time);
			if (i > 0) {
				measurements4[index] += deltaTime / num_repeats / 1000;
			}
			print("GPU Gaussian blur images filter no " + i + " took " + deltaTime + " ms");
		}
		if (maxTime < measurements4[index]) {
			maxTime = measurements4[index];
		}
		
		// filter in GPU
		for (i = 0; i <= num_repeats; i++) {
			time = getTime();
			Ext.CLIJx_blurInplace3D(input, radius, radius, radius);
			deltaTime = (getTime() - time);
			if (i > 0) {
				measurements5[index] += deltaTime / num_repeats / 1000;
			}
			print("GPU Gaussian blur inplace filter no " + i + " took " + deltaTime + " ms");
		}
		if (maxTime < measurements5[index]) {
			maxTime = measurements5[index];
		}
		index++;
	}

	Plot.create(availableGPUs[g] + " - sigma", "Sigma", "Time / s", radii, measurements1);	
	Plot.setColor(colors[0]);
	Plot.add("line", radii, measurements1);
	Plot.setColor(colors[1]);
	Plot.add("line", radii, measurements2);
	Plot.setColor(colors[2]);
	Plot.add("line", radii, measurements3);
	Plot.setColor(colors[3]);
	Plot.add("line", radii, measurements4);
	Plot.setColor(colors[4]);
	Plot.add("line", radii, measurements5);
	Plot.setLegend("clij1\tclij1 w. release\tclij2 buffers\nclij2 images\tclij2 inplace", "bottom-right");
	Plot.setLimits(0, maxradius, 0, maxTime);
	Plot.show();
	
	break;
}
