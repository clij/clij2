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


from net.haesleinhuepf.clij.coremem.enums import NativeTypeEnum

from net.haesleinhuepf.clijx import CLIJx;

def main ():

	cl_devices = ["1070", "HD", "CPU"];

	for device_name in cl_devices:

		# Init GPU
		clijx = CLIJx.getInstance(device_name);
		clijx.setKeepReferences(True);
		clijx.clear();
	
		types = [NativeTypeEnum.Float, NativeTypeEnum.UnsignedShort, NativeTypeEnum.UnsignedByte];
	
		for aType in types:
			print("Testing " + clijx.getGPUName() + " OpenCL Version " + str(clijx.getOpenCLVersion()) + " with images of type " + str(aType));
			input1 = clijx.create([1, 1, 1], aType);
			input2 = clijx.create([1, 1, 1], aType);
			output = clijx.create([1, 1, 1], aType);
			testMultiplyImages(clijx, input1, input2, output, 100, 2);
			testMultiplyImages(clijx, input1, input2, output, 200, 2);
			testMultiplyImages(clijx, input1, input2, output, 20000, 2);
			testMultiplyImages(clijx, input1, input2, output, 40000, 2);
			clijx.clear();
	
	

	

def testMultiplyImages(clijx, input1, input2, output, val1, val2):
	clijx.set(input1, val1);
	clijx.set(input2, val2);
	
	clijx.multiplyImages(input1, input2, output);
	
	maximum = clijx.maximumOfAllPixels(output);

	print(str(val1) + " * " + str(val2) + " = " + str(maximum));

main();
print("bye");


