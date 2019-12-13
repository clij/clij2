## translate3D
![Image](images/mini_clijx_logo.png)

Translate an image stack in X, Y and Z.

### Usage in ImageJ macro
```
Ext.CLIJx_translate3D(Image source, Image destination, Number translateX, Number translateY, Number translateZ);
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
float arg3 = 1.0;
float arg4 = 2.0;
float arg5 = 3.0;
```

```
// Execute operation on GPU
clijx.translate3D(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [project3D.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/project3D.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
