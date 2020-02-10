## translate3D
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Translate an image stack in X, Y and Z.

### Usage in ImageJ macro
```
Ext.CLIJx_translate3D(Image source, Image destination, Number translateX, Number translateY, Number translateZ);
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
float translateX = 1.0;
float translateY = 2.0;
float translateZ = 3.0;
```

```
// Execute operation on GPU
clij2.translate3D(clij, source, destination, translateX, translateY, translateZ);
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
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [project3D.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/project3D.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
