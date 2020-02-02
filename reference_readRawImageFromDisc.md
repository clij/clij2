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
int arg2 = 10;
int arg3 = 20;
int arg4 = 30;
int arg5 = 40;
```

```
// Execute operation on GPU
ClearCLBuffer resultReadRawImageFromDisc = clij2.readRawImageFromDisc(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result
System.out.println(resultReadRawImageFromDisc);

// cleanup memory on GPU
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
