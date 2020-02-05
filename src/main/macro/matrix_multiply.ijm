// CLIJ example macro: labeling.ijm
//
// This macro shows how to do matrix 
// multiplication in the GPU
//
// Author: Robert Haase
//         September 2019
// ---------------------------------------------


// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push a vector and a matrix to the GPU
A = newArray(1, 2, 3);
Ext.CLIJx_pushArray("A", A, 3, 1, 1);
B = newArray(1, 2, 3);
Ext.CLIJx_pushArray("B", B, 1, 3, 1);
// matrix multiplication
Ext.CLIJx_multiplyMatrix("A", "B", "C");
// show result in table
run("Clear Results");
Ext.CLIJx_image2DToResultsTable("C");

Ext.CLIJx_release("C");
// matrix multiplication
Ext.CLIJx_multiplyMatrix("B", "A", "C");

Ext.CLIJx_image2DToResultsTable("C");

// Ext.CLIJ_pull("c");










