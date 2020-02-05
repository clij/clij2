# multiply_images_test.py
#
# This script demonstrates multiplication of
# images and overflow handling with unsigned 
# short (16-bit) images
#
# In order to make it work in Fiji, you need
# to activate the clij and clij2 update sites.
#
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         November 2019
#########################################


from net.haesleinhuepf.clij2 import CLIJ2;

def main ():

	cl_devices = ["1070", "HD", "CPU"];

	for device_name in cl_devices:

		# Init GPU
		clij2 = CLIJ2.getInstance(device_name);
		clij2.setKeepReferences(True);
		clij2.clear();
	
		types = [clij2.Float, clij2.UnsignedShort, clij2.UnsignedByte];
	
		for aType in types:
			print("Testing " + clij2.getGPUName() + " OpenCL Version " + str(clij2.getOpenCLVersion()) + " with images of type " + str(aType));
			input1 = clij2.create([1, 1, 1], aType);
			input2 = clij2.create([1, 1, 1], aType);
			output = clij2.create([1, 1, 1], aType);
			testMultiplyImages(clij2, input1, input2, output, 100, 2);
			testMultiplyImages(clij2, input1, input2, output, 200, 2);
			testMultiplyImages(clij2, input1, input2, output, 20000, 2);
			testMultiplyImages(clij2, input1, input2, output, 40000, 2);
			clij2.clear();
	
	

	

def testMultiplyImages(clij2, input1, input2, output, val1, val2):
	clij2.set(input1, val1);
	clij2.set(input2, val2);
	
	clij2.multiplyImages(input1, input2, output);
	
	maximum = clij2.maximumOfAllPixels(output);

	print(str(val1) + " * " + str(val2) + " = " + str(maximum));

main();
print("bye");


