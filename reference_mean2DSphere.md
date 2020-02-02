## mean2DSphere
![Image](images/mini_clij1_logo.png)![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Computes the local mean average of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
its half-width and half-height (radius).

### Usage in ImageJ macro
```
Ext.CLIJx_mean2DSphere(Image source, Image destination, Number radiusX, Number radiusY);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer arg1 = clij2.push(arg1ImagePlus);
ClearCLBuffer arg2 = clij2.push(arg2ImagePlus);
int arg3 = 10;
int arg4 = 20;
```

```
// Execute operation on GPU
clij2.mean2DSphere(clij, arg1, arg2, arg3, arg4);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [mean_detailed_comparison_IJ_CLIJ.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mean_detailed_comparison_IJ_CLIJ.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
