## copy
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Copies an image.

<pre>f(x) = x</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_copy(Image source, Image destination);
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
```

```
// Execute operation on GPU
clij2.copy(clij, source, destination);
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
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [virtual_ablation.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/virtual_ablation.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [allocateBigImages.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/allocateBigImages.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [applyVectorField.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/applyVectorField.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [memory_reuse_versus_reallocation.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/memory_reuse_versus_reallocation.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [motionCorrection.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/motionCorrection.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [motionCorrection_compare_stackreg.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/motionCorrection_compare_stackreg.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [project3D.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/project3D.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [rgb_invert_black_white.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/rgb_invert_black_white.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [rotateFree.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/rotateFree.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [rotateOverwriteOriginal.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/rotateOverwriteOriginal.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [rotating_sphere.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/rotating_sphere.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [scaleFree.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/scaleFree.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [translate.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/translate.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [warpCat.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/warpCat.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [warpCat_RGB.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/warpCat_RGB.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [countNeighbors3D.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/countNeighbors3D.py)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [interactiveCylinderProjection.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/interactiveCylinderProjection.py)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [interactiveSphereProjection.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/interactiveSphereProjection.py)  
<a href="https://github.com/clij/clijpy/blob/master/python/"><img src="images/language_python.png" height="20"/></a> [benchmark_clijx_pull.ipynb](https://github.com/clij/clijpy/blob/master/python/benchmark_clijx_pull.ipynb)  
<a href="https://github.com/clij/clijpy/blob/master/python/"><img src="images/language_python.png" height="20"/></a> [clijpy_demo.ipynb](https://github.com/clij/clijpy/blob/master/python/clijpy_demo.ipynb)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/"><img src="images/language_java.png" height="20"/></a> [ApplyVectorFieldDemo.java](https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/ApplyVectorFieldDemo.java)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/"><img src="images/language_java.png" height="20"/></a> [MotionCorrectionDemo.java](https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/MotionCorrectionDemo.java)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
