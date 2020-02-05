## readRawImageFromDisc
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)![Image](images/mini_clijx_logo.png)

Reads a raw file from disc and pushes it immediately to the GPU.

### Usage in ImageJ macro
```
Ext.CLIJx_readRawImageFromDisc(Image destination, String filename, Number width, Number height, Number depth, Number bitsPerPixel);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer arg1 = clij2.push(arg1ImagePlus);
```

```
// Execute operation on GPU
clij2.readRawImageFromDisc(clij, arg1, arg2);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
