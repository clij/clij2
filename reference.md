# CLIJ2 reference
This reference contains all methods currently available in CLIJ2.

__Please note:__ CLIJ2 is under heavy construction. This list may change at any point.Methods marked with ' were available in CLIJ1.

<a name="absolute"></a>
## absolute'

Computes the absolute value of every individual pixel x in a given image.

<pre>f(x) = |x| </pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="absolute"></a>
## absolute'

Computes the absolute value of every individual pixel x in a given image.

<pre>f(x) = |x| </pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="addImageAndScalar"></a>
## addImageAndScalar'

Adds a scalar value s to all pixels x of a given image X.

<pre>f(x, s) = x + s</pre>

Parameters (macro):
Image source, Image destination, Number scalar

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Float arg3

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
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, Float arg4, Float arg5

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
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="addImages"></a>
## addImages'

Calculates the sum of pairs of pixels x and y of two images X and Y.

<pre>f(x, y) = x + y</pre>

Parameters (macro):
Image summand1, Image summand2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

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
ClearCLImage arg1, ClearCLImage arg2, AffineTransform2D arg3

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
ClearCLImage arg1, ClearCLImage arg2, float[] arg3

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
ClearCLImage arg1, ClearCLImage arg2, float[] arg3

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
ClearCLImage arg1, ClearCLImage arg2, AffineTransform3D arg3

<a name="affineTransform"></a>
## affineTransform'

CLIJ affineTransform is deprecated. Use affineTransform2D or affineTransform3D instead.

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

<a name="affineTransform"></a>
## affineTransform'

CLIJ affineTransform is deprecated. Use affineTransform2D or affineTransform3D instead.

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
ClearCLImage arg1, ClearCLImage arg2, AffineTransform3D arg3

<a name="affineTransform"></a>
## affineTransform'

CLIJ affineTransform is deprecated. Use affineTransform2D or affineTransform3D instead.

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

CLIJ affineTransform is deprecated. Use affineTransform2D or affineTransform3D instead.

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
ClearCLImage arg1, ClearCLImage arg2, float[] arg3

<a name="applyVectorfield"></a>
## applyVectorfield'

Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 

Parameters (macro):
Image source, Image vectorX, Image vectorY, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4

<a name="applyVectorfield"></a>
## applyVectorfield'

Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 

Parameters (macro):
Image source, Image vectorX, Image vectorY, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5

<a name="applyVectorfield"></a>
## applyVectorfield'

Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 

Parameters (macro):
Image source, Image vectorX, Image vectorY, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4, ClearCLImage arg5

<a name="applyVectorfield"></a>
## applyVectorfield'

Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 

Parameters (macro):
Image source, Image vectorX, Image vectorY, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4

<a name="argMaximumZProjection"></a>
## argMaximumZProjection'

Determines the maximum projection of an image stack along Z.
Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).

Parameters (macro):
Image source, Image destination_max, Image destination_arg_max

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="argMaximumZProjection"></a>
## argMaximumZProjection'

Determines the maximum projection of an image stack along Z.
Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).

Parameters (macro):
Image source, Image destination_max, Image destination_arg_max

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

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

<a name="binaryAnd"></a>
## binaryAnd'

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary AND operator &.
All pixel values except 0 in the input images are interpreted as 1.

<pre>f(x, y) = x & y</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="binaryAnd"></a>
## binaryAnd'

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary AND operator &.
All pixel values except 0 in the input images are interpreted as 1.

<pre>f(x, y) = x & y</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="binaryIntersection"></a>
## binaryIntersection

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary intersection operator &.
All pixel values except 0 in the input images are interpreted as 1.

<pre>f(x, y) = x & y</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="binaryNot"></a>
## binaryNot'

Computes a binary image (containing pixel values 0 and 1) from an image X by negating its pixel values
x using the binary NOT operator !
All pixel values except 0 in the input image are interpreted as 1.

<pre>f(x) = !x</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="binaryNot"></a>
## binaryNot'

Computes a binary image (containing pixel values 0 and 1) from an image X by negating its pixel values
x using the binary NOT operator !
All pixel values except 0 in the input image are interpreted as 1.

<pre>f(x) = !x</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="binaryOr"></a>
## binaryOr'

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary OR operator |.
All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="binaryOr"></a>
## binaryOr'

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary OR operator |.
All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="binaryUnion"></a>
## binaryUnion

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary union operator |.
All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="binaryXOr"></a>
## binaryXOr'

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary operators AND &, OR | and NOT ! implementing the XOR operator.
All pixel values except 0 in the input images are interpreted as 1.

<pre>f(x, y) = (x & !y) | (!x & y)</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="binaryXOr"></a>
## binaryXOr'

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary operators AND &, OR | and NOT ! implementing the XOR operator.
All pixel values except 0 in the input images are interpreted as 1.

<pre>f(x, y) = (x & !y) | (!x & y)</pre>

Parameters (macro):
Image operand1, Image operand2, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="blurSliceBySlice"></a>
## blurSliceBySlice'

Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.

The Gaussian blur is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4, float arg5, float arg6

<a name="blurSliceBySlice"></a>
## blurSliceBySlice'

Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.

The Gaussian blur is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Float arg5, Float arg6

<a name="blur"></a>
## blur'

Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.

The implementation is done separable. In case a sigma equals zero, the direction is not blurred.

Parameters (macro):
Image source, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLImage arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5

<a name="blur"></a>
## blur'

Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.

The implementation is done separable. In case a sigma equals zero, the direction is not blurred.

Parameters (macro):
Image source, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Float arg3, Float arg4

<a name="blur"></a>
## blur'

Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.

The implementation is done separable. In case a sigma equals zero, the direction is not blurred.

Parameters (macro):
Image source, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4

<a name="blur"></a>
## blur'

Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.

The implementation is done separable. In case a sigma equals zero, the direction is not blurred.

Parameters (macro):
Image source, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5

<a name="blur"></a>
## blur'

Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.

The implementation is done separable. In case a sigma equals zero, the direction is not blurred.

Parameters (macro):
Image source, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLImage arg1, ClearCLBuffer arg2, Float arg3, Float arg4

<a name="blur"></a>
## blur'

Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.

The implementation is done separable. In case a sigma equals zero, the direction is not blurred.

Parameters (macro):
Image source, Image destination, Number sigmaX, Number sigmaY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Float arg3, Float arg4, Float arg5

<a name="boundingBox"></a>
## boundingBox

Determines the bounding box of all non-zero pixels in a binary image. The positions will be stored in a new row of ImageJs
Results table in the column 'BoundingBoxX', 'BoundingBoxY', 'BoundingBoxWidth' and 'BoundingBoxHeight' in case of 2D images. If you pass over a 3D image stack, also columns 'BoundingBoxZ' and 'BoundingBoxDepth' will be given.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer arg1

<a name="centerOfMass"></a>
## centerOfMass'

Determines the center of mass of an image or image stack and writes the result in the results table
in the columns MassX, MassY and MassZ.

Parameters (macro):
Image source

Parameters (Java):
ClearCLImage arg1

<a name="centerOfMass"></a>
## centerOfMass'

Determines the center of mass of an image or image stack and writes the result in the results table
in the columns MassX, MassY and MassZ.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer arg1

<a name="connectedComponentsLabeling"></a>
## connectedComponentsLabeling

Performs connected components analysis to a binary image and generates a label map.

Parameters (macro):
Image binary_input, Image labeling_destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="convertToImageJBinary"></a>
## convertToImageJBinary'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

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
ClearCLImage arg1, ClearCLImage arg2, Integer arg3

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
ClearCLImage arg1, ClearCLBuffer arg2

<a name="copy"></a>
## copy'

Copies an image.

<pre>f(x) = x</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="copy"></a>
## copy'

Copies an image.

<pre>f(x) = x</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="copy"></a>
## copy'

Copies an image.

<pre>f(x) = x</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLImage arg2

<a name="countNonZeroPixelsLocallySliceBySlice"></a>
## countNonZeroPixelsLocallySliceBySlice'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="countNonZeroPixelsLocallySliceBySlice"></a>
## countNonZeroPixelsLocallySliceBySlice'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

<a name="countNonZeroPixelsLocally"></a>
## countNonZeroPixelsLocally'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

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
ClearCLBuffer arg1

<a name="countNonZeroVoxelsLocally"></a>
## countNonZeroVoxelsLocally'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="countNonZeroVoxelsLocally"></a>
## countNonZeroVoxelsLocally'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5

<a name="crop"></a>
## crop'

Crops a given rectangle out of a given image.

Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.

Parameters (macro):
Image source, Image destination, Number startX, Number startY, Number width, Number height

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

<a name="crop"></a>
## crop'

Crops a given rectangle out of a given image.

Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.

Parameters (macro):
Image source, Image destination, Number startX, Number startY, Number width, Number height

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5

<a name="crop"></a>
## crop'

Crops a given rectangle out of a given image.

Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.

Parameters (macro):
Image source, Image destination, Number startX, Number startY, Number width, Number height

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="crop"></a>
## crop'

Crops a given rectangle out of a given image.

Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.

Parameters (macro):
Image source, Image destination, Number startX, Number startY, Number width, Number height

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="crossCorrelation"></a>
## crossCorrelation



Parameters (macro):
Image input1, Image meanInput1, Image input2, Image meanInput2, Image destination, Number radius, Number deltaPos, Number dimension

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4, ClearCLImage arg5, int arg6, int arg7, int arg8

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

<a name="detectMaximaBox"></a>
## detectMaximaBox'

Detects local maxima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
there is no other pixel in a given radius which has a higher intensity, and to 0 otherwise.

Parameters (macro):
Image source, Image destination, Number radius

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3

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
ClearCLImage arg1, ClearCLImage arg2, Integer arg3

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
ClearCLImage arg1, ClearCLImage arg2, Integer arg3

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
ClearCLImage arg1, ClearCLImage arg2, Integer arg3

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
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Boolean arg4

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
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Boolean arg4

<a name="detectOptima"></a>
## detectOptima'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Boolean arg4

<a name="differenceOfGaussianSliceBySlice"></a>
## differenceOfGaussianSliceBySlice'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Float arg4, Float arg5

<a name="differenceOfGaussian"></a>
## differenceOfGaussian'

Applies Gaussian blur to the input image twice resulting in two images which are then subtracted from each other.

Parameters (macro):
Image input, Image destination, Number sigma1x, Number sigma1y, Number sigma2x, Number sigma2y

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Float arg4, Float arg5

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
ClearCLBuffer arg1, ClearCLBuffer arg2

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
ClearCLImage arg1, ClearCLImage arg2

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
ClearCLBuffer arg1, ClearCLBuffer arg2

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
ClearCLImage arg1, ClearCLImage arg2

<a name="dilateSphereSliceBySlice"></a>
## dilateSphereSliceBySlice'

Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="dilateSphereSliceBySlice"></a>
## dilateSphereSliceBySlice'

Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="dilateSphere"></a>
## dilateSphere'

Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="dilateSphere"></a>
## dilateSphere'

Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="divideImages"></a>
## divideImages'

Divides two images X and Y by each other pixel wise.

<pre>f(x, y) = x / y</pre>

Parameters (macro):
Image divident, Image divisor, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="divideImages"></a>
## divideImages'

Divides two images X and Y by each other pixel wise.

<pre>f(x, y) = x / y</pre>

Parameters (macro):
Image divident, Image divisor, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="downsampleSliceBySliceHalfMedian"></a>
## downsampleSliceBySliceHalfMedian'

Scales an image using scaling factors 0.5 for X and Y dimensions. The Z dimension stays untouched. Thus, each slice is processed separately.
The median method is applied. Thus, each pixel value in the destination image equals to the median of
four corresponding pixels in the source image.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="downsampleSliceBySliceHalfMedian"></a>
## downsampleSliceBySliceHalfMedian'

Scales an image using scaling factors 0.5 for X and Y dimensions. The Z dimension stays untouched. Thus, each slice is processed separately.
The median method is applied. Thus, each pixel value in the destination image equals to the median of
four corresponding pixels in the source image.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="downsample"></a>
## downsample'

Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.

Parameters (macro):
Image source, Image destination, Number factorX, Number factorY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Float arg3, Float arg4

<a name="downsample"></a>
## downsample'

Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.

Parameters (macro):
Image source, Image destination, Number factorX, Number factorY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4

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
ClearCLImage arg1, ClearCLImage arg2, Float arg3, Float arg4, Float arg5

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
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

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
ClearCLBuffer arg1, ClearCLBuffer arg2

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
ClearCLImage arg1, ClearCLImage arg2

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
ClearCLImage arg1, ClearCLImage arg2

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
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="erodeSphereSliceBySlice"></a>
## erodeSphereSliceBySlice'

Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="erodeSphereSliceBySlice"></a>
## erodeSphereSliceBySlice'

Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="erodeSphere"></a>
## erodeSphere'

Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="erodeSphere"></a>
## erodeSphere'

Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="exponential"></a>
## exponential

Computes base exponential of all pixels values.

f(x) = exp(x)

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="exponential"></a>
## exponential

Computes base exponential of all pixels values.

f(x) = exp(x)

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="extrema"></a>
## extrema

Returns an image with pixel values most distant from 0: f(x, y) = x if abs(x) > abs(y), y else.

Parameters (macro):
Image input1, Image input2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

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
ClearCLBuffer arg1, ClearCLBuffer arg2, Boolean arg3, Boolean arg4

<a name="flip"></a>
## flip'

Flips an image in X and/or Y direction depending on boolean flags.

Parameters (macro):
Image source, Image destination, Boolean flipX, Boolean flipY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Boolean arg3, Boolean arg4, Boolean arg5

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
ClearCLImage arg1, ClearCLImage arg2, Boolean arg3, Boolean arg4

<a name="generateDistanceMatrix"></a>
## generateDistanceMatrix

Takes two images containing coordinates and builds up a matrix containing distance between the points. Convention: image width represents number of points, height represents dimensionality (2D, 3D, ... 10D). The result image has width the first input image and height equals to the width of the second input image.

Parameters (macro):
Image coordinate_list1, Image coordinate_list2, Image distance_matrix_destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="getSize"></a>
## getSize

Reads out the size of an image [stack] and writes it to the results table in the columns 'Width', 'Height' and 'Depth'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer arg1

<a name="gradientX"></a>
## gradientX'

Computes the gradient of gray values along X. Assuming a, b and c are three adjacent
 pixels in X direction. In the target image will be saved as: <pre>b' = c - a;</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="gradientX"></a>
## gradientX'

Computes the gradient of gray values along X. Assuming a, b and c are three adjacent
 pixels in X direction. In the target image will be saved as: <pre>b' = c - a;</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="gradientY"></a>
## gradientY'

Computes the gradient of gray values along Y. Assuming a, b and c are three adjacent
 pixels in Y direction. In the target image will be saved as: <pre>b' = c - a;</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="gradientY"></a>
## gradientY'

Computes the gradient of gray values along Y. Assuming a, b and c are three adjacent
 pixels in Y direction. In the target image will be saved as: <pre>b' = c - a;</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="gradientZ"></a>
## gradientZ'

Computes the gradient of gray values along Z. Assuming a, b and c are three adjacent
 pixels in Z direction. In the target image will be saved as: <pre>b' = c - a;</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="gradientZ"></a>
## gradientZ'

Computes the gradient of gray values along Z. Assuming a, b and c are three adjacent
 pixels in Z direction. In the target image will be saved as: <pre>b' = c - a;</pre>

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

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
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="greater"></a>
## greater

Determines if two images A and B greater pixel wise.

f(a, b) = 1 if a > b; 0 otherwise. 

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="histogram"></a>
## histogram'

Determines the histogram of a given image.

Parameters (macro):
Image source, Image destination, Number numberOfBins, Number minimumGreyValue, Number maximumGreyValue, Boolean determineMinAndMax

Parameters (Java):
ClearCLBuffer arg1, Float arg2, Float arg3, Integer arg4

<a name="invert"></a>
## invert'

Computes the negative value of all pixels in a given image. It is recommended to convert images to 
32-bit float before applying this operation.

<pre>f(x) = - x</pre>

For binary images, use binaryNot.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="invert"></a>
## invert'

Computes the negative value of all pixels in a given image. It is recommended to convert images to 
32-bit float before applying this operation.

<pre>f(x) = - x</pre>

For binary images, use binaryNot.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

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

<a name="laplace"></a>
## laplace

Apply the Laplace operator (Diamond neighborhood) to an image.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

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
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="localThreshold"></a>
## localThreshold'

Computes a binary image with pixel values 0 and 1 depending on if a pixel value x in image X 
was above of equal to the pixel value m in mask image M.

<pre>f(x) = (1 if (x >=  m)); (0 otherwise)</pre>

Parameters (macro):
Image source, Image localThreshold, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="localThreshold"></a>
## localThreshold'

Computes a binary image with pixel values 0 and 1 depending on if a pixel value x in image X 
was above of equal to the pixel value m in mask image M.

<pre>f(x) = (1 if (x >=  m)); (0 otherwise)</pre>

Parameters (macro):
Image source, Image localThreshold, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="logarithm"></a>
## logarithm

Computes baseE logarithm of all pixels values.

f(x) = logarithm(x)

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="logarithm"></a>
## logarithm

Computes baseE logarithm of all pixels values.

f(x) = logarithm(x)

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="maskLabel"></a>
## maskLabel

Computes a masked image by applying a label mask to an image. All pixel values x of image X will be copied
to the destination image in case pixel value m at the same position in the label_map image has the right index value i.

f(x,m,i) = (x if (m == i); (0 otherwise))

Parameters (macro):
Image source, Image label_map, Image destination, Number label_index

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, Float arg4

<a name="maskStackWithPlane"></a>
## maskStackWithPlane'

Computes a masked image by applying a 2D mask to an image stack. All pixel values x of image X will be copied
to the destination image in case pixel value m at the same spatial position in the mask image is not equal to 
zero.

<pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>

Parameters (macro):
Image source, Image mask, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="maskStackWithPlane"></a>
## maskStackWithPlane'

Computes a masked image by applying a 2D mask to an image stack. All pixel values x of image X will be copied
to the destination image in case pixel value m at the same spatial position in the mask image is not equal to 
zero.

<pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>

Parameters (macro):
Image source, Image mask, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="mask"></a>
## mask'

Computes a masked image by applying a mask to an image. All pixel values x of image X will be copied
to the destination image in case pixel value m at the same position in the mask image is not equal to 
zero.

<pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>

Parameters (macro):
Image source, Image mask, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="mask"></a>
## mask'

Computes a masked image by applying a mask to an image. All pixel values x of image X will be copied
to the destination image in case pixel value m at the same position in the mask image is not equal to 
zero.

<pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>

Parameters (macro):
Image source, Image mask, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

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
ClearCLImage arg1, ClearCLImage arg2, int arg3, int arg4, int arg5

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

<a name="maximumIJ"></a>
## maximumIJ'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3

<a name="maximumImageAndScalar"></a>
## maximumImageAndScalar'

Computes the maximum of a constant scalar s and each pixel value x in a given image X.

<pre>f(x, s) = max(x, s)</pre>

Parameters (macro):
Image source, Image destination, Number scalar

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Float arg3

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
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="maximumImages"></a>
## maximumImages'

Computes the maximum of a pair of pixel values x, y from two given images X and Y.

<pre>f(x, y) = max(x, y)</pre>

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="maximumOfAllPixels"></a>
## maximumOfAllPixels'

Determines the maximum of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Max'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer arg1

<a name="maximumOfAllPixels"></a>
## maximumOfAllPixels'

Determines the maximum of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Max'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLImage arg1

<a name="maximumOfMaskedPixels"></a>
## maximumOfMaskedPixels

Determines the maximum intensity in an image, but only in pixels which have non-zero values in another mask image.

Parameters (macro):
Image source, Image mask

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="maximumSliceBySliceSphere"></a>
## maximumSliceBySliceSphere'

Computes the local maximum of a pixels ellipsoidal 2D neighborhood in an image stack 
slice by slice. The ellipses size is specified by its half-width and half-height (radius).

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="maximumSliceBySliceSphere"></a>
## maximumSliceBySliceSphere'

Computes the local maximum of a pixels ellipsoidal 2D neighborhood in an image stack 
slice by slice. The ellipses size is specified by its half-width and half-height (radius).

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

<a name="maximumSphere"></a>
## maximumSphere'

Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5

<a name="maximumSphere"></a>
## maximumSphere'

Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

<a name="maximumSphere"></a>
## maximumSphere'

Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

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
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5

<a name="maximumZProjection"></a>
## maximumZProjection'

Determines the maximum projection of an image along Z.

Parameters (macro):
Image source, Image destination_max

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="maximumZProjection"></a>
## maximumZProjection'

Determines the maximum projection of an image along Z.

Parameters (macro):
Image source, Image destination_max

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="meanBox"></a>
## meanBox'

Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, int arg3, int arg4, int arg5

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

<a name="meanIJ"></a>
## meanIJ'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3

<a name="meanOfMaskedPixels"></a>
## meanOfMaskedPixels

Determines the mean intensity in an image, but only in pixels which have non-zero values in another binary mask image.

Parameters (macro):
Image source, Image mask

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="meanSliceBySliceSphere"></a>
## meanSliceBySliceSphere'

Computes the local mean average of a pixels ellipsoidal 2D neighborhood in an image stack 
slice by slice. The ellipses size is specified by its half-width and half-height (radius).

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="meanSliceBySliceSphere"></a>
## meanSliceBySliceSphere'

Computes the local mean average of a pixels ellipsoidal 2D neighborhood in an image stack 
slice by slice. The ellipses size is specified by its half-width and half-height (radius).

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

<a name="meanSphere"></a>
## meanSphere'

Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

<a name="meanSphere"></a>
## meanSphere'

Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5

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
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="meanZProjection"></a>
## meanZProjection'

Determines the mean average projection of an image along Z.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="meanZProjection"></a>
## meanZProjection'

Determines the mean average projection of an image along Z.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="medianBox"></a>
## medianBox'

Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
its half-width and half-height (radius).

For technical reasons, the area of the rectangle must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5

<a name="medianBox"></a>
## medianBox'

Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
its half-width and half-height (radius).

For technical reasons, the area of the rectangle must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="medianBox"></a>
## medianBox'

Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
its half-width and half-height (radius).

For technical reasons, the area of the rectangle must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

<a name="medianBox"></a>
## medianBox'

Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
its half-width and half-height (radius).

For technical reasons, the area of the rectangle must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="medianSliceBySliceBox"></a>
## medianSliceBySliceBox'

Computes the local median of a pixels rectangular neighborhood. This is done slice-by-slice in a 3D 
image stack. The rectangle is specified by its half-width and half-height (radius).

For technical reasons, the area of the rectangle must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

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

<a name="medianSliceBySliceSphere"></a>
## medianSliceBySliceSphere'

Computes the local median of a pixels ellipsoidal neighborhood. This is done slice-by-slice in a 3D 
image stack. The ellipses size is specified by its half-width and half-height (radius).

For technical reasons, the area of the ellipse must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

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

<a name="medianSphere"></a>
## medianSphere'

Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
its half-width and half-height (radius).

For technical reasons, the area of the rectangle must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

<a name="medianSphere"></a>
## medianSphere'

Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
its half-width and half-height (radius).

For technical reasons, the area of the rectangle must have less than 1000 pixels.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5

<a name="minimumBox"></a>
## minimumBox'

Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, int arg3, int arg4, int arg5

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

<a name="minimumIJ"></a>
## minimumIJ'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3

<a name="minimumImageAndScalar"></a>
## minimumImageAndScalar'

Computes the maximum of a constant scalar s and each pixel value x in a given image X.

<pre>f(x, s) = min(x, s)</pre>

Parameters (macro):
Image source, Image destination, Number scalar

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3

<a name="minimumImageAndScalar"></a>
## minimumImageAndScalar'

Computes the maximum of a constant scalar s and each pixel value x in a given image X.

<pre>f(x, s) = min(x, s)</pre>

Parameters (macro):
Image source, Image destination, Number scalar

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Float arg3

<a name="minimumImages"></a>
## minimumImages'

Computes the minimum of a pair of pixel values x, y from two given images X and Y.

<pre>f(x, y) = min(x, y)</pre>

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="minimumImages"></a>
## minimumImages'

Computes the minimum of a pair of pixel values x, y from two given images X and Y.

<pre>f(x, y) = min(x, y)</pre>

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="minimumOfAllPixels"></a>
## minimumOfAllPixels'

Determines the minimum of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Min'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer arg1

<a name="minimumOfAllPixels"></a>
## minimumOfAllPixels'

Determines the minimum of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Min'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLImage arg1

<a name="minimumOfMaskedPixels"></a>
## minimumOfMaskedPixels

Determines the minimum intensity in an image, but only in pixels which have non-zero values in another mask image.

Parameters (macro):
Image source, Image mask

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="minimumSliceBySliceSphere"></a>
## minimumSliceBySliceSphere'

Computes the local minimum of a pixels ellipsoidal 2D neighborhood in an image stack 
slice by slice. The ellipses size is specified by its half-width and half-height (radius).

This filter is applied slice by slice in 2D.

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

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
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="minimumSphere"></a>
## minimumSphere'

Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

Parameters (macro):
Image source, Image destination, Number radiusX, Number radiusY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

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
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5

<a name="minimumZProjection"></a>
## minimumZProjection'

Determines the minimum projection of an image along Z.

Parameters (macro):
Image source, Image destination_sum

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="minimumZProjection"></a>
## minimumZProjection'

Determines the minimum projection of an image along Z.

Parameters (macro):
Image source, Image destination_sum

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="multiplyImageAndCoordinate"></a>
## multiplyImageAndCoordinate'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3

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

<a name="multiplyImageAndScalar"></a>
## multiplyImageAndScalar'

Multiplies all pixels value x in a given image X with a constant scalar s.

<pre>f(x, s) = x * s</pre>

Parameters (macro):
Image source, Image destination, Number scalar

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Float arg3

<a name="multiplyImages"></a>
## multiplyImages'

Multiplies all pairs of pixel values x and y from two image X and Y.

<pre>f(x, y) = x * y</pre>

Parameters (macro):
Image factor1, Image factor2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="multiplyImages"></a>
## multiplyImages'

Multiplies all pairs of pixel values x and y from two image X and Y.

<pre>f(x, y) = x * y</pre>

Parameters (macro):
Image factor1, Image factor2, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="multiplyMatrix"></a>
## multiplyMatrix

Multiplies two matrices with each other.

Parameters (macro):
Image matrix1, Image matrix2, Image matrix_destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="multiplySliceBySliceWithScalars"></a>
## multiplySliceBySliceWithScalars'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3

<a name="multiplySliceBySliceWithScalars"></a>
## multiplySliceBySliceWithScalars'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, float[] arg3

<a name="multiplyStackWithPlane"></a>
## multiplyStackWithPlane'

Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. x and y are at 
the same spatial position within a plane.

<pre>f(x, y) = x * y</pre>

Parameters (macro):
Image sourceStack, Image sourcePlane, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="multiplyStackWithPlane"></a>
## multiplyStackWithPlane'

Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. x and y are at 
the same spatial position within a plane.

<pre>f(x, y) = x * y</pre>

Parameters (macro):
Image sourceStack, Image sourcePlane, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="nonzeroMinimum3DDiamond"></a>
## nonzeroMinimum3DDiamond

Apply a minimum-sphere filter to the input image. The radius is fixed to 1.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="nonzeroMinimumBox"></a>
## nonzeroMinimumBox

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, int arg4, int arg5, int arg6

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
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="particleImageVelocimetry2D"></a>
## particleImageVelocimetry2D

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4, Integer arg5

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
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

<a name="paste"></a>
## paste

Pastes an image into another image at a given position.

Parameters (macro):
Image source, Image destination, Number destinationX, Number destinationY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

<a name="paste"></a>
## paste

Pastes an image into another image at a given position.

Parameters (macro):
Image source, Image destination, Number destinationX, Number destinationY

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5

<a name="paste"></a>
## paste

Pastes an image into another image at a given position.

Parameters (macro):
Image source, Image destination, Number destinationX, Number destinationY

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="powerImages"></a>
## powerImages

Calculates x to the power of y pixel wise of two images X and Y.

Parameters (macro):
Image input, Image exponent, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="power"></a>
## power'

Computes all pixels value x to the power of a given exponent a.

<pre>f(x, a) = x ^ a</pre>

Parameters (macro):
Image source, Image destination, Number exponent

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Float arg3

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

<a name="presign"></a>
## presign

Determines the extrema of pixel values: f(x) = x / abs(x).

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="radialProjection"></a>
## radialProjection'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Float arg3

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
ClearCLBuffer arg1, String arg2

<a name="readRawImageFromDisc"></a>
## readRawImageFromDisc

Reads a raw file from disc and pushes it immediately to the GPU.

Parameters (macro):
Image destination, String filename, Number width, Number height, Number depth, Number bitsPerPixel

Parameters (Java):
String arg1, Integer arg2, Integer arg3, Integer arg4, Integer arg5

<a name="replaceIntensity"></a>
## replaceIntensity

Replaces a specific intensity in an image with a given new value.

Parameters (macro):
Image input, Image destination, Number oldValue, number newValue

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4

<a name="replace"></a>
## replace

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="resliceBottom"></a>
## resliceBottom'

Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
offers less flexibility such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="resliceBottom"></a>
## resliceBottom'

Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
offers less flexibility such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="resliceLeft"></a>
## resliceLeft'

Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
 but offers less flexibility such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="resliceLeft"></a>
## resliceLeft'

Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
 but offers less flexibility such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="resliceRight"></a>
## resliceRight'

Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
 but offers less flexibility such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="resliceRight"></a>
## resliceRight'

Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
 but offers less flexibility such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="resliceTop"></a>
## resliceTop'

Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
offers less flexibility such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="resliceTop"></a>
## resliceTop'

Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
offers less flexibility such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="rotateLeft"></a>
## rotateLeft'

Rotates a given input image by 90 degrees counter-clockwise. For that, X and Y axis of an image stack
are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="rotateLeft"></a>
## rotateLeft'

Rotates a given input image by 90 degrees counter-clockwise. For that, X and Y axis of an image stack
are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="rotateRight"></a>
## rotateRight'

Rotates a given input image by 90 degrees clockwise. For that, X and Y axis of an image stack
are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="rotateRight"></a>
## rotateRight'

Rotates a given input image by 90 degrees clockwise. For that, X and Y axis of an image stack
are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
such as interpolation.

Parameters (macro):
Image source, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="setNonZeroPixelsToPixelIndex"></a>
## setNonZeroPixelsToPixelIndex

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="setWhereXequalsY"></a>
## setWhereXequalsY

Sets all pixel values a of a given image A to a constant value v in case its coordinates x == y. Otherwise the pixel is not overwritten.
If you want to initialize an identity transfrom matrix, set all pixels to 0 first.

<pre>f(a) = v</pre>

Parameters (macro):
Image source, Number value

Parameters (Java):
ClearCLImage arg1, Float arg2

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

<a name="set"></a>
## set'

Sets all pixel values x of a given image X to a constant value v.

<pre>f(x) = v</pre>

Parameters (macro):
Image source, Number value

Parameters (Java):
ClearCLImage arg1, Float arg2

<a name="shiftIntensitiesToCloseGaps"></a>
## shiftIntensitiesToCloseGaps

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="shortestDistances"></a>
## shortestDistances

Determine the shortest distance from a distance matrix. This corresponds to the minimum in a matrix for each individial column.

Parameters (macro):
Image distance_matrix, Image destination_minimum_distances

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

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
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="smaller"></a>
## smaller

Determines if two images A and B smaller pixel wise.

f(a, b) = 1 if a < b; 0 otherwise. 

Parameters (macro):
Image source1, Image source2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

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

<a name="splitStack"></a>
## splitStack'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage[] arg2

<a name="spotsToPointList"></a>
## spotsToPointList

Transforms a spots image as resulting from maxim detection in an image where every column cotains d 
pixels (with d = dimensionality of the original image) with the coordinates of the maxima.

Parameters (macro):
Image input_spots, Image destination_pointlist

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="stackToTiles"></a>
## stackToTiles

Stack to tiles.

Parameters (macro):
Image source, Image destination, Number tiles_x, Number tiles_y

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4

<a name="stackToTiles"></a>
## stackToTiles

Stack to tiles.

Parameters (macro):
Image source, Image destination, Number tiles_x, Number tiles_y

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4

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
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="subtractImages"></a>
## subtractImages'

Subtracts one image X from another image Y pixel wise.

<pre>f(x, y) = x - y</pre>

Parameters (macro):
Image subtrahend, Image minuend, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="subtract"></a>
## subtract'

Subtracts one image X from another image Y pixel wise.

<pre>f(x, y) = x - y</pre>

Parameters (macro):
Image subtrahend, Image minuend, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="subtract"></a>
## subtract'

Subtracts one image X from another image Y pixel wise.

<pre>f(x, y) = x - y</pre>

Parameters (macro):
Image subtrahend, Image minuend, Image destination

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3

<a name="sumPixelsSliceBySlice"></a>
## sumPixelsSliceBySlice'

null

Parameters (macro):
null

Parameters (Java):
ClearCLBuffer arg1

<a name="sumPixelsSliceBySlice"></a>
## sumPixelsSliceBySlice'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1

<a name="sumPixels"></a>
## sumPixels'

Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Sum'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLBuffer arg1

<a name="sumPixels"></a>
## sumPixels'

Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Sum'.

Parameters (macro):
Image source

Parameters (Java):
ClearCLImage arg1

<a name="sumZProjection"></a>
## sumZProjection'

Determines the sum projection of an image along Z.

Parameters (macro):
Image source, Image destination_sum

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="sumZProjection"></a>
## sumZProjection'

Determines the sum projection of an image along Z.

Parameters (macro):
Image source, Image destination_sum

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="tenengradFusion"></a>
## tenengradFusion'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, float[] arg2, float arg3, ClearCLImage[] arg4

<a name="tenengradFusion"></a>
## tenengradFusion'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, float[] arg2, ClearCLImage[] arg3

<a name="tenengradWeightsSliceBySlice"></a>
## tenengradWeightsSliceBySlice'

null

Parameters (macro):
null

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2

<a name="threshold"></a>
## threshold'

Computes a binary image with pixel values 0 and 1. All pixel values x of a given input image with 
value larger or equal to a given threshold t will be set to 1.

f(x,t) = (1 if (x >= t); (0 otherwise))

This plugin is comparable to setting a raw threshold in ImageJ and using the 'Convert to Mask' menu.

Parameters (macro):
Image source, Image destination, Number threshold

Parameters (Java):
ClearCLImage arg1, ClearCLImage arg2, Float arg3

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

<a name="translationRegistration"></a>
## translationRegistration

Measures center of mass of thresholded objects in the two input images and translates the second image so that it better fits to the first image.

Parameters (macro):
Image input1, Image input2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, double[] arg3

<a name="translationRegistration"></a>
## translationRegistration

Measures center of mass of thresholded objects in the two input images and translates the second image so that it better fits to the first image.

Parameters (macro):
Image input1, Image input2, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3

<a name="translationTimelapseRegistration"></a>
## translationTimelapseRegistration

Applies 2D translation registration to every pair of t, t+1 slices of a 2D+t image stack.

Parameters (macro):
Image input, Image output

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="transposeXY"></a>
## transposeXY

Transpose X and Y in an image.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="transposeXZ"></a>
## transposeXZ

Transpose X and Z in an image.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="transposeYZ"></a>
## transposeYZ

Transpose Y and Z in an image.

Parameters (macro):
Image input, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

<a name="writeValuesToPositions"></a>
## writeValuesToPositions

Takes an image with three/four rows (2D: height = 3; 3D: height = 4): x, y [, z] and v and target image. The value v will be written at position x/y[/z] in the target image.

Parameters (macro):
Image positionsAndValues, Image destination

Parameters (Java):
ClearCLBuffer arg1, ClearCLBuffer arg2

