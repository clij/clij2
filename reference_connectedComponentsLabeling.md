## connectedComponentsLabeling
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Performs connected components analysis to a binary image and generates a label map.

### Usage in ImageJ macro
```
Ext.CLIJx_connectedComponentsLabeling(Image binary_input, Image labeling_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer binary_input = clij2.push(binary_inputImagePlus);
labeling_destination = clij.create(binary_input);
```

```
// Execute operation on GPU
clij2.connectedComponentsLabeling(clij, binary_input, labeling_destination);
```

```
//show result
labeling_destinationImagePlus = clij2.pull(labeling_destination);
labeling_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(binary_input);
clij2.release(labeling_destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [measure_area_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measure_area_per_label.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [measure_statistics.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measure_statistics.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [boundingBoxes.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/boundingBoxes.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [excludeLabelsOnEdges.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/excludeLabelsOnEdges.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [intensity_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/intensity_per_label.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [labeling.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/labeling.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [labeling_benchmarking.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/labeling_benchmarking.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [particle_analysis.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/particle_analysis.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [pullLabelsToROIManager.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/pullLabelsToROIManager.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [rotating_sphere.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/rotating_sphere.ijm)  
<a href="https://github.com/clij/clijpy/blob/master/python/"><img src="images/language_python.png" height="20"/></a> [benchmark_clijx_pull.ipynb](https://github.com/clij/clijpy/blob/master/python/benchmark_clijx_pull.ipynb)  
<a href="https://github.com/clij/clijpy/blob/master/python/"><img src="images/language_python.png" height="20"/></a> [clijpy_demo.ipynb](https://github.com/clij/clijpy/blob/master/python/clijpy_demo.ipynb)  
<a href="https://github.com/clij/clijpy/blob/master/python/"><img src="images/language_python.png" height="20"/></a> [spot_detection.py](https://github.com/clij/clijpy/blob/master/python/spot_detection.py)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [segmentation.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/segmentation.m)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [spotDetection3D.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/spotDetection3D.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
