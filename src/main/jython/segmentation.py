# segmentation.py
# ===============
#
# particle analysis on the GPU
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         October 2019
#########################################

from ij import IJ;
from net.haesleinhuepf.clijx import CLIJx;


IJ.run("Close All");

# load example image
imp = IJ.openImage("http://wsr.imagej.net/images/blobs.gif");
IJ.run(imp, "32-bit", "");
imp.show();

# init GPU
clijx = CLIJx.getInstance();

# push image to GPU
input = clijx.push(imp);

# reserve memory for output, same size and type as input
blurred = clijx.create(input);
thresholded = clijx.create(input);
labelled = clijx.create(input);
labelled_without_edges = clijx.create(input);

# blur, threshold and label the image
clijx.blur(input, blurred, 5, 5, 0);
clijx.automaticThreshold(blurred, thresholded, "Otsu");
clijx.connectedComponentsLabeling(thresholded, labelled);
clijx.excludeLabelsOnEdges(labelled, labelled_without_edges);

# show result
clijx.show(labelled_without_edges, "labelled_without_edges");
IJ.run("glasbey");

# clean up
input.close();
blurred.close();
thresholded.close();
labelled.close();



