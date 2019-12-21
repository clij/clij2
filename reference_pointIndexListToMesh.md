## pointIndexListToMesh
![Image](images/mini_clijx_logo.png)

Meshes all points in a given point list which are indiced in a corresponding index list. TODO: Explain better

### Usage in ImageJ macro
```
Ext.CLIJx_pointIndexListToMesh(Image pointlist, Image indexList, Image Mesh);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer pointlist = clijx.push(pointlistImagePlus);
ClearCLBuffer indexList = clijx.push(indexListImagePlus);
ClearCLBuffer Mesh = clijx.push(MeshImagePlus);
```

```
// Execute operation on GPU
clijx.pointIndexListToMesh(clij, pointlist, indexList, Mesh);
```

```
//show result

// cleanup memory on GPU
pointlist.close();
indexList.close();
Mesh.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
