## writeVTKLineListToDisc
![Image](images/mini_clijx_logo.png)

Takes a point list image representing n points (n*2 for 2D points, n*3 for 3D points) and a corresponding touch matrix , sized (n+1)*(n+1), and exports them in VTK format.

### Usage in ImageJ macro
```
Ext.CLIJx_writeVTKLineListToDisc(Image pointlist, Image touch_matrix, String filename);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
ClearCLBuffer arg2 = clijx.push(arg2ImagePlus);
```

```
// Execute operation on GPU
clijx.writeVTKLineListToDisc(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
