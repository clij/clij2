## power
![Image](images/mini_clijx_logo.png)

Computes all pixels value x to the power of a given exponent a.

<pre>f(x, a) = x ^ a</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_power(Image source, Image destination, Number exponent);
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
float arg3 = 1.0;
```

```
// Execute operation on GPU
clijx.power(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
