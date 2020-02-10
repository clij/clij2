## extrema
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Returns an image with pixel values most distant from 0: f(x, y) = x if abs(x) > abs(y), y else.

### Usage in ImageJ macro
```
Ext.CLIJx_extrema(Image input1, Image input2, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer input1 = clij2.push(input1ImagePlus);
ClearCLBuffer input2 = clij2.push(input2ImagePlus);
destination = clij.create(input1);
```

```
// Execute operation on GPU
clij2.extrema(clij, input1, input2, destination);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(input1);
clij2.release(input2);
clij2.release(destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
