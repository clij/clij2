# rotateOverwriteOriginal.py
# ==========================
#
# Rotate an image on the GPU
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         October 2019
#########################################

from ij import IJ;
from net.haesleinhuepf.clij2 import CLIJ2;
from net.haesleinhuepf.clij.clearcl import ClearCLImage

from net.imglib2.realtransform import AffineTransform2D;
from java.lang import Math;
from ij import ImagePlus;


IJ.run("Close All");

# load example image
# imp = IJ.openImage("http://wsr.imagej.net/images/blobs.gif");
imp = IJ.openImage("c:/structure/data/blobs.tif");
IJ.run(imp, "32-bit", "");
imp.show();

# init GPU
clij2 = CLIJ2.getInstance();

# push image to GPU; we're using images instead of buffers 
# because they suppport interpolation
input = clij2.convert(imp, ClearCLImage);

# reserve memory for output, same size and type as input
temp = clij2.create(input);
#[input.getWidth(), input.getHeight()], input.getNativeType());

# reserve memory for the resulting video of the rotating input
result = clij2.create([input.getWidth(), input.getHeight(), 360], input.getChannelDataType());

for i in range(0, 360):
	at = AffineTransform2D();
	at.translate(-input.getWidth() / 2, -input.getHeight() / 2);
	at.rotate(i / 180.0 * Math.PI);
	at.translate(input.getWidth() / 2, input.getHeight() / 2);
	
	# Execute operation on GPU
	clij2.affineTransform2D(input, temp, at);

	# never overwrite the original with the rotated image!
	# this is just an academic example to show what can go wrong
	clij2.copy(temp, input);

	# copy the resulting rotated image in the right plane in the
	# final video stack
	clij2.copySlice(temp, result, i);

# show result
clij2.convert(result, ImagePlus).show();
IJ.setMinAndMax(0, 255);

# clean up
clij2.clear();
