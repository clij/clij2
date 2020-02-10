## setPlane
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Sets all pixel values x of a given plane in X to a constant value v.

<pre>f(x) = v</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_setPlane(Image source, Number rowIndex, Number value);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
int rowIndex = 10;
float value = 1.0;
```

```
// Execute operation on GPU
clij2.setPlane(clij, source, rowIndex, value);
```

```
//show result

// cleanup memory on GPU
clij2.release(source);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
