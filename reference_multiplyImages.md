## multiplyImages
![Image](images/mini_clij1_logo.png)

Multiplies all pairs of pixel values x and y from two image X and Y.

<pre>f(x, y) = x * y</pre>

### Usage in ImageJ macro
```
Ext.CLIJ_multiplyImages(Image factor1, Image factor2, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer factor1 = clijx.push(factor1ImagePlus);
ClearCLBuffer factor2 = clijx.push(factor2ImagePlus);
destination = clij.create(factor1);
```

```
// Execute operation on GPU
clijx.multiplyImages(clij, factor1, factor2, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
factor1.close();
factor2.close();
destination.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [multiply_images_test.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/multiply_images_test.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/"><img src="images/language_java.png" height="20"/></a> [BenchmarkingWorkflowDemo.java](https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/BenchmarkingWorkflowDemo.java)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
