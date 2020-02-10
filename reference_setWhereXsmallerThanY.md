## setWhereXsmallerThanY
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Sets all pixel values a of a given image A to a constant value v in case its coordinates x < y. Otherwise the pixel is not overwritten.
If you want to initialize an identity transfrom matrix, set all pixels to 0 first.

<pre>f(a) = v</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_setWhereXsmallerThanY(Image source, Number value);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
float value = 1.0;
```

```
// Execute operation on GPU
clij2.setWhereXsmallerThanY(clij, source, value);
```

```
//show result

// cleanup memory on GPU
clij2.release(source);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
