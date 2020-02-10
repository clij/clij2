## maximum2DSphere
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Computes the local maximum of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
its half-width and half-height (radius).

### Usage in ImageJ macro
```
Ext.CLIJx_maximum2DSphere(Image source, Image destination, Number radiusX, Number radiusY);
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
int radiusX = 10;
int radiusY = 20;
```

```
// Execute operation on GPU
clij2.maximum2DSphere(clij, source, destination, radiusX, radiusY);
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
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [localMaximum.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/localMaximum.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
