# interactiveSphereProjections.py
# ===========================
#
# This script demonstrates a spot detection workflow
# which can be tuned via a dialog giving results in
# instantly.
#
# The used data can be downloaded from:
# https://bds.mpi-cbg.de/CLIJ_benchmarking_data/
#
# Author: Robert Haase, rhaase@mpi-cbg.de
#         November 2019
#########################################

# Configuration / default values ; example data can be downloaded from here: https://bds.mpi-cbg.de/CLIJ_benchmarking_data/
data_folder = "C:/structure/data/2018-05-23-16-18-13-89-Florence_multisample/processed/tif/";
number_of_images = 451;
number_of_z_planes = 121;

# the zoom factor allows us to fit the result on the screen
# and to tune that it's running smoothly depending on 
# which GPU is used
zoom = 1.5; 


# Noise / background removal
formerDoNoiseAndBackgroundRemoval = True;
formerSigma1 = 10;
formerSigma2 = 30;

# Rigid transform
formerDoRigidTransform = True;
formerTranslationX = 30;
formerTranslationY = 0;
formerTranslationZ = 0;

formerRotationX = 0;
formerRotationY = 0;
formerRotationZ = 0;

# Spot detection
formerDoSpotDetection = False;
formerTolerance = 5;
formerThreshold = 0;


# --------------------------------------------------------
# Do not change anythign below

from ij import IJ;
from ij import ImageStack;
from ij import ImagePlus;
from ij.plugin import RGBStackMerge
from ij.gui import NewImage
from ij.plugin import CompositeConverter;
from ij.plugin import HyperStackConverter;
from ij.plugin import RGBStackConverter;
from ij import CompositeImage;
from ij.plugin import Duplicator

from net.haesleinhuepf.clijx import CLIJx;

from java.lang import Thread;
from java.lang import Math;
from java.lang import System;

from fiji.util.gui import GenericDialogPlus;

from net.imglib2.realtransform import AffineTransform3D;


from ij.plugin.filter import MaximumFinder;

IJ.run("Close All");
# load single image
#imp = IJ.openImage("C:/structure/data/florence/000300.raw.tif");
#IJ.run(imp, "32-bit", "");
#imp.show();

# load sequence
IJ.run("Image Sequence...", "open=[" + data_folder + "] sort use");
IJ.run("Stack to Hyperstack...", "order=xyczt(default) channels=1 slices=" + str(number_of_z_planes) + " frames=" + str(number_of_images) + " display=Color");
imp = IJ.getImage();

# init GPU
clijx = CLIJx.getInstance("630");

# configure initial scaling step
calib = imp.getCalibration();          
scaleX = calib.pixelWidth / calib.pixelDepth * zoom;
scaleY = calib.pixelHeight / calib.pixelDepth * zoom;
scaleZ = 1.0 * zoom;

# initialize state
input = None;
formerT = None;
resultCylinderMaxProjection = None;
resultMaxProjection = None;
spots = None;
circles = None;
blobs = None;

# build up user interface
gdp = GenericDialogPlus("Spot detection workflow");
gdp.addMessage("Noise and background subtraction (DoG)");
gdp.addCheckbox("Do noise and background subtraction ", formerDoNoiseAndBackgroundRemoval);
gdp.addSlider("Sigma 1 (in 0.1 pixel)", 0, 100, formerSigma1);
gdp.addSlider("Sigma 2 (in 0.1 pixel)", 0, 100, formerSigma2);
gdp.addMessage("Rigid transform");
gdp.addCheckbox("Do rigid transformation", formerDoRigidTransform);
gdp.addSlider("Translation X (in pixel)", -100, 100, formerTranslationX);
gdp.addSlider("Translation Y (in pixel)", -100, 100, formerTranslationY);
gdp.addSlider("Translation Z (in pixel)", -100, 100, formerTranslationZ);
gdp.addSlider("Rotation X (in degrees)", -180, 180, formerRotationX);
gdp.addSlider("Rotation Y (in degrees)", -180, 180, formerRotationY);
gdp.addSlider("Rotation Z (in degrees)", -180, 180, formerRotationZ);
gdp.addMessage("Spot detection")
gdp.addCheckbox("Do spot detection", formerDoSpotDetection);
gdp.addSlider("Tolerance", 0, 100, formerTolerance);
gdp.addSlider("Threshold", 0, 100, formerThreshold);
gdp.setModal(False);
gdp.showDialog();

doNoiseAndBackgroundRemovalCheckbox = gdp.getCheckboxes().get(0);
sigma1Slider = gdp.getSliders().get(0);
sigma2Slider = gdp.getSliders().get(1);

doRigidTransformCheckbox = gdp.getCheckboxes().get(1);
translationXSlider = gdp.getSliders().get(2);
translationYSlider = gdp.getSliders().get(3);
translationZSlider = gdp.getSliders().get(4);

rotationXSlider = gdp.getSliders().get(5);
rotationYSlider = gdp.getSliders().get(6);
rotationZSlider = gdp.getSliders().get(7);

doSpotDetectionCheckbox = gdp.getCheckboxes().get(2);
toleranceSlider = gdp.getSliders().get(8);
thresholdSlider = gdp.getSliders().get(9);

# loop until user closed the dialog
stillValid = False;
while ((not gdp.wasCanceled()) and not (gdp.wasOKed())):

	# reserve memory for input and output images
	if (input is None):
		input = clijx.create([
			(long)(imp.getWidth() * scaleX), 
			(long)(imp.getHeight() * scaleY), 
			(long)(imp.getNSlices() * scaleZ)], clijx.Float);
				
		temp1 = clijx.create(input);
		temp2 = clijx.create(input);
		dog = clijx.create(input);
		transformed = clijx.create(input);
		reslicedFromTop = clijx.create([input.getWidth(), input.getDepth(), input.getHeight()], input.getNativeType());
		cylinderProjection = clijx.create([(long)(input.getWidth() * 0.75), input.getHeight(), 540], input.getNativeType());
		cylinderProjection_temp = clijx.create([cylinderProjection.getWidth() * 2, cylinderProjection.getHeight(), cylinderProjection.getDepth()], input.getNativeType());
		sphereProjection = clijx.create([cylinderProjection_temp.getWidth(), 540, cylinderProjection_temp.getDepth()], input.getNativeType());
		sphereProjectionTransformed = clijx.create([sphereProjection.getHeight(), sphereProjection.getDepth(), sphereProjection.getWidth()], input.getNativeType());
		sphereMaxProjection = clijx.create([sphereProjectionTransformed.getWidth(), sphereProjectionTransformed.getHeight() / 2], input.getNativeType());
		maxProjection = clijx.create([transformed.getWidth(), transformed.getHeight()], input.getNativeType());

	# read current values from dialog
	doNoiseAndBackgroundRemoval = doNoiseAndBackgroundRemovalCheckbox.getState();
	sigma1 = 0.1 * sigma1Slider.getValue();
	sigma2 = 0.1 * sigma2Slider.getValue();

	doRigidTransform = doRigidTransformCheckbox.getState();
	translationX = translationXSlider.getValue();
	translationY = translationYSlider.getValue();
	translationZ = translationZSlider.getValue();

	rotationX = rotationXSlider.getValue() * Math.PI / 180.0;
	rotationY = rotationYSlider.getValue() * Math.PI / 180.0;
	rotationZ = rotationZSlider.getValue() * Math.PI / 180.0;

	doSpotDetection = doSpotDetectionCheckbox.getState();
	tolerance = toleranceSlider.getValue();
	threshold = thresholdSlider.getValue();

	# check if something changed
	if (
		formerDoNoiseAndBackgroundRemoval == doNoiseAndBackgroundRemoval and
		sigma1 == formerSigma1 and 
		sigma2 == formerSigma2 and
		formerDoRigidTransform == doRigidTransform and 
		translationX == formerTranslationX and
		translationY == formerTranslationY and
		translationZ == formerTranslationZ and
		rotationX == formerRotationX and
		rotationY == formerRotationY and
		rotationZ == formerRotationZ and 
		formerT == imp.getFrame() and 
		formerTolerance == tolerance and 
		formerThreshold == threshold and 
		formerDoSpotDetection == doSpotDetection
	):
		# sleep some msec
		Thread.sleep(100);
		continue;

	# measure start time for benchmarking
	timeStamp = System.currentTimeMillis();
		
	if (formerT != imp.getFrame()):
		formerT = imp.getFrame();
		# push image to GPU
		pushed = clijx.pushCurrentZStack(imp);
		# scale it initially; depends on zoom factor and voxel size
		scaleTransform = AffineTransform3D();
		scaleTransform.scale(scaleX, scaleY, scaleZ);		
		clijx.affineTransform3D(pushed, input, scaleTransform);
		pushed.close();
		stillValid = False;

	# Noise/background removal
	if (formerDoNoiseAndBackgroundRemoval != doNoiseAndBackgroundRemoval or formerSigma1 != sigma1 or formerSigma2 != sigma2):	
		formerDoNoiseAndBackgroundRemoval = doNoiseAndBackgroundRemoval;
		formerSigma1 = sigma1;
		formerSigma2 = sigma2;	
		stillValid = False;

	if (not stillValid):	
		if (doNoiseAndBackgroundRemoval):
			clijx.blur(input, temp1, sigma1, sigma1);		
			clijx.blur(input, temp2, sigma2, sigma2);
			clijx.subtract(temp1, temp2, dog);
		else:
			clijx.copy(input, dog);

	# Rigid transform
	if (not (formerDoRigidTransform == doRigidTransform and 
		translationX == formerTranslationX and
		translationY == formerTranslationY and
		translationZ == formerTranslationZ and
		rotationX == formerRotationX and
		rotationY == formerRotationY and
		rotationZ == formerRotationZ)):
		stillValid = False;

	if (not stillValid):
		formerDoRigidTransform = doRigidTransform;
		formerTranslationX = translationX;
		formerTranslationY = translationY;
		formerTranslationZ = translationZ;
		
		formerRotationX = rotationX;
		formerRotationY = rotationY;
		formerRotationZ = rotationZ;

		if(doRigidTransform):
			at = AffineTransform3D();
			at.translate(-transformed.getWidth() / 2, -transformed.getHeight() / 2, -transformed.getDepth() / 2);
			at.translate(translationX, translationY, translationZ);
			at.rotate(0, rotationX);
			at.rotate(1, rotationY);
			at.rotate(2, rotationZ);
			at.translate(transformed.getWidth() / 2, transformed.getHeight() / 2, transformed.getDepth() / 2);
			
			# Execute operation on GPU
			clijx.affineTransform3D(dog, transformed, at);
		else:
			clijx.copy(dog, transformed);

		# Maximum Intensity Projection
		clijx.maximumZProjection(transformed, maxProjection);

		# Cylindrical MIP
		clijx.resliceTop(transformed, reslicedFromTop);
		clijx.resliceRadial(reslicedFromTop, cylinderProjection, 0.666);

		translation = AffineTransform3D();
		translation.translate(cylinderProjection.getWidth(), 0, 0);
		
		clijx.affineTransform3D(cylinderProjection, cylinderProjection_temp, translation);
		clijx.resliceRadial(cylinderProjection_temp, sphereProjection, 0.666);
		# clijx.show(sphereProjection, "sphereProjection");
		# break;
		
		clijx.resliceLeft(sphereProjection, sphereProjectionTransformed);
		clijx.maximumZProjection(sphereProjectionTransformed, sphereMaxProjection);

	# Spot detection
	if (formerTolerance != tolerance or formerThreshold != threshold or formerDoSpotDetection != doSpotDetection):
		formerTolerance = tolerance;
		formerThreshold = threshold;
		formerDoSpotDetection = doSpotDetection;
		stillValid = False;

	if (not stillValid):
		if (doSpotDetection):
			projectionImp = clijx.pull(sphereMaxProjection);
			binary = MaximumFinder().findMaxima(projectionImp.getProcessor(), tolerance, True, threshold, MaximumFinder.SINGLE_POINTS, False, False);
			projectionImp.setProcessor(binary); 
			#projectionImp.show();
			
			if (spots is not None):
				spots.close();
			spots = clijx.push(projectionImp);
			if (circles is None):
				circles = clijx.create(spots);
			if (blobs is None):
				blobs = clijx.create(spots);
	
			clijx.dilateBox(spots, blobs);
			clijx.dilateSphere(blobs, spots);
			clijx.subtract(spots, blobs, circles);		
		else:
			if (circles is None):
				circles = clijx.create(sphereMaxProjection);
			clijx.set(circles, 0);

	# Result visualisation
	if (not stillValid):
		impSphereProjection = clijx.pull(sphereMaxProjection);
		IJ.run(impSphereProjection, "Enhance Contrast", "saturated=0.3");
		impCircles = clijx.pull(circles);
		impCircles.setDisplayRange(0, 1);
		
		IJ.run(impSphereProjection, "8-bit", "");
		IJ.run(impCircles, "8-bit", "");

		stack = ImageStack(impSphereProjection.getWidth(), impSphereProjection.getHeight());
		stack.addSlice(impSphereProjection.getProcessor());
		stack.addSlice(impCircles.getProcessor());
		
		tempImp = ImagePlus("Cylinder Maximum Projection + spots", stack);
		tempImp = HyperStackConverter.toHyperStack(tempImp, 2, 1, 1);
		if (tempImp.getNChannels() == 2):
			tempImp.setC(1);
			IJ.run(tempImp, "Magenta", "");
			tempImp.setC(2);
			tempImp.setDisplayRange(0, 1);
			RGBStackConverter.convertToRGB(tempImp);
		
			if (resultCylinderMaxProjection is None):
				resultCylinderMaxProjection = tempImp;
			else:
				resultCylinderMaxProjection.setProcessor(tempImp.getProcessor());
		resultCylinderMaxProjection.show();
		resultCylinderMaxProjection.updateAndDraw();

		# MIP
		impMaxProjection = clijx.pull(maxProjection);
		IJ.run(impMaxProjection, "Enhance Contrast", "saturated=0.35");
		if (resultMaxProjection is None):
			resultMaxProjection = impMaxProjection;
			resultMaxProjection.setTitle("Maximum Z projection");
			resultMaxProjection.show();
		else:
			resultMaxProjection.setProcessor(impMaxProjection.getProcessor());
			resultMaxProjection.updateAndDraw();

	
	formerT = imp.getFrame();
	print("Time: " + str(System.currentTimeMillis() - timeStamp) + " ms");
	
	stillValid = True;
	print(clijx.reportMemory());

# close windows
if (resultMaxProjection is not None):
	resultMaxProjection.close();

if (resultCylinderMaxProjection is not None):
	resultCylinderMaxProjection.close();


# clean up
clijx.clear();