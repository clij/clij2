## applyVectorfield2D
![Image](images/mini_clijx_logo.png)

Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 

### Usage in ImageJ macro
```
Ext.CLIJx_applyVectorfield2D(Image source, Image vectorX, Image vectorY, Image destination);
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
ClearCLBuffer arg3 = clijx.push(arg3ImagePlus);
ClearCLBuffer arg4 = clijx.push(arg4ImagePlus);
```

```
// Execute operation on GPU
clijx.applyVectorfield2D(clij, arg1, arg2, arg3, arg4);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
arg3.close();
arg4.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
