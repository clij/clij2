## meanBox
![Image](images/mini_clij1_logo.png)

Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
its half-width and half-height (radius).

### Usage in ImageJ macro
```
Ext.CLIJ_meanBox(Image source, Image destination, Number radiusX, Number radiusY);
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
```

```
// Execute operation on GPU
clijx.meanBox(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/"><img src="images/language_java.png" height="20"/></a> [BackgroundSubtractionandThresholdingDemo.java](https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/BackgroundSubtractionandThresholdingDemo.java)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [meanFilter.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/meanFilter.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
