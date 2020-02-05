// CLIJ example macro: standardDeviationZProjection.ijm
//
// This macro shows how standard devation 
// Z-projections can be done in the GPU and
// how different it is from the CPU.
//
// Author: Robert Haase
// June 2019
// ---------------------------------------------


run("Close All");
run("T1 Head (2.4M, 16-bits)");
run("32-bit");

// standard deviation Z-projection on the GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();
Ext.CLIJ2_push(getTitle());
Ext.CLIJ2_standardDeviationZProjection(getTitle, "CLIJ_standardDeviationZProjection_destination_t1-head.tif");
Ext.CLIJ2_pull("CLIJ_standardDeviationZProjection_destination_t1-head.tif");
Ext.CLIJ2_clear();

// standard deviation Z-projection on the CPU
selectWindow("t1-head.tif");
run("Z Project...", "projection=[Standard Deviation]");

// compare both results
imageCalculator("Subtract create 32-bit", "CLIJ_standardDeviationZProjection_destination_t1-head.tif","STD_t1-head.tif");
selectWindow("Result of CLIJ_standardDeviationZProjection_destination_t1-head.tif");
