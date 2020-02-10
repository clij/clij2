## saveAsTIF
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Pulls an image from the GPU memory and saves it as TIF to disc.

### Usage in ImageJ macro
```
Ext.CLIJx_saveAsTIF(Image input, String filename);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer input = clij2.push(inputImagePlus);
```

```
// Execute operation on GPU
clij2.saveAsTIF(clij, input, filename);
```

```
//show result

// cleanup memory on GPU
clij2.release(input);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
