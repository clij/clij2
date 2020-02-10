## generateDistanceMatrix
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Takes two images containing coordinates and builds up a matrix containing distance between the points. Convention: image width represents number of points, height represents dimensionality (2D, 3D, ... 10D). The result image has width the first input image and height equals to the width of the second input image.

### Usage in ImageJ macro
```
Ext.CLIJx_generateDistanceMatrix(Image coordinate_list1, Image coordinate_list2, Image distance_matrix_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer coordinate_list1 = clij2.push(coordinate_list1ImagePlus);
ClearCLBuffer coordinate_list2 = clij2.push(coordinate_list2ImagePlus);
distance_matrix_destination = clij.create(coordinate_list1);
```

```
// Execute operation on GPU
clij2.generateDistanceMatrix(clij, coordinate_list1, coordinate_list2, distance_matrix_destination);
```

```
//show result
distance_matrix_destinationImagePlus = clij2.pull(distance_matrix_destination);
distance_matrix_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(coordinate_list1);
clij2.release(coordinate_list2);
clij2.release(distance_matrix_destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [mesh_closest_points.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mesh_closest_points.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [spot_distance_measurement.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/spot_distance_measurement.ijm)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [meshClosestPoints.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/meshClosestPoints.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
