## writeVTKLineListToDisc
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Takes a point list image representing n points (n*2 for 2D points, n*3 for 3D points) and a corresponding touch matrix , sized (n+1)*(n+1), and exports them in VTK format.

### Usage in ImageJ macro
```
Ext.CLIJx_writeVTKLineListToDisc(Image pointlist, Image touch_matrix, String filename);
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
```

```
// Execute operation on GPU
clij2.writeVTKLineListToDisc(clij, pointlist, touch_matrix, filename);
```

```
//show result

// cleanup memory on GPU
clij2.release(pointlist);
clij2.release(touch_matrix);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
