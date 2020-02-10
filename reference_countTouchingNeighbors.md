## countTouchingNeighbors
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Takes a touching-neighbors-matrix as input and delivers a vector with number of touching neighbors per label as a vector.

### Usage in ImageJ macro
```
Ext.CLIJx_countTouchingNeighbors(Image touch_matrix, Image touching_neighbors_count_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer touch_matrix = clij2.push(touch_matrixImagePlus);
touching_neighbors_count_destination = clij.create(touch_matrix);
```

```
// Execute operation on GPU
clij2.countTouchingNeighbors(clij, touch_matrix, touching_neighbors_count_destination);
```

```
//show result
touching_neighbors_count_destinationImagePlus = clij2.pull(touching_neighbors_count_destination);
touching_neighbors_count_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(touch_matrix);
clij2.release(touching_neighbors_count_destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
