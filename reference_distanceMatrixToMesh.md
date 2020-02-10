## distanceMatrixToMesh
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a distance matrix of size n*n to draw lines from all points to points if the corresponding pixel in the distance matrix is smaller than a given distance threshold.

### Usage in ImageJ macro
```
Ext.CLIJx_distanceMatrixToMesh(Image pointlist, Image distance_matrix, Image mesh_destination, Number maximumDistance);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer pointlist = clij2.push(pointlistImagePlus);
ClearCLBuffer distance_matrix = clij2.push(distance_matrixImagePlus);
mesh_destination = clij.create(pointlist);
float maximumDistance = 1.0;
```

```
// Execute operation on GPU
clij2.distanceMatrixToMesh(clij, pointlist, distance_matrix, mesh_destination, maximumDistance);
```

```
//show result
mesh_destinationImagePlus = clij2.pull(mesh_destination);
mesh_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(pointlist);
clij2.release(distance_matrix);
clij2.release(mesh_destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
