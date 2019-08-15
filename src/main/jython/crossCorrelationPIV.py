# crossCorrelationPIV.py
# ======================
#
# Particle image velocimetry (PIV) based on cross correlation. CLIJ does the PIV in all three dimensions individually.
# This allows fast analysis of optical flow with the drawback of not being super precised. It is assumed that CLIJ
# over estimates the flow a bit because of the dimension reduction.
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         April 2019
#########################################

from ij import IJ;
from ij.gui import NewImage;
from net.haesleinhuepf.clij import CLIJ;

# load example image
imp = IJ.openImage("c:/structure/code/clij-docs/src/main/resources/blobs.tif");
imp.show();
IJ.run(imp, "32-bit", "");

vfXImp = NewImage.createFloatImage("vf", imp.getWidth(), imp.getHeight(), 1, NewImage.FILL_BLACK);
for x in range(100, 120):
    for y in range(100, 120):
        vfXImp.getProcessor().setf(x, y, 2);

clij = CLIJ.getInstance();

# push images to GPU and create memory for vector field
input = clij.push(imp);
vfraw = clij.push(vfXImp);
vf = clij.create(input);
shifted = clij.create(input);

# make a smoothly changing warp
clij.op().blur(vfraw, vf, 5, 5);

# apply vector field
clij.op().applyVectorfield(input, vf, vf, shifted);

# analyse shift
vfXAnalysed = clij.create(input);
vfYAnalysed = clij.create(input);
clij.op().particleImageVelocimetry2D(input, shifted, vfXAnalysed, vfYAnalysed, 5);

# show analysed vector field
clij.show(vfXAnalysed, "vfXAnalysed");
clij.show(vfYAnalysed, "vfYAnalysed");

# clean up 
input.close();
vfraw.close();
vf.close();
vfXAnalysed.close();
vfYAnalysed.close();

shifted.close();
