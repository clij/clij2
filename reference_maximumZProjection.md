## maximumZProjection
![Image](images/mini_clij1_logo.png)

Determines the maximum projection of an image along Z.

### Usage in ImageJ macro
```
Ext.CLIJ_maximumZProjection(Image source, Image destination_max);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
destination_max = clij.create(new long[]{source.getWidth(), source.getHeight()}, source.getNativeType());
```

```
// Execute operation on GPU
clijx.maximumZProjection(clij, source, destination_max);
```

```
//show result
destination_maxImagePlus = clij.pull(destination_max);
destination_maxImagePlus.show();

// cleanup memory on GPU
source.close();
destination_max.close();
```




### Example scripts
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [bigImageTransfer.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/bigImageTransfer.ijm)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [maximumProjection.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/maximumProjection.ijm)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [project3D.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/project3D.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [interactiveCylinderProjection.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/interactiveCylinderProjection.py)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [interactiveSphereProjection.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/interactiveSphereProjection.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/groovy/"><img src="images/language_groovy.png" height="20"/></a> [maximumProjection.groovy](https://github.com/clij/clij-docs/blob/master/src/main/groovy/maximumProjection.groovy)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [interactiveSpotDetection.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/interactiveSpotDetection.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [maximumProjection.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/maximumProjection.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [multi_GPU_demo.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/multi_GPU_demo.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/"><img src="images/language_java.png" height="20"/></a> [MaximumProjectionDemo.java](https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/MaximumProjectionDemo.java)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/javascript/"><img src="images/language_javascript.png" height="20"/></a> [maximumProjection.js](https://github.com/clij/clij-docs/blob/master/src/main/javascript/maximumProjection.js)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/beanshell/"><img src="images/language_beanshell.png" height="20"/></a> [maximumProjection.bsh](https://github.com/clij/clij-docs/blob/master/src/main/beanshell/maximumProjection.bsh)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
