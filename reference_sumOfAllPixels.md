## sumOfAllPixels
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Sum'.

### Usage in ImageJ macro
```
Ext.CLIJx_sumOfAllPixels(Image source);
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
double resultSumOfAllPixels = clij2.sumOfAllPixels(clij, source);
```

```
//show result
System.out.println(resultSumOfAllPixels);

// cleanup memory on GPU
clij2.release(source);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [measure_area_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measure_area_per_label.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [intensity_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/intensity_per_label.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [statistics.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/statistics.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
