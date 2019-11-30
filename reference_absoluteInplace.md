## absoluteInplace
![Image](images/mini_clijx_logo.png)

Computes the absolute value of every individual pixel x in a given image.

<pre>f(x) = |x| </pre>

### Usage in ImageJ macro
```
Ext.CLIJx_absoluteInplace(Image source, Image destination);
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
clijx.absoluteInplace(clij, arg1);
```

```
//show result

// cleanup memory on GPU
arg1.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
