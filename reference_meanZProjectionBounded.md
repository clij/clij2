## meanZProjectionBounded
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Determines the mean projection of an image along Z within a given z range.

### Usage in ImageJ macro
```
Ext.CLIJx_meanZProjectionBounded(Image source, Image destination_mean, Number min_z, Number max_z);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
destination_mean = clij.create(source);
int min_z = 10;
int max_z = 20;
```

```
// Execute operation on GPU
clij2.meanZProjectionBounded(clij, source, destination_mean, min_z, max_z);
```

```
//show result
destination_meanImagePlus = clij2.pull(destination_mean);
destination_meanImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(destination_mean);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
