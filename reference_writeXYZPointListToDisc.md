## writeXYZPointListToDisc
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Takes a point list image representing n points (n*2 for 2D points, n*3 for 3D points) and exports them in XYZ format.

### Usage in ImageJ macro
```
Ext.CLIJx_writeXYZPointListToDisc(Image pointlist, String filename);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer pointlist = clij2.push(pointlistImagePlus);
```

```
// Execute operation on GPU
clij2.writeXYZPointListToDisc(clij, pointlist, filename);
```

```
//show result

// cleanup memory on GPU
clij2.release(pointlist);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
