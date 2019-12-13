## applyVectorField2D
![Image](images/mini_clijx_logo.png)

Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 

### Usage in ImageJ macro
```
Ext.CLIJx_applyVectorField2D(Image source, Image vectorX, Image vectorY, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
ClearCLBuffer vectorX = clijx.push(vectorXImagePlus);
ClearCLBuffer vectorY = clijx.push(vectorYImagePlus);
destination = clij.create(source);
```

```
// Execute operation on GPU
clijx.applyVectorField2D(clij, source, vectorX, vectorY, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
source.close();
vectorX.close();
vectorY.close();
destination.close();
```




### Example scripts
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [applyVectorField.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/applyVectorField.ijm)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [warpCat.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/warpCat.ijm)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [warpCat_RGB.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/warpCat_RGB.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
