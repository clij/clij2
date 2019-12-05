## detectMaximaBox
![Image](images/mini_clij1_logo.png)

Detects local maxima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
there is no other pixel in a given radius which has a higher intensity, and to 0 otherwise.

### Usage in ImageJ macro
```
Ext.CLIJ_detectMaximaBox(Image source, Image destination, Number radius);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
ClearCLBuffer arg2 = clijx.push(arg2ImagePlus);
int arg3 = 10;
```

```
// Execute operation on GPU
clijx.detectMaximaBox(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [mesh_closest_points.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mesh_closest_points.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [spot_distance_measurement.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/spot_distance_measurement.ijm)  
<a href="https://github.com/clij/clijpy/blob/master/python/"><img src="images/language_python.png" height="20"/></a> [spot_detection.py](https://github.com/clij/clijpy/blob/master/python/spot_detection.py)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [meshClosestPoints.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/meshClosestPoints.m)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [spotDetection3D.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/spotDetection3D.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
