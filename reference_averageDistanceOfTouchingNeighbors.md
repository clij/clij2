## averageDistanceOfTouchingNeighbors
![Image](images/mini_clijx_logo.png)

Takes a touch matrix and a distance matrix to determine the average distance of touching neighbors for every object.

### Usage in ImageJ macro
```
Ext.CLIJx_averageDistanceOfTouchingNeighbors(Image distance_matrix, Image touch_matrix, Image average_distancelist_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer distance_matrix = clijx.push(distance_matrixImagePlus);
ClearCLBuffer touch_matrix = clijx.push(touch_matrixImagePlus);
average_distancelist_destination = clij.create(distance_matrix);
```

```
// Execute operation on GPU
clijx.averageDistanceOfTouchingNeighbors(clij, distance_matrix, touch_matrix, average_distancelist_destination);
```

```
//show result
average_distancelist_destinationImagePlus = clij.pull(average_distancelist_destination);
average_distancelist_destinationImagePlus.show();

// cleanup memory on GPU
distance_matrix.close();
touch_matrix.close();
average_distancelist_destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
