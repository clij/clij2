## setColumn
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Sets all pixel values x of a given column in X to a constant value v.

<pre>f(x) = v</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_setColumn(Image source, Number columnIndex, Number value);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer arg1 = clij2.push(arg1ImagePlus);
int arg2 = 10;
float arg3 = 1.0;
```

```
// Execute operation on GPU
clij2.setColumn(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
