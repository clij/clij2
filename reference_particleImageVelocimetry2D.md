## particleImageVelocimetry2D
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)![Image](images/mini_clijx_logo.png)

null

### Usage in ImageJ macro
```
Ext.CLIJx_particleImageVelocimetry2D(null);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer arg1 = clij2.push(arg1ImagePlus);
ClearCLBuffer arg2 = clij2.push(arg2ImagePlus);
ClearCLBuffer arg3 = clij2.push(arg3ImagePlus);
ClearCLBuffer arg4 = clij2.push(arg4ImagePlus);
int arg5 = 10;
```

```
// Execute operation on GPU
clij2.particleImageVelocimetry2D(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
clij2.release(arg3);
clij2.release(arg4);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [crossCorrelationPIV.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/crossCorrelationPIV.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
