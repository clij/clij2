## setWhereXequalsY
![Image](images/mini_clijx_logo.png)

Sets all pixel values a of a given image A to a constant value v in case its coordinates x == y. Otherwise the pixel is not overwritten.
If you want to initialize an identity transfrom matrix, set all pixels to 0 first.

<pre>f(a) = v</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_setWhereXequalsY(Image source, Number value);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
float arg2 = 1.0;
```

```
// Execute operation on GPU
clijx.setWhereXequalsY(clij, arg1, arg2);
```

```
//show result

// cleanup memory on GPU
arg1.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
