## averageDistanceOfTouchingNeighbors
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Takes a touch matrix and a distance matrix to determine the average distance of touching neighbors for every object.

### Usage in ImageJ macro
```
Ext.CLIJx_averageDistanceOfTouchingNeighbors(Image distance_matrix, Image touch_matrix, Image average_distancelist_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer distance_matrix = clij2.push(distance_matrixImagePlus);
ClearCLBuffer touch_matrix = clij2.push(touch_matrixImagePlus);
average_distancelist_destination = clij.create(distance_matrix);
```

```
// Execute operation on GPU
clij2.averageDistanceOfTouchingNeighbors(clij, distance_matrix, touch_matrix, average_distancelist_destination);
```

```
//show result
average_distancelist_destinationImagePlus = clij2.pull(average_distancelist_destination);
average_distancelist_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(distance_matrix);
clij2.release(touch_matrix);
clij2.release(average_distancelist_destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
