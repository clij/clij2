// CLIJ example macro: orthogonalMaximumProjections.ijm
//
// This macro shows how maximum-X, -Y and -Z projections can be created using the GPU.
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------
run("Close All");

// Get test data
run("T1 Head (2.4M, 16-bits)");
//open("C:/structure/data/t1-head.tif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();
downScalingFactorInXY = 0.666; // because the image has slice distance 1.5
downScalingFactorInZ = 1;

downscaled = "Downscaled";

maximumProjectionX = "Maximum projection in X";
maximumProjectionY = "Maximum projection in Y";
maximumProjectionZ = "Maximum projection in Z";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push data to GPU
Ext.CLIJx_push(input);

// cleanup ImageJ
run("Close All");

// process
Ext.CLIJx_downsample3D(input, downscaled, downScalingFactorInXY, downScalingFactorInXY, downScalingFactorInZ);

Ext.CLIJx_maximumXProjection(downscaled, maximumProjectionX);
Ext.CLIJx_maximumYProjection(downscaled, maximumProjectionY);
Ext.CLIJx_maximumZProjection(downscaled, maximumProjectionZ);

// show results
Ext.CLIJx_pull(maximumProjectionX);
Ext.CLIJx_pull(maximumProjectionY);
Ext.CLIJx_pull(maximumProjectionZ);
