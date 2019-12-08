# jaccardIndex.py
# ===============
#
# Compute the Jaccard index of two binary 
# images on the GPU.
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         December 2019
#########################################

from ij import IJ;
from net.haesleinhuepf.clijx import CLIJx;

IJ.run("Close All");

# init GPU
clijx = CLIJx.getInstance();

# init two binary images
image1 = clijx.create([100, 100, 10], clijx.UnsignedByte);
image2 = clijx.create([100, 100, 10], clijx.UnsignedByte);
temp = clijx.create([100, 100, 10], clijx.UnsignedByte);
clijx.set(image1, 0);
clijx.set(image2, 0);

# set two spheres
clijx.drawSphere(image1, 50, 50, 5, 20, 20, 5);
clijx.drawSphere(image2, 40, 40, 5, 20, 20, 5);

# visualise overlap
clijx.showRGB(image2, image1, image2, "Overlap (single plane)");

# compute and output overlap
jaccardIndex = clijx.jaccardIndex(image1, image2);
diceIndex = clijx.sorensenDiceCoefficient(image1, image2);
print("Jaccard index:" + str(jaccardIndex));
print("Dice index:" + str(diceIndex));

# cleanup by the end
clijx.clear();