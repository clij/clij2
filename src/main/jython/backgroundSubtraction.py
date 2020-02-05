# backgroundSubtraction.py
# ========================
#
# Apply background subtraction to an image on the GPU
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         October 2019
#########################################

from ij import IJ;
from net.haesleinhuepf.clij2 import CLIJ2;


IJ.run("Close All");

# load example image
imp = IJ.openImage("http://imagej.nih.gov/ij/images/t1-head.zip");
# IJ.run(imp, "32-bit", "");
imp.show();

# init GPU
clij2 = CLIJ2.getInstance();

# push image to GPU
input = clij2.push(imp);

# allocated background image of same size and type as input
background = clij2.create(input);

# reserve memory for output, same size and type
output = clij2.create(input);

# get a background image by Gaussian blurring
clij2.gaussianBlur(input, background, 10, 10, 1);

# subtract background from original image
clij2.subtractImages(input, background, output);

# show result
clij2.pull(output).show();
IJ.setMinAndMax(0, 255);

# clean up
clij2.clear();
