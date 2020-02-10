## meanSquaredError
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Determines the mean squared error (MSE) between two images. The MSE will be stored in a new row of ImageJs
Results table in the column 'MSE'.

### Usage in ImageJ macro
```
Ext.CLIJx_meanSquaredError(Image source1, Image source2);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source1 = clij2.push(source1ImagePlus);
ClearCLBuffer source2 = clij2.push(source2ImagePlus);
```

```
// Execute operation on GPU
double resultMeanSquaredError = clij2.meanSquaredError(clij, source1, source2);
```

```
//show result
System.out.println(resultMeanSquaredError);

// cleanup memory on GPU
clij2.release(source1);
clij2.release(source2);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [iterative_minimum.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/iterative_minimum.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [minimumOctagon.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/minimumOctagon.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
