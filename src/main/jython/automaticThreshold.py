# statistics.py
# =====================
#
# Derive some statistics of an image on the GPU
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         October 2019
#########################################

from ij import IJ;
from net.haesleinhuepf.clij2 import CLIJ2;


IJ.run("Close All");

# load example image
imp = IJ.openImage("http://wsr.imagej.net/images/blobs.gif");
imp.show();

# init GPU
clij2 = CLIJ2.getInstance();

# push image to GPU
input = clij2.push(imp);

# reserve memory for output, same size and type as input
output = clij2.create(input);

# apply threshold method on GPU
clij2.automaticThreshold(input, output, "Otsu");

# show result
clij2.pull(output).show();
IJ.setMinAndMax(0, 1);

# clean up
clij2.clear();



