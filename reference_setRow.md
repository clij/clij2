## setRow
![Image](images/mini_clijx_logo.png)

Sets all pixel values x of a given row in X to a constant value v.

<pre>f(x) = v</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_setRow(Image source, Number rowIndex, Number value);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
int arg2 = 10;
float arg3 = 1.0;
```

```
// Execute operation on GPU
clijx.setRow(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
arg1.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
