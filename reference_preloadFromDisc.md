## preloadFromDisc
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

This plugin takes two image filenames and loads them into RAM. The first image is returned immediately, the second image is loaded in the background and  will be returned when the plugin is called again.

 It is assumed that all images have the same size. If this is not the case, call release(image) before  getting the second image.

### Usage in ImageJ macro
```
Ext.CLIJx_preloadFromDisc(Image destination, String filename, String nextFilename, String loaderId);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
destination = clij.create();
```

```
// Execute operation on GPU
ClearCLBuffer resultPreloadFromDisc = clij2.preloadFromDisc(clij, destination, filename, nextFilename, loaderId);
```

```
//show result
System.out.println(resultPreloadFromDisc);
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [preloading.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/preloading.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
