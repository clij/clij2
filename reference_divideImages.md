## divideImages
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Divides two images X and Y by each other pixel wise.

<pre>f(x, y) = x / y</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_divideImages(Image divident, Image divisor, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer divident = clij2.push(dividentImagePlus);
ClearCLBuffer divisor = clij2.push(divisorImagePlus);
destination = clij.create(divident);
```

```
// Execute operation on GPU
clij2.divideImages(clij, divident, divisor, destination);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(divident);
clij2.release(divisor);
clij2.release(destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
