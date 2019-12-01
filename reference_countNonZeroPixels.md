## countNonZeroPixels
![Image](images/mini_clijx_logo.png)

Determines the number of all pixels in a given image which are not equal to 0. It will be stored in a new row of ImageJs
Results table in the column 'CountNonZero'.

### Usage in ImageJ macro
```
Ext.CLIJx_countNonZeroPixels(Image source);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
```

```
// Execute operation on GPU
double resultCountNonZeroPixels = clijx.countNonZeroPixels(clij, source);
```

```
//show result
System.out.println(resultCountNonZeroPixels);

// cleanup memory on GPU
source.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [intensity_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/intensity_per_label.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
