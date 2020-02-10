## binarySubtract
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Subtracts one binary image from another.

### Usage in ImageJ macro
```
Ext.CLIJx_binarySubtract(Image minuend, Image subtrahend, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer minuend = clij2.push(minuendImagePlus);
ClearCLBuffer subtrahend = clij2.push(subtrahendImagePlus);
destination = clij.create(minuend);
```

```
// Execute operation on GPU
clij2.binarySubtract(clij, minuend, subtrahend, destination);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(minuend);
clij2.release(subtrahend);
clij2.release(destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
