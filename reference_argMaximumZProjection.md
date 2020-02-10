## argMaximumZProjection
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Determines the maximum projection of an image stack along Z.
Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).

### Usage in ImageJ macro
```
Ext.CLIJx_argMaximumZProjection(Image source, Image destination_max, Image destination_arg_max);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
destination_max = clij.create(source);
destination_arg_max = clij.create(source);
```

```
// Execute operation on GPU
clij2.argMaximumZProjection(clij, source, destination_max, destination_arg_max);
```

```
//show result
destination_maxImagePlus = clij2.pull(destination_max);
destination_maxImagePlus.show();
destination_arg_maxImagePlus = clij2.pull(destination_arg_max);
destination_arg_maxImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(destination_max);
clij2.release(destination_arg_max);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
