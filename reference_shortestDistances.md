## shortestDistances
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Determine the shortest distance from a distance matrix. This corresponds to the minimum in a matrix for each individial column.

### Usage in ImageJ macro
```
Ext.CLIJx_shortestDistances(Image distance_matrix, Image destination_minimum_distances);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer distance_matrix = clij2.push(distance_matrixImagePlus);
destination_minimum_distances = clij.create(distance_matrix);
```

```
// Execute operation on GPU
clij2.shortestDistances(clij, distance_matrix, destination_minimum_distances);
```

```
//show result
destination_minimum_distancesImagePlus = clij2.pull(destination_minimum_distances);
destination_minimum_distancesImagePlus.show();

// cleanup memory on GPU
clij2.release(distance_matrix);
clij2.release(destination_minimum_distances);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [spot_distance_measurement.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/spot_distance_measurement.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
