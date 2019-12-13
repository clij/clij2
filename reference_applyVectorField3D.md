## applyVectorField3D
![Image](images/mini_clijx_logo.png)

Image source, Image vectorX, Image vectorY, Image vectorZ, Image destination

### Usage in ImageJ macro
```
Ext.CLIJx_applyVectorField3D(Deforms an image stack according to distances provided in the given vector image stacks. It is recommended to use 32-bit image stacks for input, output and vector image stacks. );
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
ClearCLBuffer arg5 = clijx.push(arg5ImagePlus);
```

```
// Execute operation on GPU
clijx.applyVectorField3D(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
arg3.close();
arg4.close();
arg5.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
