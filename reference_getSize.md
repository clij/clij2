## getSize
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Reads out the size of an image [stack] and writes it to the results table in the columns 'Width', 'Height' and 'Depth'.

### Usage in ImageJ macro
```
Ext.CLIJx_getSize(Image source);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
```

```
// Execute operation on GPU
long[] resultGetSize = clij2.getSize(clij, source);
```

```
//show result
System.out.println(resultGetSize);

// cleanup memory on GPU
clij2.release(source);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [virtual_ablation.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/virtual_ablation.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [allocateBig2DImages.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/allocateBig2DImages.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [getsize.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/getsize.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [mesh_closest_points.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mesh_closest_points.ijm)  
<a href="https://github.com/clij/clijpy/blob/master/python/"><img src="images/language_python.png" height="20"/></a> [spot_detection.py](https://github.com/clij/clijpy/blob/master/python/spot_detection.py)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [backgroundSubtractionMaximumProjection.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/backgroundSubtractionMaximumProjection.m)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [maximumProjection.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/maximumProjection.m)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [reslice.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/reslice.m)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [spotDetection3D.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/spotDetection3D.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
