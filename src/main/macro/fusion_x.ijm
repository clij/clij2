// CLIJ example macro: fusion.ijm
//
// This macro shows how to fuse images acquired 
// at different focal planes on the GPU. This 
// technique is basically extended depth of 
// field.
//
// Author: Robert Haase
//         October 2019
// ---------------------------------------------

run("Close All");

// Get test data
open("C:/structure/code/clij-advanced-filters/src/test/resources/Haarlem_DZ_thumbnails_sb_text.gif");
run("32-bit");
//makeRectangle(90, 0, 336, 475);
makeRectangle(0, 31, 426, 481);
run("Duplicate...", "duplicate");

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();


for (i=-8;i<=8;i+=4) {
	edfZ = 10 + i;
	imageName = "slice" + edfZ;
	print("pushing " + imageName);
	rename(imageName);
	setSlice(edfZ);
	Ext.CLIJx_pushCurrentSlice(imageName);
}


Ext.CLIJx_reportMemory();

Ext.CLIJx_tenengradFusionOf5("slice2", 
"slice6", "slice10", 
"slice14", "slice18", "fused", 10, 10, 0);

Ext.CLIJx_pull("fused");


for (i=-8;i<=8;i+=4) {
	edfZ = 10 + i;
	imageName = "slice" + edfZ;
	Ext.CLIJx_pull(imageName);
}
