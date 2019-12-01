## touchMatrixToMesh
![Image](images/mini_clijx_logo.png)

Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a touch matrix of size n*n to draw lines from all points to points if the corresponding pixel in the touch matrix is 1.

### Usage in ImageJ macro
```
Ext.CLIJx_touchMatrixToMesh(Image pointlist, Image touch_matrix, Image mesh_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer pointlist = clijx.push(pointlistImagePlus);
ClearCLBuffer touch_matrix = clijx.push(touch_matrixImagePlus);
mesh_destination = clij.create(pointlist);
```

```
// Execute operation on GPU
clijx.touchMatrixToMesh(clij, pointlist, touch_matrix, mesh_destination);
```

```
//show result
mesh_destinationImagePlus = clij.pull(mesh_destination);
mesh_destinationImagePlus.show();

// cleanup memory on GPU
pointlist.close();
touch_matrix.close();
mesh_destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
