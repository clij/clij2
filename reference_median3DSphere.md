## median3DSphere
![Image](images/mini_clijx_logo.png)

Computes the local median of a pixels spherical neighborhood. The spheres size is specified by 
its half-width, half-height and half-depth (radius).

For technical reasons, the volume of the sphere must contain less than 1000 voxels.

### Usage in ImageJ macro
```
Ext.CLIJx_median3DSphere(Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ);
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
int arg3 = 10;
int arg4 = 20;
int arg5 = 30;
```

```
// Execute operation on GPU
clijx.median3DSphere(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)