## meanSquaredError
![Image](images/mini_clijx_logo.png)

Determines the mean squared error (MSE) between two images. The MSE will be stored in a new row of ImageJs
Results table in the column 'MSE'.

### Usage in ImageJ macro
```
Ext.CLIJx_meanSquaredError(Image source1, Image source2);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source1 = clijx.push(source1ImagePlus);
ClearCLBuffer source2 = clijx.push(source2ImagePlus);
```

```
// Execute operation on GPU
double resultMeanSquaredError = clijx.meanSquaredError(clij, source1, source2);
```

```
//show result
System.out.println(resultMeanSquaredError);

// cleanup memory on GPU
source1.close();
source2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [iterative_minimum.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/iterative_minimum.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [minimumOctagon.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/minimumOctagon.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
