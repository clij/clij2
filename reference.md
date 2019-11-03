# CLIJx reference
This reference contains all methods currently available in CLIJx.

__Please note:__ CLIJx is under heavy construction. This list may change at any point.Methods marked with ' were available in CLIJ1.

* <a href="#absolute">absolute'</a>
* <a href="#addImageAndScalar">addImageAndScalar'</a>
* <a href="#addImagesWeighted">addImagesWeighted'</a>
* <a href="#addImages">addImages'</a>
* <a href="#affineTransform2D">affineTransform2D'</a>
* <a href="#affineTransform2D">affineTransform2D'</a>
* <a href="#affineTransform3D">affineTransform3D'</a>
* <a href="#affineTransform3D">affineTransform3D'</a>
* <a href="#affineTransform">affineTransform'</a>
* <a href="#affineTransform">affineTransform'</a>
* <a href="#applyVectorfield">applyVectorfield'</a>
* <a href="#applyVectorfield">applyVectorfield'</a>
* <a href="#argMaximumZProjection">argMaximumZProjection'</a>
* <a href="#automaticThreshold">automaticThreshold'</a>
* <a href="#automaticThreshold">automaticThreshold'</a>
* <a href="#averageDistanceOfClosestPoints">averageDistanceOfClosestPoints</a>
* <a href="#binaryAnd">binaryAnd'</a>
* <a href="#binaryEdgeDetection">binaryEdgeDetection</a>
* <a href="#binaryIntersection">binaryIntersection</a>
* <a href="#binaryNot">binaryNot'</a>
* <a href="#binaryOr">binaryOr'</a>
* <a href="#binarySubtract">binarySubtract</a>
* <a href="#binaryUnion">binaryUnion</a>
* <a href="#binaryXOr">binaryXOr'</a>
* <a href="#blurSliceBySlice">blurSliceBySlice'</a>
* <a href="#blur">blur'</a>
* <a href="#blur">blur'</a>
* <a href="#boundingBox">boundingBox</a>
* <a href="#centerOfMass">centerOfMass'</a>
* <a href="#connectedComponentsLabelingInplace">connectedComponentsLabelingInplace</a>
* <a href="#connectedComponentsLabeling">connectedComponentsLabeling</a>
* <a href="#convertToImageJBinary">convertToImageJBinary'</a>
* <a href="#copySlice">copySlice'</a>
* <a href="#copy">copy'</a>
* <a href="#countNonZeroPixelsLocallySliceBySlice">countNonZeroPixelsLocallySliceBySlice'</a>
* <a href="#countNonZeroPixelsLocally">countNonZeroPixelsLocally'</a>
* <a href="#countNonZeroPixels">countNonZeroPixels</a>
* <a href="#countNonZeroVoxelsLocally">countNonZeroVoxelsLocally'</a>
* <a href="#countTouchingNeighbors">countTouchingNeighbors</a>
* <a href="#crop">crop'</a>
* <a href="#crop">crop'</a>
* <a href="#crossCorrelation">crossCorrelation</a>
* <a href="#deformableRegistration2D">deformableRegistration2D</a>
* <a href="#detectLabelEdges">detectLabelEdges</a>
* <a href="#detectMaximaBox">detectMaximaBox'</a>
* <a href="#detectMaximaSliceBySliceBox">detectMaximaSliceBySliceBox'</a>
* <a href="#detectMinimaBox">detectMinimaBox'</a>
* <a href="#detectMinimaSliceBySliceBox">detectMinimaSliceBySliceBox'</a>
* <a href="#detectOptimaSliceBySlice">detectOptimaSliceBySlice'</a>
* <a href="#detectOptima">detectOptima'</a>
* <a href="#differenceOfGaussian">differenceOfGaussian</a>
* <a href="#differenceOfGaussian">differenceOfGaussian</a>
* <a href="#dilateBoxSliceBySlice">dilateBoxSliceBySlice'</a>
* <a href="#dilateBox">dilateBox'</a>
* <a href="#dilateSphereSliceBySlice">dilateSphereSliceBySlice'</a>
* <a href="#dilateSphere">dilateSphere'</a>
* <a href="#distanceMap">distanceMap</a>
* <a href="#divideImages">divideImages'</a>
* <a href="#downsampleSliceBySliceHalfMedian">downsampleSliceBySliceHalfMedian'</a>
* <a href="#downsample">downsample'</a>
* <a href="#downsample">downsample'</a>
* <a href="#drawBox">drawBox</a>
* <a href="#drawBox">drawBox</a>
* <a href="#drawLine">drawLine</a>
* <a href="#drawSphere">drawSphere</a>
* <a href="#drawSphere">drawSphere</a>
* <a href="#drawTwoValueLine">drawTwoValueLine</a>
* <a href="#equalConstant">equalConstant</a>
* <a href="#equal">equal</a>
* <a href="#erodeBoxSliceBySlice">erodeBoxSliceBySlice'</a>
* <a href="#erodeBox">erodeBox'</a>
* <a href="#erodeSphereSliceBySlice">erodeSphereSliceBySlice'</a>
* <a href="#erodeSphere">erodeSphere'</a>
* <a href="#excludeLabelsOnEdges">excludeLabelsOnEdges</a>
* <a href="#exponential">exponential</a>
* <a href="#extrema">extrema</a>
* <a href="#fillHistogram">fillHistogram'</a>
* <a href="#flip">flip'</a>
* <a href="#flip">flip'</a>
* <a href="#gaussJordan">gaussJordan</a>
* <a href="#generateDistanceMatrix">generateDistanceMatrix</a>
* <a href="#generateTouchMatrix">generateTouchMatrix</a>
* <a href="#getSize">getSize</a>
* <a href="#gradientX">gradientX'</a>
* <a href="#gradientY">gradientY'</a>
* <a href="#gradientZ">gradientZ'</a>
* <a href="#greaterConstant">greaterConstant</a>
* <a href="#greaterOrEqualConstant">greaterOrEqualConstant</a>
* <a href="#greaterOrEqual">greaterOrEqual</a>
* <a href="#greater">greater</a>
* <a href="#histogram">histogram'</a>
* <a href="#image2DToResultsTable">image2DToResultsTable</a>
* <a href="#invert">invert'</a>
* <a href="#jaccardIndex">jaccardIndex</a>
* <a href="#labelToMask">labelToMask</a>
* <a href="#laplace">laplace</a>
* <a href="#localExtremaBox">localExtremaBox</a>
* <a href="#localID">localID</a>
* <a href="#localPositiveMinimum">localPositiveMinimum</a>
* <a href="#localThreshold">localThreshold'</a>
* <a href="#logarithm">logarithm</a>
* <a href="#maskLabel">maskLabel</a>
* <a href="#maskStackWithPlane">maskStackWithPlane'</a>
* <a href="#mask">mask'</a>
* <a href="#matrixEqual">matrixEqual</a>
* <a href="#maximumBox">maximumBox'</a>
* <a href="#maximumIJ">maximumIJ'</a>
* <a href="#maximumImageAndScalar">maximumImageAndScalar'</a>
* <a href="#maximumImages">maximumImages'</a>
* <a href="#maximumOfAllPixels">maximumOfAllPixels'</a>
* <a href="#maximumOfMaskedPixels">maximumOfMaskedPixels</a>
* <a href="#maximumSliceBySliceSphere">maximumSliceBySliceSphere'</a>
* <a href="#maximumSphere">maximumSphere'</a>
* <a href="#maximumSphere">maximumSphere'</a>
* <a href="#maximumXYZProjection">maximumXYZProjection'</a>
* <a href="#maximumZProjection">maximumZProjection'</a>
* <a href="#meanBox">meanBox'</a>
* <a href="#meanClosestSpotDistances">meanClosestSpotDistances</a>
* <a href="#meanClosestSpotDistances">meanClosestSpotDistances</a>
* <a href="#meanIJ">meanIJ'</a>
* <a href="#meanOfMaskedPixels">meanOfMaskedPixels</a>
* <a href="#meanSliceBySliceSphere">meanSliceBySliceSphere'</a>
* <a href="#meanSphere">meanSphere'</a>
* <a href="#meanSphere">meanSphere'</a>
* <a href="#meanSquaredError">meanSquaredError</a>
* <a href="#meanZProjection">meanZProjection'</a>
* <a href="#medianBox">medianBox'</a>
* <a href="#medianBox">medianBox'</a>
* <a href="#medianSliceBySliceBox">medianSliceBySliceBox'</a>
* <a href="#medianSliceBySliceSphere">medianSliceBySliceSphere'</a>
* <a href="#medianSphere">medianSphere'</a>
* <a href="#medianSphere">medianSphere'</a>
* <a href="#minimumBox">minimumBox'</a>
* <a href="#minimumIJ">minimumIJ'</a>
* <a href="#minimumImageAndScalar">minimumImageAndScalar'</a>
* <a href="#minimumImages">minimumImages'</a>
* <a href="#minimumOfAllPixels">minimumOfAllPixels'</a>
* <a href="#minimumOfMaskedPixels">minimumOfMaskedPixels</a>
* <a href="#minimumSliceBySliceSphere">minimumSliceBySliceSphere'</a>
* <a href="#minimumSphere">minimumSphere'</a>
* <a href="#minimumSphere">minimumSphere'</a>
* <a href="#minimumZProjection">minimumZProjection'</a>
* <a href="#multiplyImageAndCoordinate">multiplyImageAndCoordinate'</a>
* <a href="#multiplyImageAndScalar">multiplyImageAndScalar'</a>
* <a href="#multiplyImages">multiplyImages'</a>
* <a href="#multiplyMatrix">multiplyMatrix</a>
* <a href="#multiplySliceBySliceWithScalars">multiplySliceBySliceWithScalars'</a>
* <a href="#multiplyStackWithPlane">multiplyStackWithPlane'</a>
* <a href="#nClosestPoints">nClosestPoints</a>
* <a href="#nonzeroMaximumDiamond">nonzeroMaximumDiamond</a>
* <a href="#nonzeroMaximumDiamond">nonzeroMaximumDiamond</a>
* <a href="#nonzeroMinimumDiamond">nonzeroMinimumDiamond</a>
* <a href="#nonzeroMinimumDiamond">nonzeroMinimumDiamond</a>
* <a href="#notEqualConstant">notEqualConstant</a>
* <a href="#notEqual">notEqual</a>
* <a href="#onlyzeroOverwriteMaximumBox">onlyzeroOverwriteMaximumBox</a>
* <a href="#onlyzeroOverwriteMaximumDiamond">onlyzeroOverwriteMaximumDiamond</a>
* <a href="#particleImageVelocimetry2D">particleImageVelocimetry2D</a>
* <a href="#particleImageVelocimetryTimelapse">particleImageVelocimetryTimelapse</a>
* <a href="#particleImageVelocimetry">particleImageVelocimetry</a>
* <a href="#paste">paste</a>
* <a href="#paste">paste</a>
* <a href="#powerImages">powerImages</a>
* <a href="#power">power'</a>
* <a href="#preloadFromDisc">preloadFromDisc</a>
* <a href="#presign">presign</a>
* <a href="#pullAsROI">pullAsROI</a>
* <a href="#radialProjection">radialProjection'</a>
* <a href="#readImageFromDisc">readImageFromDisc</a>
* <a href="#readRawImageFromDisc">readRawImageFromDisc</a>
* <a href="#readRawImageFromDisc">readRawImageFromDisc</a>
* <a href="#replaceIntensities">replaceIntensities</a>
* <a href="#replaceIntensity">replaceIntensity</a>
* <a href="#resliceBottom">resliceBottom'</a>
* <a href="#resliceLeft">resliceLeft'</a>
* <a href="#resliceRight">resliceRight'</a>
* <a href="#resliceTop">resliceTop'</a>
* <a href="#rotateLeft">rotateLeft'</a>
* <a href="#rotateRight">rotateRight'</a>
* <a href="#saveAsTIF">saveAsTIF</a>
* <a href="#setNonZeroPixelsToPixelIndex">setNonZeroPixelsToPixelIndex</a>
* <a href="#setWhereXequalsY">setWhereXequalsY</a>
* <a href="#set">set'</a>
* <a href="#shiftIntensitiesToCloseGaps">shiftIntensitiesToCloseGaps</a>
* <a href="#shortestDistances">shortestDistances</a>
* <a href="#smallerConstant">smallerConstant</a>
* <a href="#smallerOrEqualConstant">smallerOrEqualConstant</a>
* <a href="#smallerOrEqual">smallerOrEqual</a>
* <a href="#smaller">smaller</a>
* <a href="#sorensenDiceCoefficient">sorensenDiceCoefficient</a>
* <a href="#splitStack">splitStack'</a>
* <a href="#spotsToPointList">spotsToPointList</a>
* <a href="#stackToTiles">stackToTiles</a>
* <a href="#standardDeviationOfAllPixels">standardDeviationOfAllPixels</a>
* <a href="#standardDeviationOfAllPixels">standardDeviationOfAllPixels</a>
* <a href="#statisticsOfLabelledPixels">statisticsOfLabelledPixels</a>
* <a href="#statisticsOfLabelledPixels">statisticsOfLabelledPixels</a>
* <a href="#statisticsOfLabelledPixels">statisticsOfLabelledPixels</a>
* <a href="#statisticsOfLabelledPixels">statisticsOfLabelledPixels</a>
* <a href="#stopWatch">stopWatch</a>
* <a href="#subtractBackground">subtractBackground</a>
* <a href="#subtractBackground">subtractBackground</a>
* <a href="#subtractImages">subtractImages'</a>
* <a href="#subtract">subtract'</a>
* <a href="#sumPixelsSliceBySlice">sumPixelsSliceBySlice'</a>
* <a href="#sumPixels">sumPixels'</a>
* <a href="#sumZProjection">sumZProjection'</a>
* <a href="#threshold">threshold'</a>
* <a href="#topHatBox">topHatBox</a>
* <a href="#topHatSphere">topHatSphere</a>
* <a href="#touchMatrixToMesh">touchMatrixToMesh</a>
* <a href="#translationRegistration">translationRegistration</a>
* <a href="#translationRegistration">translationRegistration</a>
* <a href="#translationTimelapseRegistration">translationTimelapseRegistration</a>
* <a href="#transposeXY">transposeXY</a>
* <a href="#transposeXZ">transposeXZ</a>
* <a href="#transposeYZ">transposeYZ</a>
* <a href="#varianceOfAllPixels">varianceOfAllPixels</a>
* <a href="#varianceOfAllPixels">varianceOfAllPixels</a>
* <a href="#writeValuesToPositions">writeValuesToPositions</a>
<a name="absolute"></a>
## absolute'

Computes the absolute value of every individual pixel x in a given image.

<pre>f(x) = |x| </pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="addImageAndScalar"></a>
## addImageAndScalar'

Adds a scalar value s to all pixels x of a given image X.

<pre>f(x, s) = x + s</pre>

Parameters (macro):
Image source, Image destination, Number scalar

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="addImagesWeighted"></a>
## addImagesWeighted'

Calculates the sum of pairs of pixels x and y from images X and Y weighted with factors a and b.

<pre>f(x, y, a, b) = x * a + y * b</pre>

Parameters (macro):
Image summand1, Image summand2, Image destination, Number factor1, Number factor2

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, Float arg4, Float arg5

<a name="addImages"></a>
## addImages'

Calculates the sum of pairs of pixels x and y of two images X and Y.

<pre>f(x, y) = x + y</pre>

Parameters (macro):
Image summand1, Image summand2, Image destination

Parameters (Java):
ClearCLBuffer summand1, ClearCLBuffer summand2, ClearCLBuffer destination

<a name="affineTransform2D"></a>
## affineTransform2D'

Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.

Supported transforms:
* center: translate the coordinate origin to the center of the image
* -center: translate the coordinate origin back to the initial origin
* rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
* scale=[factor]: isotropic scaling according to given zoom factor
* scaleX=[factor]: scaling along X-axis according to given zoom factor
* scaleY=[factor]: scaling along Y-axis according to given zoom factor
* shearXY=[factor]: shearing along X-axis in XY plane according to given factor
* translateX=[distance]: translate along X-axis by distance given in pixels
* translateY=[distance]: translate along X-axis by distance given in pixels

Example transform:
transform = "center scale=2 rotate=45 -center";

Parameters (macro):
Image source, Image destination, String transform

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, AffineTransform2D arg3

<a name="affineTransform2D"></a>
## affineTransform2D'

Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.

Supported transforms:
* center: translate the coordinate origin to the center of the image
* -center: translate the coordinate origin back to the initial origin
* rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
* scale=[factor]: isotropic scaling according to given zoom factor
* scaleX=[factor]: scaling along X-axis according to given zoom factor
* scaleY=[factor]: scaling along Y-axis according to given zoom factor
* shearXY=[factor]: shearing along X-axis in XY plane according to given factor
* translateX=[distance]: translate along X-axis by distance given in pixels
* translateY=[distance]: translate along X-axis by distance given in pixels

Example transform:
transform = "center scale=2 rotate=45 -center";

Parameters (macro):
Image source, Image destination, String transform

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3

<a name="affineTransform3D"></a>
## affineTransform3D'

Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.

Supported transforms:
* center: translate the coordinate origin to the center of the image
* -center: translate the coordinate origin back to the initial origin
* rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
* rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
* rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
* rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
* scale=[factor]: isotropic scaling according to given zoom factor
* scaleX=[factor]: scaling along X-axis according to given zoom factor
* scaleY=[factor]: scaling along Y-axis according to given zoom factor
* scaleZ=[factor]: scaling along Z-axis according to given zoom factor
* shearXY=[factor]: shearing along X-axis in XY plane according to given factor
* shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
* shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
* shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
* shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
* shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
* translateX=[distance]: translate along X-axis by distance given in pixels
* translateY=[distance]: translate along X-axis by distance given in pixels
* translateZ=[distance]: translate along X-axis by distance given in pixels

Example transform:
transform = "center scale=2 rotate=45 -center";

Parameters (macro):
Image source, Image destination, String transform

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3

<a name="affineTransform3D"></a>
## affineTransform3D'

Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.

Supported transforms:
* center: translate the coordinate origin to the center of the image
* -center: translate the coordinate origin back to the initial origin
* rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
* rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
* rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
* rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
* scale=[factor]: isotropic scaling according to given zoom factor
* scaleX=[factor]: scaling along X-axis according to given zoom factor
* scaleY=[factor]: scaling along Y-axis according to given zoom factor
* scaleZ=[factor]: scaling along Z-axis according to given zoom factor
* shearXY=[factor]: shearing along X-axis in XY plane according to given factor
* shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
* shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
* shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
* shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
* shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
* translateX=[distance]: translate along X-axis by distance given in pixels
* translateY=[distance]: translate along X-axis by distance given in pixels
* translateZ=[distance]: translate along X-axis by distance given in pixels

Example transform:
transform = "center scale=2 rotate=45 -center";

Parameters (macro):
Image source, Image destination, String transform

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, AffineTransform3D arg3

<a name="affineTransform"></a>
## affineTransform'

CLIJ affineTransform is <b>deprecated</b>. Use affineTransform2D or affineTransform3D instead.

Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.

Supported transforms:
* center: translate the coordinate origin to the center of the image
* -center: translate the coordinate origin back to the initial origin
* rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
* rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
* rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
* rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
* scale=[factor]: isotropic scaling according to given zoom factor
* scaleX=[factor]: scaling along X-axis according to given zoom factor
* scaleY=[factor]: scaling along Y-axis according to given zoom factor
* scaleZ=[factor]: scaling along Z-axis according to given zoom factor
* shearXY=[factor]: shearing along X-axis in XY plane according to given factor
* shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
* shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
* shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
* shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
* shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
* translateX=[distance]: translate along X-axis by distance given in pixels
* translateY=[distance]: translate along X-axis by distance given in pixels
* translateZ=[distance]: translate along X-axis by distance given in pixels

Example transform:
transform = "center scale=2 rotate=45 -center";

Parameters (macro):
Image source, Image destination, String transform

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, AffineTransform3D arg3

<a name="affineTransform"></a>
## affineTransform'

CLIJ affineTransform is <b>deprecated</b>. Use affineTransform2D or affineTransform3D instead.

Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.

Supported transforms:
* center: translate the coordinate origin to the center of the image
* -center: translate the coordinate origin back to the initial origin
* rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
* rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
* rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
* rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
* scale=[factor]: isotropic scaling according to given zoom factor
* scaleX=[factor]: scaling along X-axis according to given zoom factor
* scaleY=[factor]: scaling along Y-axis according to given zoom factor
* scaleZ=[factor]: scaling along Z-axis according to given zoom factor
* shearXY=[factor]: shearing along X-axis in XY plane according to given factor
* shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
* shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
* shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
* shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
* shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
* translateX=[distance]: translate along X-axis by distance given in pixels
* translateY=[distance]: translate along X-axis by distance given in pixels
* translateZ=[distance]: translate along X-axis by distance given in pixels

Example transform:
transform = "center scale=2 rotate=45 -center";

Parameters (macro):
Image source, Image destination, String transform

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3

<a name="applyVectorfield"></a>
## applyVectorfield'

Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 

Parameters (macro):
Image source, Image vectorX, Image vectorY, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer vectorX, ClearCLBuffer vectorY, ClearCLBuffer destination

<a name="applyVectorfield"></a>
## applyVectorfield'

Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 

Parameters (macro):
Image source, Image vectorX, Image vectorY, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5

<a name="argMaximumZProjection"></a>
## argMaximumZProjection'

Determines the maximum projection of an image stack along Z.
Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).

Parameters (macro):
Image source, Image destination_max, Image destination_arg_max

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination_max, ClearCLBuffer destination_arg_max

<a name="automaticThreshold"></a>
## automaticThreshold'

The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one 
of these methods in the method text field:
[Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]

Parameters (macro):
Image input, Image destination, String method

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3

<a name="automaticThreshold"></a>
## automaticThreshold'

The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one 
of these methods in the method text field:
[Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]

Parameters (macro):
Image input, Image destination, String method

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3, Float arg4, Float arg5, Integer arg6

<a name="averageDistanceOfClosestPoints"></a>
## averageDistanceOfClosestPoints

Determine the n point indices with shortest distance for all points in a distance matrix.
This corresponds to the n row indices with minimum values for each column of the distance matrix.

Parameters (macro):
Image distance_matrix, Image indexlist_destination, Number nClosestPointsTofind

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3

<a name="binaryAnd"></a>
## binaryAnd'

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary AND operator &.
All pixel values except 0 in the input images are interpreted as 1.

<pre>f(x, y) = x & y</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination

<a name="binaryEdgeDetection"></a>
## binaryEdgeDetection

Determines pixels/voxels which are on the surface of a binary objects and sets only them to 1 in the destination image.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImageInterface arg1, ClearCLImageInterface arg2



### Example scripts
* [outline.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/outline.ijm)


<a name="binaryIntersection"></a>
## binaryIntersection

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary intersection operator &.
All pixel values except 0 in the input images are interpreted as 1.

<pre>f(x, y) = x & y</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination

<a name="binaryNot"></a>
## binaryNot'

Computes a binary image (containing pixel values 0 and 1) from an image X by negating its pixel values
x using the binary NOT operator !
All pixel values except 0 in the input image are interpreted as 1.

<pre>f(x) = !x</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="binaryOr"></a>
## binaryOr'

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary OR operator |.
All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination

<a name="binarySubtract"></a>
## binarySubtract

Subtracts one binary image from another.

Parameters (macro):
Image minuend, Image subtrahend, Image destination

Parameters (Java):
ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3

<a name="binaryUnion"></a>
## binaryUnion

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary union operator |.
All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination

<a name="binaryXOr"></a>
## binaryXOr'

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary operators AND &, OR | and NOT ! implementing the XOR operator.
All pixel values except 0 in the input images are interpreted as 1.

<pre>f(x, y) = (x & !y) | (!x & y)</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination

<a name="blurSliceBySlice"></a>
## blurSliceBySlice'

Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.

The Gaussian blur is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4, float arg5, float arg6

<a name="blur"></a>
## blur'

Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.

The implementation is done separable. In case a sigma equals zero, the direction is not blurred.

Parameters (macro):
Image source, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5



### Example scripts
* [benchmarkingGaussianBlurs.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/benchmarkingGaussianBlurs.ijm)


<a name="blur"></a>
## blur'

Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.

The implementation is done separable. In case a sigma equals zero, the direction is not blurred.

Parameters (macro):
Image source, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4



### Example scripts
* [benchmarkingGaussianBlurs.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/benchmarkingGaussianBlurs.ijm)


<a name="boundingBox"></a>
## boundingBox

Determines the bounding box of all non-zero pixels in a binary image. The positions will be stored in a new row of ImageJs
Results table in the column 'BoundingBoxX', 'BoundingBoxY', 'BoundingBoxZ', 'BoundingBoxWidth', 'BoundingBoxHeight' 'BoundingBoxDepth'.In case of 2D images Z and depth will be zero.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer source



### Example scripts
* [boundingBoxes.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/boundingBoxes.ijm)


<a name="centerOfMass"></a>
## centerOfMass'

Determines the center of mass of an image or image stack and writes the result in the results table
in the columns MassX, MassY and MassZ.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer source

<a name="connectedComponentsLabelingInplace"></a>
## connectedComponentsLabelingInplace

Performs connected components analysis to a binary image and generates a label map.

Parameters (macro):
Image binary_source_labeling_destination

Parameters (Java):
ClearCLBuffer binary_source_labeling_destination

<a name="connectedComponentsLabeling"></a>
## connectedComponentsLabeling

Performs connected components analysis to a binary image and generates a label map.

Parameters (macro):
Image binary_input, Image labeling_destination

Parameters (Java):
ClearCLBuffer binary_input, ClearCLBuffer labeling_destination



### Example scripts
* [boundingBoxes.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/boundingBoxes.ijm)
* [excludeLabelsOnEdges.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/excludeLabelsOnEdges.ijm)
* [intensity_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/intensity_per_label.ijm)
* [labeling.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/labeling.ijm)
* [labeling_benchmarking.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/labeling_benchmarking.ijm)
* [measure_area_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measure_area_per_label.ijm)
* [measure_statistics.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measure_statistics.ijm)
* [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)
* [particle_analysis.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/particle_analysis.ijm)
* [pullLabelsToROIManager.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/pullLabelsToROIManager.ijm)


<a name="convertToImageJBinary"></a>
## convertToImageJBinary'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="copySlice"></a>
## copySlice'

This method has two purposes: 
It copies a 2D image to a given slice z position in a 3D image stack or 
It copies a given slice at position z in an image stack to a 2D image.

The first case is only available via ImageJ macro. If you are using it, it is recommended that the 
target 3D image already pre-exists in GPU memory before calling this method. Otherwise, CLIJ create 
the image stack with z planes.

Parameters (macro):
Image source, Image destination, Number sliceIndex

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3

<a name="copy"></a>
## copy'

Copies an image.

<pre>f(x) = x</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="countNonZeroPixelsLocallySliceBySlice"></a>
## countNonZeroPixelsLocallySliceBySlice'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="countNonZeroPixelsLocally"></a>
## countNonZeroPixelsLocally'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="countNonZeroPixels"></a>
## countNonZeroPixels

Determines the number of all pixels in a given image which are not equal to 0. It will be stored in a new row of ImageJs
Results table in the column 'Count_non_zero'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer source



### Example scripts
* [intensity_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/intensity_per_label.ijm)


<a name="countNonZeroVoxelsLocally"></a>
## countNonZeroVoxelsLocally'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="countTouchingNeighbors"></a>
## countTouchingNeighbors

Takes a touching-neighbors-matrix as input and delivers a vector with number of touching neighbors per label as a vector.

Parameters (macro):
Image touch_matrix, Image touching_neighbors_count_destination

Parameters (Java):
ClearCLBuffer touch_matrix, ClearCLBuffer touching_neighbors_count_destination

<a name="crop"></a>
## crop'

Crops a given rectangle out of a given image.

Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.

Parameters (macro):
Image source, Image destination, Number startX, Number startY, Number width, Number height

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="crop"></a>
## crop'

Crops a given rectangle out of a given image.

Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.

Parameters (macro):
Image source, Image destination, Number startX, Number startY, Number width, Number height

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="crossCorrelation"></a>
## crossCorrelation



Parameters (macro):
Image input1, Image meanInput1, Image input2, Image meanInput2, Image destination, Number radius, Number deltaPos, Number dimension

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5, int arg6, int arg7, int arg8

<a name="deformableRegistration2D"></a>
## deformableRegistration2D

Applies particle image velocimetry to two images and registers them afterwards by warping input image 2 with a smoothed vector field.

Parameters (macro):
Image input1, Image input2, Image destination, Number maxDeltaX, Number maxDeltaY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, int arg4, int arg5

<a name="detectLabelEdges"></a>
## detectLabelEdges

Takes a labelmap with n labels and generates a (n+1)*(n+1) matrix where all pixels are set to 0 exept those where labels are touching.Only half of the matrix is filled (with x < y). For example, if labels 3 and 4 are touching then the pixel (3,4) in the matrix will be set to 1.

Parameters (macro):
Image label_map, Image touch_matrix_destination

Parameters (Java):
ClearCLBuffer label_map, ClearCLBuffer touch_matrix_destination

<a name="detectMaximaBox"></a>
## detectMaximaBox'

Detects local maxima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
there is no other pixel in a given radius which has a higher intensity, and to 0 otherwise.

Parameters (macro):
Image source, Image destination, Number radius

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3

<a name="detectMaximaSliceBySliceBox"></a>
## detectMaximaSliceBySliceBox'

Detects local maxima in a given square neighborhood of an input image stack. The input image stack is 
processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
given radius which has a higher intensity, and to 0 otherwise.

Parameters (macro):
Image source, Image destination, Number radius

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3

<a name="detectMinimaBox"></a>
## detectMinimaBox'

Detects local minima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
there is no other pixel in a given radius which has a lower intensity, and to 0 otherwise.

Parameters (macro):
Image source, Image destination, Number radius

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3

<a name="detectMinimaSliceBySliceBox"></a>
## detectMinimaSliceBySliceBox'

Detects local minima in a given square neighborhood of an input image stack. The input image stack is 
processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
given radius which has a lower intensity, and to 0 otherwise.

Parameters (macro):
Image source, Image destination, Number radius

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3

<a name="detectOptimaSliceBySlice"></a>
## detectOptimaSliceBySlice'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Boolean arg4

<a name="detectOptima"></a>
## detectOptima'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Boolean arg4

<a name="differenceOfGaussian"></a>
## differenceOfGaussian

Applies Gaussian blur to the input image twice resulting in two images which are then subtracted from each other.

Parameters (macro):
Image input, Image destination, Number sigma1x, Number sigma1y, Number sigma2x, Number sigma2y

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5, Float arg6

<a name="differenceOfGaussian"></a>
## differenceOfGaussian

Applies Gaussian blur to the input image twice resulting in two images which are then subtracted from each other.

Parameters (macro):
Image input, Image destination, Number sigma1x, Number sigma1y, Number sigma2x, Number sigma2y

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5, Float arg6, Float arg7, Float arg8

<a name="dilateBoxSliceBySlice"></a>
## dilateBoxSliceBySlice'

Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
difference is that the output image contains values 0 and 1 instead of 0 and 255.

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="dilateBox"></a>
## dilateBox'

Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
difference is that the output image contains values 0 and 1 instead of 0 and 255.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="dilateSphereSliceBySlice"></a>
## dilateSphereSliceBySlice'

Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="dilateSphere"></a>
## dilateSphere'

Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="distanceMap"></a>
## distanceMap

Generates a distance map from a binary image.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination



### Example scripts
* [distanceMap.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/distanceMap.ijm)


<a name="divideImages"></a>
## divideImages'

Divides two images X and Y by each other pixel wise.

<pre>f(x, y) = x / y</pre>

Parameters (macro):
Image divident, Image divisor, Image destination

Parameters (Java):
ClearCLBuffer divident, ClearCLBuffer divisor, ClearCLBuffer destination

<a name="downsampleSliceBySliceHalfMedian"></a>
## downsampleSliceBySliceHalfMedian'

Scales an image using scaling factors 0.5 for X and Y dimensions. The Z dimension stays untouched. Thus, each slice is processed separately.
The median method is applied. Thus, each pixel value in the destination image equals to the median of
four corresponding pixels in the source image.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="downsample"></a>
## downsample'

Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.

Parameters (macro):
Image source, Image destination, Number factorX, Number factorY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5

<a name="downsample"></a>
## downsample'

Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.

Parameters (macro):
Image source, Image destination, Number factorX, Number factorY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4

<a name="drawBox"></a>
## drawBox

Draws a box at a given start point with given size. All pixels other than in the box are untouched. Consider using clij.op.set(buffer, 0); in advance.

Parameters (macro):
Image destination, Number x, Number y, Number z, Number width, Number height, Number depth

Parameters (Java):
ClearCLBuffer arg1, Float arg2, Float arg3, Float arg4, Float arg5

<a name="drawBox"></a>
## drawBox

Draws a box at a given start point with given size. All pixels other than in the box are untouched. Consider using clij.op.set(buffer, 0); in advance.

Parameters (macro):
Image destination, Number x, Number y, Number z, Number width, Number height, Number depth

Parameters (Java):
ClearCLBuffer arg1, Float arg2, Float arg3, Float arg4, Float arg5, Float arg6, Float arg7

<a name="drawLine"></a>
## drawLine

Draws a line between two points with a given thickness. All pixels other than on the line are untouched. Consider using clij.op.set(buffer, 0); in advance.

Parameters (macro):
Image destination, Number x1, Number y1, Number z1, Number x2, Number y2, Number z2, Number thickness

Parameters (Java):
ClearCLBuffer arg1, Float arg2, Float arg3, Float arg4, Float arg5, Float arg6, Float arg7, Float arg8



### Example scripts
* [drawLine.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/drawLine.ijm)
* [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)
* [mesh_closest_points.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mesh_closest_points.ijm)


<a name="drawSphere"></a>
## drawSphere

Draws a sphere around a given point with given radii in x, y and z (if 3D). All pixels other than in the sphere are untouched. Consider using clij.op.set(buffer, 0); in advance.

Parameters (macro):
Image destination, Number x, Number y, Number z, Number radius_x, Number radius_y, Number radius_z

Parameters (Java):
ClearCLBuffer arg1, Float arg2, Float arg3, Float arg4, Float arg5

<a name="drawSphere"></a>
## drawSphere

Draws a sphere around a given point with given radii in x, y and z (if 3D). All pixels other than in the sphere are untouched. Consider using clij.op.set(buffer, 0); in advance.

Parameters (macro):
Image destination, Number x, Number y, Number z, Number radius_x, Number radius_y, Number radius_z

Parameters (Java):
ClearCLBuffer arg1, Float arg2, Float arg3, Float arg4, Float arg5, Float arg6, Float arg7

<a name="drawTwoValueLine"></a>
## drawTwoValueLine

Draws a line between two points with a given thickness. Pixels close to point 1 are set to value1. Pixels closer to point 2 are set to value2 All pixels other than on the line are untouched. Consider using clij.set(buffer, 0); in advance.

Parameters (macro):
Image destination, Number x1, Number y1, Number z1, Number x2, Number y2, Number z2, Number thickness, Number value1, Number value2

Parameters (Java):
ClearCLBuffer arg1, Float arg2, Float arg3, Float arg4, Float arg5, Float arg6, Float arg7, Float arg8, Float arg9, Float arg10

<a name="equalConstant"></a>
## equalConstant

Determines if an image A and a constant b are equal.

f(a, b) = 1 if a == b; 0 otherwise. 

Parameters (macro):
Image source, Image destination, Number constant

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="equal"></a>
## equal

Determines if two images A and B equal pixel wise.

f(a, b) = 1 if a == b; 0 otherwise. 

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination

<a name="erodeBoxSliceBySlice"></a>
## erodeBoxSliceBySlice'

Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
difference is that the output image contains values 0 and 1 instead of 0 and 255.

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="erodeBox"></a>
## erodeBox'

Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
difference is that the output image contains values 0 and 1 instead of 0 and 255.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="erodeSphereSliceBySlice"></a>
## erodeSphereSliceBySlice'

Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="erodeSphere"></a>
## erodeSphere'

Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="excludeLabelsOnEdges"></a>
## excludeLabelsOnEdges

Removes all labels from a label map which touch the edges. Remaining label elements are renumbered afterwards.

Parameters (macro):
Image label_map_input, Image label_map_destination

Parameters (Java):
ClearCLBuffer label_map_input, ClearCLBuffer label_map_destination



### Example scripts
* [excludeLabelsOnEdges.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/excludeLabelsOnEdges.ijm)
* [pullLabelsToROIManager.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/pullLabelsToROIManager.ijm)


<a name="exponential"></a>
## exponential

Computes base exponential of all pixels values.

f(x) = exp(x)

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="extrema"></a>
## extrema

Returns an image with pixel values most distant from 0: f(x, y) = x if abs(x) > abs(y), y else.

Parameters (macro):
Image input1, Image input2, Image destination

Parameters (Java):
ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination

<a name="fillHistogram"></a>
## fillHistogram'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4

<a name="flip"></a>
## flip'

Flips an image in X and/or Y direction depending on boolean flags.

Parameters (macro):
Image source, Image destination, Boolean flipX, Boolean flipY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Boolean arg3, Boolean arg4, Boolean arg5

<a name="flip"></a>
## flip'

Flips an image in X and/or Y direction depending on boolean flags.

Parameters (macro):
Image source, Image destination, Boolean flipX, Boolean flipY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Boolean arg3, Boolean arg4

<a name="gaussJordan"></a>
## gaussJordan

Gauss Jordan elimination algorithm for solving linear equation systems. Ent the equation coefficients as an n*n sized image A and an n*1 sized image B:
<pre>a(1,1)*x + a(2,1)*y + a(3,1)+z = b(1)
a(2,1)*x + a(2,2)*y + a(3,2)+z = b(2)
a(3,1)*x + a(3,2)*y + a(3,3)+z = b(3)
</pre>
The results will then be given in an n*1 image with values [x, y, z].

Adapted from: 
https://github.com/qbunia/rodinia/blob/master/opencl/gaussian/gaussianElim_kernels.cl
L.G. Szafaryn, K. Skadron and J. Saucerman. "Experiences Accelerating MATLAB Systems
//Biology Applications." in Workshop on Biomedicine in Computing (BiC) at the International
//Symposium on Computer Architecture (ISCA), June 2009.

Parameters (macro):
Image A_matrix, Image B_result_vector, Image solution_destination

Parameters (Java):
ClearCLBuffer A_matrix, ClearCLBuffer B_result_vector, ClearCLBuffer solution_destination

<a name="generateDistanceMatrix"></a>
## generateDistanceMatrix

Takes two images containing coordinates and builds up a matrix containing distance between the points. Convention: image width represents number of points, height represents dimensionality (2D, 3D, ... 10D). The result image has width the first input image and height equals to the width of the second input image.

Parameters (macro):
Image coordinate_list1, Image coordinate_list2, Image distance_matrix_destination

Parameters (Java):
ClearCLBuffer coordinate_list1, ClearCLBuffer coordinate_list2, ClearCLBuffer distance_matrix_destination



### Example scripts
* [mesh_closest_points.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mesh_closest_points.ijm)
* [spot_distance_measurement.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/spot_distance_measurement.ijm)


<a name="generateTouchMatrix"></a>
## generateTouchMatrix

Takes a labelmap with n labels and generates a (n+1)*(n+1) matrix where all pixels are set to 0 exept those where labels are touching.Only half of the matrix is filled (with x < y). For example, if labels 3 and 4 are touching then the pixel (3,4) in the matrix will be set to 1.

Parameters (macro):
Image label_map, Image touch_matrix_destination

Parameters (Java):
ClearCLBuffer label_map, ClearCLBuffer touch_matrix_destination



### Example scripts
* [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)


<a name="getSize"></a>
## getSize

Reads out the size of an image [stack] and writes it to the results table in the columns 'Width', 'Height' and 'Depth'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer source



### Example scripts
* [getsize.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/getsize.ijm)
* [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)
* [mesh_closest_points.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mesh_closest_points.ijm)


<a name="gradientX"></a>
## gradientX'

Computes the gradient of gray values along X. Assuming a, b and c are three adjacent
 pixels in X direction. In the target image will be saved as: <pre>b' = c - a;</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="gradientY"></a>
## gradientY'

Computes the gradient of gray values along Y. Assuming a, b and c are three adjacent
 pixels in Y direction. In the target image will be saved as: <pre>b' = c - a;</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="gradientZ"></a>
## gradientZ'

Computes the gradient of gray values along Z. Assuming a, b and c are three adjacent
 pixels in Z direction. In the target image will be saved as: <pre>b' = c - a;</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="greaterConstant"></a>
## greaterConstant

Determines if two images A and B greater pixel wise.

f(a, b) = 1 if a > b; 0 otherwise. 

Parameters (macro):
Image source, Image destination, Number constant

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="greaterOrEqualConstant"></a>
## greaterOrEqualConstant

Determines if two images A and B greater or equal pixel wise.

f(a, b) = 1 if a >= b; 0 otherwise. 

Parameters (macro):
Image source, Image destination, Number constant

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="greaterOrEqual"></a>
## greaterOrEqual

Determines if two images A and B greater or equal pixel wise.

f(a, b) = 1 if a >= b; 0 otherwise. 

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination

<a name="greater"></a>
## greater

Determines if two images A and B greater pixel wise.

f(a, b) = 1 if a > b; 0 otherwise. 

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination

<a name="histogram"></a>
## histogram'

Determines the histogram of a given image.

Parameters (macro):
Image source, Image destination, Number numberOfBins, Number minimumGreyValue, Number maximumGreyValue, Boolean determineMinAndMax

Parameters (Java):
ClearCLBuffer arg1, Float arg2, Float arg3, Integer arg4

<a name="image2DToResultsTable"></a>
## image2DToResultsTable

Converts an image into a table.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer arg1, ResultsTable arg2



### Example scripts
* [matrix_multiply.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/matrix_multiply.ijm)
* [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)
* [mesh_closest_points.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mesh_closest_points.ijm)


<a name="invert"></a>
## invert'

Computes the negative value of all pixels in a given image. It is recommended to convert images to 
32-bit float before applying this operation.

<pre>f(x) = - x</pre>

For binary images, use binaryNot.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="jaccardIndex"></a>
## jaccardIndex

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="labelToMask"></a>
## labelToMask

Masks a single label in a label map: Sets all pixels in the target image to 1, where the given label index was present in the label map.

Parameters (macro):
Image label_map_source, Image mask_destination, Number label_index

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3



### Example scripts
* [boundingBoxes.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/boundingBoxes.ijm)
* [measure_area_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measure_area_per_label.ijm)


<a name="laplace"></a>
## laplace

Apply the Laplace operator (Diamond neighborhood) to an image.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLBuffer input, ClearCLBuffer destination

<a name="localExtremaBox"></a>
## localExtremaBox

Applies a local minimum and maximum filter. Afterwards, the value is returned which is more far from zero.

Parameters (macro):
Image input, Image destination, Number radiusX, Number radiusY, Number radiusZ

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="localID"></a>
## localID

local id

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLBuffer input, ClearCLBuffer destination

<a name="localPositiveMinimum"></a>
## localPositiveMinimum

null

Parameters (macro):
null

Parameters (Java):
ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3

<a name="localThreshold"></a>
## localThreshold'

Computes a binary image with pixel values 0 and 1 depending on if a pixel value x in image X 
was above of equal to the pixel value m in mask image M.

<pre>f(x) = (1 if (x >=  m)); (0 otherwise)</pre>

Parameters (macro):
Image source, Image localThreshold, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer localThreshold, ClearCLBuffer destination

<a name="logarithm"></a>
## logarithm

Computes baseE logarithm of all pixels values.

f(x) = logarithm(x)

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="maskLabel"></a>
## maskLabel

Computes a masked image by applying a label mask to an image. All pixel values x of image X will be copied
to the destination image in case pixel value m at the same position in the label_map image has the right index value i.

f(x,m,i) = (x if (m == i); (0 otherwise))

Parameters (macro):
Image source, Image label_map, Image destination, Number label_index

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, Float arg4



### Example scripts
* [intensity_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/intensity_per_label.ijm)


<a name="maskStackWithPlane"></a>
## maskStackWithPlane'

Computes a masked image by applying a 2D mask to an image stack. All pixel values x of image X will be copied
to the destination image in case pixel value m at the same spatial position in the mask image is not equal to 
zero.

<pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>

Parameters (macro):
Image source, Image mask, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer mask, ClearCLBuffer destination

<a name="mask"></a>
## mask'

Computes a masked image by applying a mask to an image. All pixel values x of image X will be copied
to the destination image in case pixel value m at the same position in the mask image is not equal to 
zero.

<pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>

Parameters (macro):
Image source, Image mask, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer mask, ClearCLBuffer destination



### Example scripts
* [intensity_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/intensity_per_label.ijm)


<a name="matrixEqual"></a>
## matrixEqual

Checks if all elements of a matrix are different by less than or equal to a given tolerance.
The result will be put in the results table as 1 if yes and 0 otherwise.

Parameters (macro):
Image input1, Image input2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="maximumBox"></a>
## maximumBox'

Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4, int arg5

<a name="maximumIJ"></a>
## maximumIJ'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3

<a name="maximumImageAndScalar"></a>
## maximumImageAndScalar'

Computes the maximum of a constant scalar s and each pixel value x in a given image X.

<pre>f(x, s) = max(x, s)</pre>

Parameters (macro):
Image source, Image destination, Number scalar

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="maximumImages"></a>
## maximumImages'

Computes the maximum of a pair of pixel values x, y from two given images X and Y.

<pre>f(x, y) = max(x, y)</pre>

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination

<a name="maximumOfAllPixels"></a>
## maximumOfAllPixels'

Determines the maximum of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Max'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer source

<a name="maximumOfMaskedPixels"></a>
## maximumOfMaskedPixels

Determines the maximum intensity in an image, but only in pixels which have non-zero values in another mask image.

Parameters (macro):
Image source, Image mask

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer mask

<a name="maximumSliceBySliceSphere"></a>
## maximumSliceBySliceSphere'

Computes the local maximum of a pixels ellipsoidal 2D neighborhood in an image stack 
slice by slice. The ellipses size is specified by its half-width and half-height (radius).

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="maximumSphere"></a>
## maximumSphere'

Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="maximumSphere"></a>
## maximumSphere'

Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="maximumXYZProjection"></a>
## maximumXYZProjection'

Determines the maximum projection of an image along a given dimension. Furthermore, the X and Y
 dimesions of the resulting image must be specified by the user according to its definition:
X = 0
Y = 1
Z = 2


Parameters (macro):
Image source, Image destination_max, Number dimensionX, Number dimensionY, Number projectedDimension

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="maximumZProjection"></a>
## maximumZProjection'

Determines the maximum projection of an image along Z.

Parameters (macro):
Image source, Image destination_max

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination_max

<a name="meanBox"></a>
## meanBox'

Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4, int arg5

<a name="meanClosestSpotDistances"></a>
## meanClosestSpotDistances

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="meanClosestSpotDistances"></a>
## meanClosestSpotDistances

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Boolean arg3

<a name="meanIJ"></a>
## meanIJ'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3

<a name="meanOfMaskedPixels"></a>
## meanOfMaskedPixels

Determines the mean intensity in an image, but only in pixels which have non-zero values in another binary mask image.

Parameters (macro):
Image source, Image mask

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer mask

<a name="meanSliceBySliceSphere"></a>
## meanSliceBySliceSphere'

Computes the local mean average of a pixels ellipsoidal 2D neighborhood in an image stack 
slice by slice. The ellipses size is specified by its half-width and half-height (radius).

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="meanSphere"></a>
## meanSphere'

Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="meanSphere"></a>
## meanSphere'

Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="meanSquaredError"></a>
## meanSquaredError

Determines the mean squared error (MSE) between two images. The MSE will be stored in a new row of ImageJs
Results table in the column 'MSE'.

Parameters (macro):
Image source1, Image source2

Parameters (Java):
ClearCLBuffer source1, ClearCLBuffer source2

<a name="meanZProjection"></a>
## meanZProjection'

Determines the mean average projection of an image along Z.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="medianBox"></a>
## medianBox'

Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
its half-width and half-height (radius).

For technical reasons, the area of the rectangle must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="medianBox"></a>
## medianBox'

Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
its half-width and half-height (radius).

For technical reasons, the area of the rectangle must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="medianSliceBySliceBox"></a>
## medianSliceBySliceBox'

Computes the local median of a pixels rectangular neighborhood. This is done slice-by-slice in a 3D 
image stack. The rectangle is specified by its half-width and half-height (radius).

For technical reasons, the area of the rectangle must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="medianSliceBySliceSphere"></a>
## medianSliceBySliceSphere'

Computes the local median of a pixels ellipsoidal neighborhood. This is done slice-by-slice in a 3D 
image stack. The ellipses size is specified by its half-width and half-height (radius).

For technical reasons, the area of the ellipse must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="medianSphere"></a>
## medianSphere'

Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
its half-width and half-height (radius).

For technical reasons, the area of the rectangle must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="medianSphere"></a>
## medianSphere'

Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
its half-width and half-height (radius).

For technical reasons, the area of the rectangle must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="minimumBox"></a>
## minimumBox'

Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4, int arg5

<a name="minimumIJ"></a>
## minimumIJ'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3

<a name="minimumImageAndScalar"></a>
## minimumImageAndScalar'

Computes the maximum of a constant scalar s and each pixel value x in a given image X.

<pre>f(x, s) = min(x, s)</pre>

Parameters (macro):
Image source, Image destination, Number scalar

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="minimumImages"></a>
## minimumImages'

Computes the minimum of a pair of pixel values x, y from two given images X and Y.

<pre>f(x, y) = min(x, y)</pre>

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination

<a name="minimumOfAllPixels"></a>
## minimumOfAllPixels'

Determines the minimum of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Min'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer source

<a name="minimumOfMaskedPixels"></a>
## minimumOfMaskedPixels

Determines the minimum intensity in an image, but only in pixels which have non-zero values in another mask image.

Parameters (macro):
Image source, Image mask

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer mask

<a name="minimumSliceBySliceSphere"></a>
## minimumSliceBySliceSphere'

Computes the local minimum of a pixels ellipsoidal 2D neighborhood in an image stack 
slice by slice. The ellipses size is specified by its half-width and half-height (radius).

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="minimumSphere"></a>
## minimumSphere'

Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="minimumSphere"></a>
## minimumSphere'

Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="minimumZProjection"></a>
## minimumZProjection'

Determines the minimum projection of an image along Z.

Parameters (macro):
Image source, Image destination_sum

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination_sum

<a name="multiplyImageAndCoordinate"></a>
## multiplyImageAndCoordinate'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3

<a name="multiplyImageAndScalar"></a>
## multiplyImageAndScalar'

Multiplies all pixels value x in a given image X with a constant scalar s.

<pre>f(x, s) = x * s</pre>

Parameters (macro):
Image source, Image destination, Number scalar

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="multiplyImages"></a>
## multiplyImages'

Multiplies all pairs of pixel values x and y from two image X and Y.

<pre>f(x, y) = x * y</pre>

Parameters (macro):
Image factor1, Image factor2, Image destination

Parameters (Java):
ClearCLBuffer factor1, ClearCLBuffer factor2, ClearCLBuffer destination

<a name="multiplyMatrix"></a>
## multiplyMatrix

Multiplies two matrices with each other.

Parameters (macro):
Image matrix1, Image matrix2, Image matrix_destination

Parameters (Java):
ClearCLBuffer matrix1, ClearCLBuffer matrix2, ClearCLBuffer matrix_destination



### Example scripts
* [matrix_multiply.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/matrix_multiply.ijm)


<a name="multiplySliceBySliceWithScalars"></a>
## multiplySliceBySliceWithScalars'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3

<a name="multiplyStackWithPlane"></a>
## multiplyStackWithPlane'

Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. x and y are at 
the same spatial position within a plane.

<pre>f(x, y) = x * y</pre>

Parameters (macro):
Image sourceStack, Image sourcePlane, Image destination

Parameters (Java):
ClearCLBuffer sourceStack, ClearCLBuffer sourcePlane, ClearCLBuffer destination

<a name="nClosestPoints"></a>
## nClosestPoints

Determine the n point indices with shortest distance for all points in a distance matrix.
This corresponds to the n row indices with minimum values for each column of the distance matrix.

Parameters (macro):
Image distance_matrix, Image indexlist_destination, Number nClosestPointsTofind

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2



### Example scripts
* [mesh_closest_points.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mesh_closest_points.ijm)


<a name="nonzeroMaximumDiamond"></a>
## nonzeroMaximumDiamond

Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3, ClearCLKernel arg4

<a name="nonzeroMaximumDiamond"></a>
## nonzeroMaximumDiamond

Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3

<a name="nonzeroMinimumDiamond"></a>
## nonzeroMinimumDiamond

Apply a minimum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3, ClearCLKernel arg4

<a name="nonzeroMinimumDiamond"></a>
## nonzeroMinimumDiamond

Apply a minimum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3

<a name="notEqualConstant"></a>
## notEqualConstant

Determines if two images A and B equal pixel wise.

f(a, b) = 1 if a != b; 0 otherwise. 

Parameters (macro):
Image source, Image destination, Number constant

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="notEqual"></a>
## notEqual

Determines if two images A and B equal pixel wise.

f(a, b) = 1 if a != b; 0 otherwise. 

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination

<a name="onlyzeroOverwriteMaximumBox"></a>
## onlyzeroOverwriteMaximumBox

null

Parameters (macro):
null

Parameters (Java):
ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3

<a name="onlyzeroOverwriteMaximumDiamond"></a>
## onlyzeroOverwriteMaximumDiamond

TODO

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3



### Example scripts
* [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)


<a name="particleImageVelocimetry2D"></a>
## particleImageVelocimetry2D

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, Integer arg5

<a name="particleImageVelocimetryTimelapse"></a>
## particleImageVelocimetryTimelapse

Run particle image velocimetry on a 2D+t timelapse.

Parameters (macro):
Image source, Image destinationDeltaX, Image destinationDeltaY, Image destinationDeltaZ, Number maxDeltaX, Number maxDeltaY, Number maxDeltaZ, Boolean correctLocalShift

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, int arg5, int arg6, int arg7, boolean arg8

<a name="particleImageVelocimetry"></a>
## particleImageVelocimetry

For every pixel in source image 1, determine the pixel with the most similar intensity in 
 the local neighborhood with a given radius in source image 2. Write the distance in 
X and Y in the two corresponding destination images.

Parameters (macro):
Image source1, Image source2, Image destinationDeltaX, Image destinationDeltaY, Image destinationDeltaZ, Number maxDeltaX, Number maxDeltaY, Number maxDeltaZ, Boolean correctLocalShift

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5, Integer arg6, Integer arg7, Integer arg8, boolean arg9

<a name="paste"></a>
## paste

Pastes an image into another image at a given position.

Parameters (macro):
Image source, Image destination, Number destinationX, Number destinationY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4



### Example scripts
* [make_super_blobs.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/make_super_blobs.ijm)
* [paste_images.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/paste_images.ijm)


<a name="paste"></a>
## paste

Pastes an image into another image at a given position.

Parameters (macro):
Image source, Image destination, Number destinationX, Number destinationY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5



### Example scripts
* [make_super_blobs.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/make_super_blobs.ijm)
* [paste_images.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/paste_images.ijm)


<a name="powerImages"></a>
## powerImages

Calculates x to the power of y pixel wise of two images X and Y.

Parameters (macro):
Image input, Image exponent, Image destination

Parameters (Java):
ClearCLBuffer input, ClearCLBuffer exponent, ClearCLBuffer destination

<a name="power"></a>
## power'

Computes all pixels value x to the power of a given exponent a.

<pre>f(x, a) = x ^ a</pre>

Parameters (macro):
Image source, Image destination, Number exponent

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="preloadFromDisc"></a>
## preloadFromDisc

This plugin takes two image filenames and loads them into RAM. The first image is returned immediately, the second image is loaded in the background and  will be returned when the plugin is called again.

 It is assumed that all images have the same size. If this is not the case, call release(image) before  getting the second image.

Parameters (macro):
Image destination, String filename, String nextFilename, String loaderId

Parameters (Java):
ClearCLBuffer arg1, String arg2, String arg3, String arg4



### Example scripts
* [preloading.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/preloading.ijm)


<a name="presign"></a>
## presign

Determines the extrema of pixel values: f(x) = x / abs(x).

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLBuffer input, ClearCLBuffer destination

<a name="pullAsROI"></a>
## pullAsROI

Pulls a binary image from the GPU memory and puts it on the currently active ImageJ window.

Parameters (macro):
Image binary_input

Parameters (Java):
ClearCLBuffer binary_input



### Example scripts
* [pullAsROI.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/pullAsROI.ijm)


<a name="radialProjection"></a>
## radialProjection'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="readImageFromDisc"></a>
## readImageFromDisc

Read an image from disc.

Parameters (macro):
Image destination, String filename

Parameters (Java):
String arg1

<a name="readRawImageFromDisc"></a>
## readRawImageFromDisc

Reads a raw file from disc and pushes it immediately to the GPU.

Parameters (macro):
Image destination, String filename, Number width, Number height, Number depth, Number bitsPerPixel

Parameters (Java):
String arg1, Integer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="readRawImageFromDisc"></a>
## readRawImageFromDisc

Reads a raw file from disc and pushes it immediately to the GPU.

Parameters (macro):
Image destination, String filename, Number width, Number height, Number depth, Number bitsPerPixel

Parameters (Java):
ClearCLBuffer arg1, String arg2

<a name="replaceIntensities"></a>
## replaceIntensities

Replaces specific intensities specified in a vector image with given new values specified in a vector image.

Parameters (macro):
Image input, Image new_values_vector, Image destination

Parameters (Java):
ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3

<a name="replaceIntensity"></a>
## replaceIntensity

Replaces a specific intensity in an image with a given new value.

Parameters (macro):
Image input, Image destination, Number oldValue, number newValue

Parameters (Java):
ClearCLImageInterface arg1, ClearCLImageInterface arg2, Float arg3, Float arg4

<a name="resliceBottom"></a>
## resliceBottom'

Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
offers less flexibility such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="resliceLeft"></a>
## resliceLeft'

Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
 but offers less flexibility such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="resliceRight"></a>
## resliceRight'

Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
 but offers less flexibility such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="resliceTop"></a>
## resliceTop'

Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
offers less flexibility such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="rotateLeft"></a>
## rotateLeft'

Rotates a given input image by 90 degrees counter-clockwise. For that, X and Y axis of an image stack
are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="rotateRight"></a>
## rotateRight'

Rotates a given input image by 90 degrees clockwise. For that, X and Y axis of an image stack
are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination

<a name="saveAsTIF"></a>
## saveAsTIF

Pulls an image from the GPU memory and saves it as TIF to disc.

Parameters (macro):
Image input, String filename

Parameters (Java):
ClearCLBuffer arg1, String arg2

<a name="setNonZeroPixelsToPixelIndex"></a>
## setNonZeroPixelsToPixelIndex

null

Parameters (macro):
null

Parameters (Java):
ClearCLImageInterface arg1, ClearCLImageInterface arg2

<a name="setWhereXequalsY"></a>
## setWhereXequalsY

Sets all pixel values a of a given image A to a constant value v in case its coordinates x == y. Otherwise the pixel is not overwritten.
If you want to initialize an identity transfrom matrix, set all pixels to 0 first.

<pre>f(a) = v</pre>

Parameters (macro):
Image source, Number value

Parameters (Java):
ClearCLBuffer arg1, Float arg2

<a name="set"></a>
## set'

Sets all pixel values x of a given image X to a constant value v.

<pre>f(x) = v</pre>

Parameters (macro):
Image source, Number value

Parameters (Java):
ClearCLBuffer arg1, Float arg2

<a name="shiftIntensitiesToCloseGaps"></a>
## shiftIntensitiesToCloseGaps

null

Parameters (macro):
null

Parameters (Java):
ClearCLImageInterface arg1, ClearCLImageInterface arg2

<a name="shortestDistances"></a>
## shortestDistances

Determine the shortest distance from a distance matrix. This corresponds to the minimum in a matrix for each individial column.

Parameters (macro):
Image distance_matrix, Image destination_minimum_distances

Parameters (Java):
ClearCLBuffer distance_matrix, ClearCLBuffer destination_minimum_distances



### Example scripts
* [spot_distance_measurement.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/spot_distance_measurement.ijm)


<a name="smallerConstant"></a>
## smallerConstant

Determines if two images A and B smaller pixel wise.

f(a, b) = 1 if a < b; 0 otherwise. 

Parameters (macro):
Image source, Image destination, Number constant

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="smallerOrEqualConstant"></a>
## smallerOrEqualConstant

Determines if two images A and B smaller or equal pixel wise.

f(a, b) = 1 if a <= b; 0 otherwise. 

Parameters (macro):
Image source, Image destination, Number constant

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="smallerOrEqual"></a>
## smallerOrEqual

Determines if two images A and B smaller or equal pixel wise.

f(a, b) = 1 if a <= b; 0 otherwise. 

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination

<a name="smaller"></a>
## smaller

Determines if two images A and B smaller pixel wise.

f(a, b) = 1 if a < b; 0 otherwise. 

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination

<a name="sorensenDiceCoefficient"></a>
## sorensenDiceCoefficient

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="splitStack"></a>
## splitStack'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer[] arg2

<a name="spotsToPointList"></a>
## spotsToPointList

Transforms a spots image as resulting from maxim detection in an image where every column cotains d 
pixels (with d = dimensionality of the original image) with the coordinates of the maxima.

Parameters (macro):
Image input_spots, Image destination_pointlist

Parameters (Java):
ClearCLBuffer input_spots, ClearCLBuffer destination_pointlist



### Example scripts
* [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)
* [mesh_closest_points.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mesh_closest_points.ijm)
* [spot_distance_measurement.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/spot_distance_measurement.ijm)


<a name="stackToTiles"></a>
## stackToTiles

Stack to tiles.

Parameters (macro):
Image source, Image destination, Number tiles_x, Number tiles_y

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="standardDeviationOfAllPixels"></a>
## standardDeviationOfAllPixels

Determines the standard deviation of all pixels in an image. The value will be stored in a new row of ImageJs
Results table in the column 'Standard_deviation'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer source

<a name="standardDeviationOfAllPixels"></a>
## standardDeviationOfAllPixels

Determines the standard deviation of all pixels in an image. The value will be stored in a new row of ImageJs
Results table in the column 'Standard_deviation'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer arg1, Float arg2

<a name="statisticsOfLabelledPixels"></a>
## statisticsOfLabelledPixels

Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.

Parameters (macro):
Image input, Image labelmap

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3



### Example scripts
* [measure_statistics.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measure_statistics.ijm)
* [particle_analysis.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/particle_analysis.ijm)


<a name="statisticsOfLabelledPixels"></a>
## statisticsOfLabelledPixels

Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.

Parameters (macro):
Image input, Image labelmap

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ResultsTable arg3



### Example scripts
* [measure_statistics.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measure_statistics.ijm)
* [particle_analysis.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/particle_analysis.ijm)


<a name="statisticsOfLabelledPixels"></a>
## statisticsOfLabelledPixels

Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.

Parameters (macro):
Image input, Image labelmap

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4



### Example scripts
* [measure_statistics.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measure_statistics.ijm)
* [particle_analysis.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/particle_analysis.ijm)


<a name="statisticsOfLabelledPixels"></a>
## statisticsOfLabelledPixels

Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.

Parameters (macro):
Image input, Image labelmap

Parameters (Java):
ClearCLBuffer input, ClearCLBuffer labelmap



### Example scripts
* [measure_statistics.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measure_statistics.ijm)
* [particle_analysis.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/particle_analysis.ijm)


<a name="stopWatch"></a>
## stopWatch

Measures time and outputs delay to last call.

Parameters (macro):
String text

Parameters (Java):
String arg1

<a name="subtractBackground"></a>
## subtractBackground

Applies Gaussian blur to the input image and subtracts the result from the original image.

Parameters (macro):
Image input, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4

<a name="subtractBackground"></a>
## subtractBackground

Applies Gaussian blur to the input image and subtracts the result from the original image.

Parameters (macro):
Image input, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5

<a name="subtractImages"></a>
## subtractImages'

Subtracts one image X from another image Y pixel wise.

<pre>f(x, y) = x - y</pre>

Parameters (macro):
Image subtrahend, Image minuend, Image destination

Parameters (Java):
ClearCLBuffer subtrahend, ClearCLBuffer minuend, ClearCLBuffer destination

<a name="subtract"></a>
## subtract'

Subtracts one image X from another image Y pixel wise.

<pre>f(x, y) = x - y</pre>

Parameters (macro):
Image subtrahend, Image minuend, Image destination

Parameters (Java):
ClearCLBuffer subtrahend, ClearCLBuffer minuend, ClearCLBuffer destination

<a name="sumPixelsSliceBySlice"></a>
## sumPixelsSliceBySlice'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1

<a name="sumPixels"></a>
## sumPixels'

Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Sum'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer source

<a name="sumZProjection"></a>
## sumZProjection'

Determines the sum projection of an image along Z.

Parameters (macro):
Image source, Image destination_sum

Parameters (Java):
ClearCLBuffer source, ClearCLBuffer destination_sum

<a name="threshold"></a>
## threshold'

Computes a binary image with pixel values 0 and 1. All pixel values x of a given input image with 
value larger or equal to a given threshold t will be set to 1.

f(x,t) = (1 if (x >= t); (0 otherwise))

This plugin is comparable to setting a raw threshold in ImageJ and using the 'Convert to Mask' menu.

Parameters (macro):
Image source, Image destination, Number threshold

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="topHatBox"></a>
## topHatBox

Apply a top-hat filter to the input image.

Parameters (macro):
Image input, Image destination, Number radiusX, Number radiusY, Number radiusZ

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="topHatSphere"></a>
## topHatSphere

Apply a top-hat filter to the input image.

Parameters (macro):
Image input, Image destination, Number radiusX, Number radiusY, Number radiusZ

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="touchMatrixToMesh"></a>
## touchMatrixToMesh

Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a touch matrix of size n*n. and draws lines from all points to points if the corresponding pixel in the touch matrix is 1.

Parameters (macro):
Image pointlist, Image touch_matrix, Image mesh_destination

Parameters (Java):
ClearCLBuffer pointlist, ClearCLBuffer touch_matrix, ClearCLBuffer mesh_destination

<a name="translationRegistration"></a>
## translationRegistration

Measures center of mass of thresholded objects in the two input images and translates the second image so that it better fits to the first image.

Parameters (macro):
Image input1, Image input2, Image destination

Parameters (Java):
ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination

<a name="translationRegistration"></a>
## translationRegistration

Measures center of mass of thresholded objects in the two input images and translates the second image so that it better fits to the first image.

Parameters (macro):
Image input1, Image input2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, double[] arg3

<a name="translationTimelapseRegistration"></a>
## translationTimelapseRegistration

Applies 2D translation registration to every pair of t, t+1 slices of a 2D+t image stack.

Parameters (macro):
Image input, Image output

Parameters (Java):
ClearCLBuffer input, ClearCLBuffer output

<a name="transposeXY"></a>
## transposeXY

Transpose X and Y in an image.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLBuffer input, ClearCLBuffer destination

<a name="transposeXZ"></a>
## transposeXZ

Transpose X and Z in an image.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLBuffer input, ClearCLBuffer destination

<a name="transposeYZ"></a>
## transposeYZ

Transpose Y and Z in an image.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLBuffer input, ClearCLBuffer destination

<a name="varianceOfAllPixels"></a>
## varianceOfAllPixels

Determines the variance of all pixels in an image. The value will be stored in a new row of ImageJs
Results table in the column 'Variance'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer source

<a name="varianceOfAllPixels"></a>
## varianceOfAllPixels

Determines the variance of all pixels in an image. The value will be stored in a new row of ImageJs
Results table in the column 'Variance'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer arg1, Float arg2

<a name="writeValuesToPositions"></a>
## writeValuesToPositions

Takes an image with three/four rows (2D: height = 3; 3D: height = 4): x, y [, z] and v and target image. The value v will be written at position x/y[/z] in the target image.

Parameters (macro):
Image positionsAndValues, Image destination

Parameters (Java):
ClearCLBuffer positionsAndValues, ClearCLBuffer destination

