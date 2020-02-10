## meanOfAllPixels
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Determines the mean average of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Mean'.

### Usage in ImageJ macro
```
Ext.CLIJx_meanOfAllPixels(Image source);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
```

```
// Execute operation on GPU
double resultMeanOfAllPixels = clij2.meanOfAllPixels(clij, source);
```

```
//show result
System.out.println(resultMeanOfAllPixels);

// cleanup memory on GPU
clij2.release(source);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [preloading.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/preloading.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [spot_distance_measurement.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/spot_distance_measurement.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
