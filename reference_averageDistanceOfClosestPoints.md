## averageDistanceOfClosestPoints
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Determine the n point indices with shortest distance for all points in a distance matrix.
This corresponds to the n row indices with minimum values for each column of the distance matrix.

### Usage in ImageJ macro
```
Ext.CLIJx_averageDistanceOfClosestPoints(Image distance_matrix, Image indexlist_destination, Number nClosestPointsTofind);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer distance_matrix = clij2.push(distance_matrixImagePlus);
indexlist_destination = clij.create(distance_matrix);
int nClosestPointsTofind = 10;
```

```
// Execute operation on GPU
clij2.averageDistanceOfClosestPoints(clij, distance_matrix, indexlist_destination, nClosestPointsTofind);
```

```
//show result
indexlist_destinationImagePlus = clij2.pull(indexlist_destination);
indexlist_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(distance_matrix);
clij2.release(indexlist_destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
