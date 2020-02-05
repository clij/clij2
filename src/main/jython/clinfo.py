# clinfo.py
# ===================
#
# Output all information about found GPUs
# (or rather: all found OpenCL devices)
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         October 2019
#########################################

from ij import IJ;
from net.haesleinhuepf.clij2 import CLIJ2;

IJ.log(CLIJ2.clinfo());
