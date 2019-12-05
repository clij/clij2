## rotateRight
![Image](images/mini_clij1_logo.png)

Rotates a given input image by 90 degrees clockwise. For that, X and Y axis of an image stack
are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
such as interpolation.

### Usage in ImageJ macro
```
Ext.CLIJ_rotateRight(Image source, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
destination = clij.create(source);
```

```
// Execute operation on GPU
clijx.rotateRight(clij, source, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
source.close();
destination.close();
```




### Example scripts
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [turn_stack.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/turn_stack.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [interactiveCylinderProjection.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/interactiveCylinderProjection.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [interactiveSpotDetection.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/interactiveSpotDetection.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [spotDetectionpy.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/spotDetectionpy.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
