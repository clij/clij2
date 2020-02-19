// virtual_ablation.py
// ===================
//
// This script demonstrates image warping
// on the GPU
//
// Author: Robert Haase, rhaase@mpi-cbg.de
//         February 2020
//
////////////////////////////////////////////////

run("Close All");

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();



//width = 512;
//height = 1024;

function cut(input, gapX, gapY, gapWidth, gapHeight, blurSigma, backgroundIntensity, shiftDistanceLeft, shiftDistanceRight, cutTimePoint, relaxCoefficent, localShift) {
    print("shiftDistance " + shiftDistance);
	print ("relax" + relaxCoefficent)
	Ext.CLIJx_getSize(input);
	width = getResult("Width", nResults() - 1);
	height = getResult("Height", nResults() - 1);
	depth = getResult("Depth", nResults() - 1);



    shiftX = "shiftX";
    shiftY = "shiftY";
    temp = "temp";
    temp2 = "temp2";
    temp3 = "temp3";
    slice = "slice";
    
    Ext.CLIJx_create2D(shiftX, width, height, 32);
    Ext.CLIJx_create2D(shiftY, width, height, 32);
    Ext.CLIJx_create2D(temp, width, height, 32);
    Ext.CLIJx_create2D(temp2, width, height, 32);
    Ext.CLIJx_create2D(temp3, width, height, 32);
    
    shiftLeft = 0;
    shiftRight = 0;
    
	for (z = cutTimePoint; z < depth; z++) {
		print(z + ": " + shiftDistanceLeft);
		
	    shiftDistanceLeft = relaxCoefficent * shiftDistanceLeft;
	    shiftDistanceRight = relaxCoefficent * shiftDistanceRight;

	    shiftLeft = shiftLeft + shiftDistanceLeft;
	    shiftRight = shiftRight + shiftDistanceRight;
		
		Ext.CLIJx_set(temp, 0);
		Ext.CLIJx_drawBox(temp, gapX - gapWidth, gapY, 0, gapWidth, gapHeight, 1, -shiftLeft);
		Ext.CLIJx_gaussianBlur2D(temp, temp3, blurSigma, blurSigma);
		
		Ext.CLIJx_set(temp, 0);
		Ext.CLIJx_drawBox(temp, gapX + gapWidth, gapY, 0, gapWidth, gapHeight, 1, shiftRight);
		Ext.CLIJx_gaussianBlur2D(temp, slice, blurSigma, blurSigma);
		
		Ext.CLIJx_addImages(temp3, slice, temp2);


		Ext.CLIJx_set(temp, 0);
		Ext.CLIJx_drawBox(temp, gapX, gapY, 0, gapWidth, gapHeight, 1, localShift);
		Ext.CLIJx_gaussianBlur2D(temp, slice, blurSigma, blurSigma);

		Ext.CLIJx_addImages(temp2, slice, temp3);
		
		Ext.CLIJx_invert(temp3, shiftX);
		
	    Ext.CLIJx_copySlice(input, slice, z);
	
	    Ext.CLIJx_drawBox(slice, gapX, gapY, 0, gapWidth, gapHeight, 1, backgroundIntensity);
	
	    Ext.CLIJx_applyVectorField2D(slice, shiftX, shiftY, temp);
	    Ext.CLIJx_copySlice(temp, input, z);

	}
	
    Ext.CLIJx_release(shiftX);
    Ext.CLIJx_release(shiftY);
    Ext.CLIJx_release(temp);
    Ext.CLIJx_release(temp2);
    Ext.CLIJx_release(temp3);
    Ext.CLIJx_release(slice);
}

//folder = "C:/structure/data/wolgast_1050_1249/";
folder = "C:/structure/data/finsterwalde_1275_1474/";
run("Image Sequence...", "open=" + folder + " number=100 sort");

input = getTitle();
Ext.CLIJ2_push(input);

run("Restore Selection");
waitForUser("Please draw a cut");
Roi.getBounds(gapX, gapY, gapWidth, gapHeight)
//gapX = 122;
//gapY = 250;
//gapWidth = 10;
//gapHeight = 200;

blurSigma = 25;
backgroundIntensity = 5;

cutTimePoint = 5;
shiftDistance = 10.0;
relaxCoefficient = 0.9;

localShift = 0;

cut(input, gapX, gapY, gapWidth, gapHeight, blurSigma, backgroundIntensity, shiftDistance, 0, cutTimePoint, relaxCoefficient, localShift);

Ext.CLIJx_pull(input);

getDimensions(width, height, channels, depth, frames);

run("Restore Selection");
for (z = 0; z < depth; z++) {
	Stack.setSlice(z + 1);
	run("Add Selection...");
}
run("Flatten", "stack");

