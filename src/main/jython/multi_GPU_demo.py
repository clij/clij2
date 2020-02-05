# multi_GPU_demo.py
# ================
# 
# This Jython script demonstrates how to process images 
# on multiple GPUs (or: OpenCL devices) in parallel.
# 
# Author: Robert Haase, rhaase@mpi-cbg.de
#         August 2019
#
########################################################

from ij import IJ;

from java.lang import Thread;

# The Processor class extends Thread so that we can run it
# in parallel
class Processor(Thread):
	# the CLIJ instance doing the heavy work
	clij = None;
	# the image which should be processed
	image = None;
	# a flag that says some work is ongoing
	working = False;
	# a flag that says is work was done in the past
	finished = False;

	# Constructor
	def __init__(self, clij):
		self.clij = clij;

	# sets the image which should be processed
	def setImage(self, image):
		self.image = image;

	# the actual procedure. Run processor.start() to get started in parallel.
	def run(self):
		# set status flags and initialize
		self.finished = False;
		self.working = True;
		# print("" + str(self.clij) + " starts working...\n");
		
		clij = self.clij;

		# push the image to GPU memory
		input_image = clij.push(self.image);

		# allocate more memory on the GPU for temp and resul images
		temp_image = clij.create(input_image);
		backgroundSubtracted_image = clij.create(input_image);
		max_projection_image = clij.create([input_image.getWidth(), input_image.getHeight()], input_image.getNativeType());

		# perform a background-subtracted maximum projection
		clij.op().blur(input_image, temp_image, 5, 5, 1);
		clij.op().subtract(input_image, temp_image, backgroundSubtracted_image);
		clij.op().maximumZProjection(backgroundSubtracted_image, max_projection_image);

		# pull result back from GPU memory and show it
		result = clij.pull(max_projection_image);
		# result.show();
		# IJ.run("Enhance Contrast", "saturated=0.35");

		# clean up by the end
		input_image.close();
		temp_image.close();
		backgroundSubtracted_image.close();
		max_projection_image.close();

		# set status flags
		self.working = False;
		self.finished = True;

	def isWorking(self):
		return self.working;
		
	def isFinished(self):
		return self.finished;

	def getCLIJ(self):
		return self.clij;

#imp = IJ.openImage("C:/structure/data/2018-05-23-16-18-13-89-Florence_multisample/processed/tif/000116.raw.tif");
imp = IJ.openImage("https://bds.mpi-cbg.de/CLIJ_benchmarking_data/000461.raw.tif");

from net.haesleinhuepf.clij import CLIJ;
from net.haesleinhuepf.clij2 import CLIJ2;

# print out available OpenCL devices
print("Available devices:");
for name in CLIJ.getAvailableDeviceNames():
	print(name);

# initialize a hand full of processors
processors = []
for i in range(0, len(CLIJ.getAvailableDeviceNames())):
	processors.append(Processor(CLIJ2(CLIJ(i))));

from java.lang import System;
startTime = System.currentTimeMillis();

# loop until a given number of images was processed
processed_images = 0;
while(processed_images < 10):
	# go trough all processors and see if one is doing nothing
	for j in range(0, len(processors)):
		processor = processors[j];
		if(not processor.isWorking()):
			# found a sleeping processor!

			# was he done with something?
			if (processor.isFinished()):
				processed_images += 1;
				# update log
				IJ.log("\\Clear");
				IJ.log("Processed images: " + str(processed_images));
				
				# replace it with a new processor
				processor = Processor(processor.getCLIJ());
				processors[j] = processor;
				
			# Starting a processor
			processor.setImage(imp);
			processor.start();

	# wait a moment
	Thread.sleep(100);

print("Processing on " + str(len(CLIJ.getAvailableDeviceNames())) + " devices took " + str(System.currentTimeMillis() - startTime) + " ms");
