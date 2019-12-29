## rotate3D
![Image](images/mini_clijx_logo.png)

Rotates an image stack in 3D. All angles are entered in degrees. If the image is not rotated around 
the center, it is rotated around the coordinate origin.

It is recommended to apply the rotation to an isotropic image stack.

### Usage in ImageJ macro
```
Ext.CLIJx_rotate3D(Image source, Image destination, Number angleX, Number angleY, Number angleZ, Boolean rotateAroundCenter);
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
boolean arg6 = true;
```

```
// Execute operation on GPU
clijx.rotate3D(clij, arg1, arg2, arg3, arg4, arg5, arg6);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [project3D.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/project3D.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
