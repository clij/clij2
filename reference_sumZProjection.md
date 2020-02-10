## sumZProjection
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Determines the sum projection of an image along Z.

### Usage in ImageJ macro
```
Ext.CLIJx_sumZProjection(Image source, Image destination_sum);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
destination_sum = clij.create(source);
```

```
// Execute operation on GPU
clij2.sumZProjection(clij, source, destination_sum);
```

```
//show result
destination_sumImagePlus = clij2.pull(destination_sum);
destination_sumImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(destination_sum);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
