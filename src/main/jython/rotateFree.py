# rotateFree.py
# =============
#
# Rotate an image on the GPU
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         October 2019
#########################################

from ij import IJ;
from net.haesleinhuepf.clij2 import CLIJ2;

from net.imglib2.realtransform import AffineTransform2D;
from java.lang import Math;


IJ.run("Close All");

# load example image
imp = IJ.openImage("http://wsr.imagej.net/images/blobs.gif");
# imp = IJ.openImage("c:/structure/data/blobs.tif");
imp.show();

# init GPU
clij2 = CLIJ2.getInstance();

# push image to GPU
input = clij2.push(imp);

# reserve memory for output, same size and type as input
temp = clij2.create(input);

# reserve memory for the resulting video of the rotating input
result = clij2.create([input.getWidth(), input.getHeight(), 36], input.getNativeType());

for i in range(0, 36):
	at = AffineTransform2D();
	at.translate(-input.getWidth() / 2, -input.getHeight() / 2);
	at.rotate(i * 10.0 / 180.0 * Math.PI);
	at.translate(input.getWidth() / 2, input.getHeight() / 2);
	
	# Execute operation on GPU
	clij2.affineTransform2D(input, temp, at);

	clij2.copySlice(temp, result, i);

# show result
clij2.pull(result).show();
IJ.setMinAndMax(0, 255);

# clean up
clij2.clear();
