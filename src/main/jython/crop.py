# crop.py
# =======
#
# Crop out a part of an image on the GPU
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

# reserve memory for output, with a given size and type as input
output = clij2.create([75, 75], input.getNativeType());

# crop
clij2.crop(input, output, 10, 10);

# show result
clij2.pull(output).show();

# clean up
clij2.clear();