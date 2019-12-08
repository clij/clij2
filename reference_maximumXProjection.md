## maximumXProjection
![Image](images/mini_clijx_logo.png)

Determines the maximum projection of an image along X.

### Usage in ImageJ macro
```
Ext.CLIJx_maximumXProjection(Image source, Image destination_max);
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
```

```
// Execute operation on GPU
clijx.maximumXProjection(clij, source, destination_max);
```

```
//show result
destination_maxImagePlus = clij.pull(destination_max);
destination_maxImagePlus.show();

// cleanup memory on GPU
source.close();
destination_max.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
