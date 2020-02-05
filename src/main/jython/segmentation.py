# segmentation.py
# ===============
#
# particle analysis on the GPU
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         October 2019
#########################################

from ij import IJ;
from net.haesleinhuepf.clij2 import CLIJ2;


IJ.run("Close All");

# load example image
imp = IJ.openImage("http://wsr.imagej.net/images/blobs.gif");
IJ.run(imp, "32-bit", "");
imp.show();

# init GPU
clij2 = CLIJ2.getInstance();

# push image to GPU
input = clij2.push(imp);

# reserve memory for output, same size and type as input
blurred = clij2.create(input);
thresholded = clij2.create(input);
labelled = clij2.create(input);
labelled_without_edges = clij2.create(input);

# blur, threshold and label the image
clij2.blur(input, blurred, 5, 5, 0);
clij2.automaticThreshold(blurred, thresholded, "Otsu");
clij2.connectedComponentsLabeling(thresholded, labelled);
clij2.excludeLabelsOnEdges(labelled, labelled_without_edges);

# show result
clij2.show(labelled_without_edges, "labelled_without_edges");
IJ.run("glasbey");

# clean up
clij2.clear();