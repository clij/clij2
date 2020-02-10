## particleImageVelocimetry
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

For every pixel in source image 1, determine the pixel with the most similar intensity in 
 the local neighborhood with a given radius in source image 2. Write the distance in 
X and Y in the two corresponding destination images.

### Usage in ImageJ macro
```
Ext.CLIJx_particleImageVelocimetry(Image source1, Image source2, Image destinationDeltaX, Image destinationDeltaY, Image destinationDeltaZ, Number maxDeltaX, Number maxDeltaY, Number maxDeltaZ, Boolean correctLocalShift);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source1 = clij2.push(source1ImagePlus);
ClearCLBuffer source2 = clij2.push(source2ImagePlus);
destinationDeltaX = clij.create(source1);
destinationDeltaY = clij.create(source1);
destinationDeltaZ = clij.create(source1);
int maxDeltaX = 10;
int maxDeltaY = 20;
int maxDeltaZ = 30;
boolean correctLocalShift = true;
```

```
// Execute operation on GPU
clij2.particleImageVelocimetry(clij, source1, source2, destinationDeltaX, destinationDeltaY, destinationDeltaZ, maxDeltaX, maxDeltaY, maxDeltaZ, correctLocalShift);
```

```
//show result
destinationDeltaXImagePlus = clij2.pull(destinationDeltaX);
destinationDeltaXImagePlus.show();
destinationDeltaYImagePlus = clij2.pull(destinationDeltaY);
destinationDeltaYImagePlus.show();
destinationDeltaZImagePlus = clij2.pull(destinationDeltaZ);
destinationDeltaZImagePlus.show();

// cleanup memory on GPU
clij2.release(source1);
clij2.release(source2);
clij2.release(destinationDeltaX);
clij2.release(destinationDeltaY);
clij2.release(destinationDeltaZ);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [crossCorrelationPIV.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/crossCorrelationPIV.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
