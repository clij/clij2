## minimumImages
![Image](images/mini_clijx_logo.png)

Computes the minimum of a pair of pixel values x, y from two given images X and Y.

<pre>f(x, y) = min(x, y)</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_minimumImages(Image source1, Image source2, Image destination);
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
```

```
// Execute operation on GPU
clijx.minimumImages(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
arg3.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
