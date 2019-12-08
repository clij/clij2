## readRawImageFromDisc
![Image](images/mini_clijx_logo.png)

Reads a raw file from disc and pushes it immediately to the GPU.

### Usage in ImageJ macro
```
Ext.CLIJx_readRawImageFromDisc(Image destination, String filename, Number width, Number height, Number depth, Number bitsPerPixel);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
int arg2 = 10;
int arg3 = 20;
int arg4 = 30;
int arg5 = 40;
```

```
// Execute operation on GPU
ClearCLBuffer resultReadRawImageFromDisc = clijx.readRawImageFromDisc(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result
System.out.println(resultReadRawImageFromDisc);

// cleanup memory on GPU
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
