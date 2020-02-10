## multiplyImageAndCoordinate
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Multiplies all pixel intensities with the x, y or z coordinate, depending on specified dimension.</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_multiplyImageAndCoordinate(Image source, Image destination, Number dimension);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
destination = clij.create(source);
int dimension = 10;
```

```
// Execute operation on GPU
clij2.multiplyImageAndCoordinate(clij, source, destination, dimension);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
