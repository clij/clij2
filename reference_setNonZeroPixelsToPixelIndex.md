## setNonZeroPixelsToPixelIndex
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Sets all pixels in an image which are not zero to the index of the pixel. This can be used for Connected Components Analysis.

### Usage in ImageJ macro
```
Ext.CLIJx_setNonZeroPixelsToPixelIndex(Image source, Image destination);
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
```

```
// Execute operation on GPU
clij2.setNonZeroPixelsToPixelIndex(clij, arg1, arg2);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
