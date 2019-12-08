## crop
![Image](images/mini_clij1_logo.png)

Crops a given rectangle out of a given image.

Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.

### Usage in ImageJ macro
```
Ext.CLIJ_crop(Image source, Image destination, Number startX, Number startY, Number width, Number height);
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
```

```
// Execute operation on GPU
clijx.crop(clij, arg1, arg2, arg3, arg4, arg5);
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
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [crop.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/crop.ijm)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [crop.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/crop.py)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [crop.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/crop.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
