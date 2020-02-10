## watershed
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Apply a binary watershed to a binary image and introduces black pixels between objects.

### Usage in ImageJ macro
```
Ext.CLIJx_watershed(Image binary_source, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer binary_source = clij2.push(binary_sourceImagePlus);
destination = clij.create(binary_source);
```

```
// Execute operation on GPU
clij2.watershed(clij, binary_source, destination);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(binary_source);
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [watershed.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/watershed.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
