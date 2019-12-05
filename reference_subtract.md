## subtract
![Image](images/mini_clij1_logo.png)

Subtracts one image X from another image Y pixel wise.

<pre>f(x, y) = x - y</pre>

### Usage in ImageJ macro
```
Ext.CLIJ_subtract(Image subtrahend, Image minuend, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer subtrahend = clijx.push(subtrahendImagePlus);
ClearCLBuffer minuend = clijx.push(minuendImagePlus);
destination = clij.create(subtrahend);
```

```
// Execute operation on GPU
clijx.subtract(clij, subtrahend, minuend, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
subtrahend.close();
minuend.close();
destination.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [workflow.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/workflow.ijm)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [topHat.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/topHat.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [interactiveCylinderProjection.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/interactiveCylinderProjection.py)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [interactiveSphereProjection.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/interactiveSphereProjection.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [backgroundSubtraction.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/backgroundSubtraction.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [interactiveSpotDetection.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/interactiveSpotDetection.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [multi_GPU_demo.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/multi_GPU_demo.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [spotDetectionpy.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/spotDetectionpy.py)  
<a href="https://github.com/clij/clijpy/blob/master/python/"><img src="images/language_python.png" height="20"/></a> [spot_detection.py](https://github.com/clij/clijpy/blob/master/python/spot_detection.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/"><img src="images/language_java.png" height="20"/></a> [BackgroundSubtractionandThresholdingDemo.java](https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/BackgroundSubtractionandThresholdingDemo.java)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [backgroundSubtractionMaximumProjection.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/backgroundSubtractionMaximumProjection.m)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [spotDetection3D.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/spotDetection3D.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
