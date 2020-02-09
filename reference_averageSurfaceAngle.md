## averageSurfaceAngle
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Takes a pointlist and a touch matrix to determine the average angle of adjacent triangles in a surface mesh. For every point, the average angle of adjacent triangles is saved.

### Usage in ImageJ macro
```
Ext.CLIJx_averageSurfaceAngle(Image pointlist, Image touch_matrix, Image average_distancelist_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer pointlist = clij2.push(pointlistImagePlus);
ClearCLBuffer touch_matrix = clij2.push(touch_matrixImagePlus);
average_distancelist_destination = clij.create(pointlist);
```

```
// Execute operation on GPU
clij2.averageSurfaceAngle(clij, pointlist, touch_matrix, average_distancelist_destination);
```

```
//show result
average_distancelist_destinationImagePlus = clij2.pull(average_distancelist_destination);
average_distancelist_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(pointlist);
clij2.release(touch_matrix);
clij2.release(average_distancelist_destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
