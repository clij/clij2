run("Close All");

// Get test data
run("Blobs (25K)");
run("16-bit");
rename("input");
getDimensions(width, height, channels, slices, frames);

tiles = 10;

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

Ext.CLIJx_push("input");

Ext.CLIJx_create2D("output", width * tiles, height * tiles, 16);

for (x = 0; x < tiles; x++) {
	for (y = 0; y < tiles; y++) {
		Ext.CLIJx_paste2D("input", "output", width * x, height * y);
	}
}

Ext.CLIJx_pull("output");


