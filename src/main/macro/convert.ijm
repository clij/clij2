// CLIJ example macro: convert,ijm
//
// This macro shows how to convert an image in the GPU.
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------

run("Close All");

cl_device = "";

run("Blobs (25K)");
source = getTitle();

run("CLIJ Macro Extensions", "cl_device=" + cl_device);
Ext.CLIJx_clear();

Ext.CLIJx_push(source);

Ext.CLIJx_convertFloat(source, "float");
Ext.CLIJx_pull("float");

Ext.CLIJx_convertUInt8("float", "uint8");
Ext.CLIJx_pull("uint8");

Ext.CLIJx_convertUInt16(source, "uint16");
Ext.CLIJx_pull("uint16");

