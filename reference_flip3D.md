## flip3D
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Flips an image in X, Y and/or Z direction depending on boolean flags.

### Usage in ImageJ macro
```
Ext.CLIJx_flip3D(Image source, Image destination, Boolean flipX, Boolean flipY, Boolean flipZ);
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
boolean flipX = true;
boolean flipY = false;
boolean flipZ = false;
```

```
// Execute operation on GPU
clij2.flip3D(clij, source, destination, flipX, flipY, flipZ);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [flip.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/flip.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
