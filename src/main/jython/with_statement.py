# with_statement.py
# =================
#
# Using the with statement with clijx
#
# To try it out, activate the clij and 
# clij2 update sites in Fiji
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         December 2019
#########################################

from __future__ import with_statement;
from ij import IJ;
from net.haesleinhuepf.clijx import CLIJx;


IJ.run("Close All");

# load example image
imp = IJ.openImage("http://wsr.imagej.net/images/blobs.gif");
IJ.run(imp, "32-bit", "");
imp.show();

# init GPU
with CLIJx.getInstance() as clijx:
	# push image to GPU
	input = clijx.push(imp);
	
	# reserve memory for output, same size and type as input
	blurred = clijx.create(input);

	# blur, threshold and label the image
	clijx.blur(input, blurred, 5, 5, 0);

	# show result
	clijx.show(blurred, "blurred");
	
	#
	# By the end of this block, clijx will 
	# clean up memory automatically
	#

# check if clijx was clean up by the end of the block
IJ.log(CLIJx.getInstance().reportMemory());
