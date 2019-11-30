## paste
![Image](images/mini_clijx_logo.png)

Pastes an image into another image at a given position.

### Usage in ImageJ macro
```
Ext.CLIJx_paste(Image source, Image destination, Number destinationX, Number destinationY);
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
```

```
// Execute operation on GPU
clijx.paste(clij, arg1, arg2, arg3, arg4);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [make_super_blobs.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/make_super_blobs.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [paste_images.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/paste_images.ijm)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [paste.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/paste.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
