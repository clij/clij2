## mask
![Image](images/mini_clij1_logo.png)

Computes a masked image by applying a mask to an image. All pixel values x of image X will be copied
to the destination image in case pixel value m at the same position in the mask image is not equal to 
zero.

<pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>

### Usage in ImageJ macro
```
Ext.CLIJ_mask(Image source, Image mask, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
ClearCLBuffer mask = clijx.push(maskImagePlus);
destination = clij.create(source);
```

```
// Execute operation on GPU
clijx.mask(clij, source, mask, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
source.close();
mask.close();
destination.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [intensity_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/intensity_per_label.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [countNeighbors3D.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/countNeighbors3D.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [statistics.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/statistics.py)  
<a href="https://github.com/clij/clijpy/blob/master/python/"><img src="images/language_python.png" height="20"/></a> [spot_detection.py](https://github.com/clij/clijpy/blob/master/python/spot_detection.py)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [spotDetection3D.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/spotDetection3D.m)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [statistics.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/statistics.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
