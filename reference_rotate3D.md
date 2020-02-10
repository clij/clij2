## rotate3D
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

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
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
destination = clij.create(source);
float angleX = 1.0;
float angleY = 2.0;
float angleZ = 3.0;
boolean rotateAroundCenter = true;
```

```
// Execute operation on GPU
clij2.rotate3D(clij, source, destination, angleX, angleY, angleZ, rotateAroundCenter);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [project3D.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/project3D.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [rotating_sphere.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/rotating_sphere.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
