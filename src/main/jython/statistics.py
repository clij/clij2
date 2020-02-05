# statistics.py
# =============
#
# Derive pixel statistics of an image on the GPU
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         October 2019
#########################################

from ij import IJ;
from net.haesleinhuepf.clij2 import CLIJ2;


IJ.run("Close All");

# load example image
imp = IJ.openImage("http://wsr.imagej.net/images/blobs.gif");
# imp = IJ.openImage("c:/structure/data/blobs.tif");
imp.show();

# init GPU
clij2 = CLIJ2.getInstance();

# push image to GPU
input = clij2.push(imp);

meanIntensity = clij2.sumOfAllPixels(input) / input.getWidth() / input.getHeight();
IJ.log("Mean intensity of all pixels: " + str(meanIntensity));


# reserve memory for a mask and masked image, same size and type as input
mask = clij2.create(input);
masked = clij2.create(input);

# apply threshold method on GPU
clij2.automaticThreshold(input, mask, "Otsu");

# mask the image
clij.op().mask(input, mask, masked);

# determine mean intensity of masked area:
meanIntensity = clij2.sumPixels(masked) / input.getWidth() / input.getHeight();
IJ.log("Mean intensity of masked pixels: " + str(meanIntensity));

# clean up
clij2.clear();


