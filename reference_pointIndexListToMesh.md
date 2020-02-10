## pointIndexListToMesh
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Meshes all points in a given point list which are indiced in a corresponding index list. TODO: Explain better

### Usage in ImageJ macro
```
Ext.CLIJx_pointIndexListToMesh(Image pointlist, Image indexList, Image Mesh);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer pointlist = clij2.push(pointlistImagePlus);
ClearCLBuffer indexList = clij2.push(indexListImagePlus);
ClearCLBuffer Mesh = clij2.push(MeshImagePlus);
```

```
// Execute operation on GPU
clij2.pointIndexListToMesh(clij, pointlist, indexList, Mesh);
```

```
//show result

// cleanup memory on GPU
clij2.release(pointlist);
clij2.release(indexList);
clij2.release(Mesh);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
