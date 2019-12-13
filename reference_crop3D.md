## crop3D
![Image](images/mini_clijx_logo.png)

Crops a given sub-stack out of a given image stack.

Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.

### Usage in ImageJ macro
```
Ext.CLIJx_crop3D(Image source, Image destination, Number startX, Number startY, Number startZ, Number width, Number height, Number depth);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
ClearCLBuffer arg2 = clijx.push(arg2ImagePlus);
int arg3 = 10;
int arg4 = 20;
int arg5 = 30;
int arg6 = 40;
int arg7 = 50;
int arg8 = 60;
```

```
// Execute operation on GPU
clijx.crop3D(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [oddEven.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/oddEven.ijm)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [allocateBigImages.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/allocateBigImages.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
