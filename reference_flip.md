## flip
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Flips an image in X and/or Y direction depending on boolean flags.

### Usage in ImageJ macro
```
Ext.CLIJx_flip(Image source, Image destination, Boolean flipX, Boolean flipY);
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
boolean arg3 = true;
boolean arg4 = false;
```

```
// Execute operation on GPU
clij2.flip(clij, arg1, arg2, arg3, arg4);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [countNeighbors3D.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/countNeighbors3D.py)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [flipImage.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/flipImage.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
