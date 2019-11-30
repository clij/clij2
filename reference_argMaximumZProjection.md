## argMaximumZProjection
![Image](images/mini_clij1_logo.png)

Determines the maximum projection of an image stack along Z.
Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).

### Usage in ImageJ macro
```
Ext.CLIJ_argMaximumZProjection(Image source, Image destination_max, Image destination_arg_max);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
destination_max = clij.create(source);
destination_arg_max = clij.create(source);
```

```
// Execute operation on GPU
clijx.argMaximumZProjection(clij, source, destination_max, destination_arg_max);
```

```
//show result
destination_maxImagePlus = clij.pull(destination_max);
destination_maxImagePlus.show();
destination_arg_maxImagePlus = clij.pull(destination_arg_max);
destination_arg_maxImagePlus.show();

// cleanup memory on GPU
source.close();
destination_max.close();
destination_arg_max.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
