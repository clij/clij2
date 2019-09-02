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
Ext.CLIJ_clear();

// push a vector and a matrix to the GPU
A = newArray(1, 2, 3);
Ext.CLIJ_pushArray("A", A, 3, 1, 1);
B = newArray(1, 1, 2, 3, 4, 3);
Ext.CLIJ_pushArray("B", B, 2, 3, 1);
// matrix multiplication
Ext.CLIJ_multiplyMatrix("A", "B", "C");
// show result in table
run("Clear Results");
setResult("data", nResults(), "A");
Ext.CLIJ_image2DToResultsTable("A");
setResult("data", nResults(), "B");
Ext.CLIJ_image2DToResultsTable("B");
setResult("data", nResults(), "C");
Ext.CLIJ_image2DToResultsTable("C");

// Ext.CLIJ_pull("c");










