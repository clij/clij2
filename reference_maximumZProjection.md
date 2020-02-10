## maximumZProjection
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Determines the maximum projection of an image along Z.

### Usage in ImageJ macro
```
Ext.CLIJx_maximumZProjection(Image source, Image destination_max);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
destination_max = clij.create(new long[]{source.getWidth(), source.getHeight()}, source.getNativeType());
```

```
// Execute operation on GPU
clij2.maximumZProjection(clij, source, destination_max);
```

```
//show result
destination_maxImagePlus = clij2.pull(destination_max);
destination_maxImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(destination_max);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [bigImageTransfer.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/bigImageTransfer.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [maximumProjection.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/maximumProjection.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [orthogonalMaximumProjections.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/orthogonalMaximumProjections.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [project3D.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/project3D.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [rotating_sphere.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/rotating_sphere.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [interactiveCylinderProjection.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/interactiveCylinderProjection.py)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [interactiveSphereProjection.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/interactiveSphereProjection.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/"><img src="images/language_java.png" height="20"/></a> [MaximumProjectionDemo.java](https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/MaximumProjectionDemo.java)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/javascript/"><img src="images/language_javascript.png" height="20"/></a> [maximumProjection.js](https://github.com/clij/clij-docs/blob/master/src/main/javascript/maximumProjection.js)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/beanshell/"><img src="images/language_beanshell.png" height="20"/></a> [maximumProjection.bsh](https://github.com/clij/clij-docs/blob/master/src/main/beanshell/maximumProjection.bsh)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [backgroundSubtractionMaximumProjection.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/backgroundSubtractionMaximumProjection.m)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [maximumProjection.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/maximumProjection.m)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [reslice.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/reslice.m)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [spotDetection3D.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/spotDetection3D.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
