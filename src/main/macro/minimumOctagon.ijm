// CLIJ example macro: iterative_mean.ijm
//
// This macro shows how to blur an image in the GPU
// using an iterative mean filter.
//
// Author: Robert Haase
//         December 20189
// ---------------------------------------------
run("Close All");

// Get test data
run("T1 Head (2.4M, 16-bits)");
input = getTitle();
getDimensions(width, height, channels, slices, frames);

blurred = "Blurred";
mean_blurred = "meanBlurred";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push images to GPU
Ext.CLIJx_push(input);

// cleanup ImageJ
run("Close All");

// Blur in GPU
for (radius = 10; radius < 15; radius += 5) {
	time = getTime();
	Ext.CLIJx_minimum3DSphere(input, blurred, radius, radius, radius);
	IJ.log("min sphere took " + (getTime() - time ));
	
	time = getTime();
	Ext.CLIJx_minimum3DBox(input, blurred, radius, radius, radius);
	IJ.log("min box took " + (getTime() - time ));

	minimum = 1000000;
	min_iterations = 0;
	
	for (iterations = radius - 9; iterations <= radius + 2; iterations += 1) {
		Ext.CLIJx_minimumOctagon(input, mean_blurred, iterations);
		Ext.CLIJx_meanSquaredError(blurred, mean_blurred);
		setResult("Iterations", nResults() - 1, iterations);
		value = getResult("MSE", nResults() - 1);
		if (minimum > value) {
			minimum = value;
			min_iterations = iterations;
		}
		//break;
	}


	time = getTime();
	Ext.CLIJx_minimumOctagon(input, blurred, min_iterations);
	IJ.log("iterative min " + (getTime() - time ));

	IJ.log("r" + radius + " = i" + min_iterations);
	//break;
}


// Cleanup by the end
Ext.CLIJx_clear();
