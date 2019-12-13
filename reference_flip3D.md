## flip3D
![Image](images/mini_clijx_logo.png)

Flips an image in X, Y and/or Z direction depending on boolean flags.

### Usage in ImageJ macro
```
Ext.CLIJx_flip3D(Image source, Image destination, Boolean flipX, Boolean flipY, Boolean flipZ);
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
boolean arg3 = true;
boolean arg4 = false;
boolean arg5 = false;
```

```
// Execute operation on GPU
clijx.flip3D(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [flip.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/flip.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
