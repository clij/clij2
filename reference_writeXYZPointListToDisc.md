## writeXYZPointListToDisc
![Image](images/mini_clijx_logo.png)

Takes a point list image representing n points (n*2 for 2D points, n*3 for 3D points) and exports them in XYZ format.

### Usage in ImageJ macro
```
Ext.CLIJx_writeXYZPointListToDisc(Image pointlist, String filename);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
```

```
// Execute operation on GPU
clijx.writeXYZPointListToDisc(clij, arg1, arg2);
```

```
//show result

// cleanup memory on GPU
arg1.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
