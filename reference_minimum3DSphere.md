## minimum3DSphere
![Image](images/mini_clij1_logo.png)![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Computes the local minimum of a pixels spherical neighborhood. The spheres size is specified by 
its half-width, half-height and half-depth (radius).

### Usage in ImageJ macro
```
Ext.CLIJx_minimum3DSphere(Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ);
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
int arg5 = 30;
```

```
// Execute operation on GPU
clij2.minimum3DSphere(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [iterative_minimum.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/iterative_minimum.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [minimumOctagon.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/minimumOctagon.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [oddEven.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/oddEven.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
