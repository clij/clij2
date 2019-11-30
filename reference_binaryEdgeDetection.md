## binaryEdgeDetection
![Image](images/mini_clijx_logo.png)

Determines pixels/voxels which are on the surface of a binary objects and sets only them to 1 in the destination image.

### Usage in ImageJ macro
```
Ext.CLIJx_binaryEdgeDetection(Image source, Image destination);
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
```

```
// Execute operation on GPU
clijx.binaryEdgeDetection(clij, arg1, arg2);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [outline.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/outline.ijm)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [outline.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/outline.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
