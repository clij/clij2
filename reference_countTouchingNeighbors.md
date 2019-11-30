## countTouchingNeighbors
![Image](images/mini_clijx_logo.png)

Takes a touching-neighbors-matrix as input and delivers a vector with number of touching neighbors per label as a vector.

### Usage in ImageJ macro
```
Ext.CLIJx_countTouchingNeighbors(Image touch_matrix, Image touching_neighbors_count_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer touch_matrix = clijx.push(touch_matrixImagePlus);
touching_neighbors_count_destination = clij.create(touch_matrix);
```

```
// Execute operation on GPU
clijx.countTouchingNeighbors(clij, touch_matrix, touching_neighbors_count_destination);
```

```
//show result
touching_neighbors_count_destinationImagePlus = clij.pull(touching_neighbors_count_destination);
touching_neighbors_count_destinationImagePlus.show();

// cleanup memory on GPU
touch_matrix.close();
touching_neighbors_count_destination.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [countNeighbors3D.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/countNeighbors3D.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
