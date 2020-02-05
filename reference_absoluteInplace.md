## absoluteInplace
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)![Image](images/mini_clijx_logo.png)

Computes the absolute value of every individual pixel x in a given image.

<pre>f(x) = |x| </pre>

### Usage in ImageJ macro
```
Ext.CLIJx_absoluteInplace(Image source, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer arg1 = clij2.push(arg1ImagePlus);
```

```
// Execute operation on GPU
clij2.absoluteInplace(clij, arg1);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
