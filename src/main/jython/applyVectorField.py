# applyVectorField.py
# ===================
#
# This script demonstrates how to apply a
# warping to an image on the GPU
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         October 2019
#########################################

from ij import IJ;
from net.haesleinhuepf.clij2 import CLIJ2;
from net.imglib2.realtransform import AffineTransform2D;

from ij.gui import NewImage;
from ij.gui import OvalRoi;

from java.lang import Math;


IJ.run("Close All");

# load/create example images
imp = IJ.openImage("http://wsr.imagej.net/images/blobs.gif");
IJ.run(imp, "32-bit", "");
imp.show();

shiftX = NewImage.createFloatImage("", imp.getWidth(), imp.getHeight(), 1, NewImage.FILL_BLACK);
shiftY = NewImage.createFloatImage("", imp.getWidth(), imp.getHeight(), 1, NewImage.FILL_BLACK);

# define shift some of the pixels in X
shiftX.setRoi(OvalRoi(20, 98, 72, 68));
IJ.run(shiftX, "Add...", "value=25");
IJ.run(shiftX, "Select None", "");
IJ.run(shiftX, "Gaussian Blur...", "sigma=15");

# init GPU
clij2 = CLIJ2.getInstance();

# push image to GPU
input = clij2.push(imp);

shiftXgpu = clij2.push(shiftX);
rotatedShiftXgpu = clij2.create(shiftXgpu);
shiftYgpu = clij2.push(shiftY);

temp = clij2.create(input);

# reserve memory for output
output = clij2.create([input.getWidth(), input.getHeight(), 36], input.getNativeType());

for i in range(0, 36):

	# change the shift from slice to slice
	at = AffineTransform2D();
	at.translate(-input.getWidth() / 2, -input.getHeight() / 2);
	at.rotate(i * 10.0 / 180.0 * Math.PI);
	at.translate(input.getWidth() / 2, input.getHeight() / 2);

	clij2.affineTransform2D(shiftXgpu, rotatedShiftXgpu, at);
	
	# apply transform
	clij2.applyVectorfield(input, rotatedShiftXgpu, shiftYgpu, temp);

	# put resulting 2D image in the right plane
	clij2.copySlice(temp, output, i);

# show result
clij2.pull(output).show();
IJ.setMinAndMax(0, 255);

clij2.clear();


