## subtractImageFromScalar
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Subtracts one image X from a scalar s pixel wise.

<pre>f(x, s) = s - x</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_subtractImageFromScalar(Image input, Image destination, Number scalar);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer input = clij2.push(inputImagePlus);
destination = clij.create(input);
float scalar = 1.0;
```

```
// Execute operation on GPU
clij2.subtractImageFromScalar(clij, input, destination, scalar);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(input);
clij2.release(destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
