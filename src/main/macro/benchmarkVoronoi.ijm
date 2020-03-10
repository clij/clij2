// CLIJ example macro: voronoi.ijm
//
// This macro shows how to apply an get a
// voronoi image of a binary image in the GPU.
//
// Author: Robert Haase
//         March 2020
// ---------------------------------------------


// Get test data
run("Blobs (25K)");
run("Scale...", "x=10 y=10 width=2560 height=2560 interpolation=Bilinear average create");

input = getTitle();

mask = "Mask";
voronoi_diagram = "voronoi_diagram";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push data to GPU
Ext.CLIJ2_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJ2_thresholdOtsu(input, mask);




// fill holes in the binary image
for (i = 0; i < 10; i++) {
	Ext.CLIJ2_pullBinary(mask);	
	time = getTime();
	run("Voronoi");
	print("Voronoi on CPU took " + (getTime() - time) + " msec");
	if (i < 9) {
		close();
	}
}


// voronoi
for (i = 0; i < 10; i++) {
	time = getTime();
	Ext.CLIJ2_voronoiOctagon(mask, voronoi_diagram);
	print("Voronoi on GPU took " + (getTime() - time) + " msec");
}


// show result
Ext.CLIJ2_pullBinary(mask);
Ext.CLIJ2_pullBinary(voronoi_diagram);
