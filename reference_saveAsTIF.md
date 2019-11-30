## saveAsTIF
![Image](images/mini_clijx_logo.png)

Pulls an image from the GPU memory and saves it as TIF to disc.

### Usage in ImageJ macro
```
Ext.CLIJx_saveAsTIF(Image input, String filename);
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
clijx.saveAsTIF(clij, arg1, arg2);
```

```
//show result

// cleanup memory on GPU
arg1.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
