## multiplyImages
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Multiplies all pairs of pixel values x and y from two image X and Y.

<pre>f(x, y) = x * y</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_multiplyImages(Image factor1, Image factor2, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer factor1 = clij2.push(factor1ImagePlus);
ClearCLBuffer factor2 = clij2.push(factor2ImagePlus);
destination = clij.create(factor1);
```

```
// Execute operation on GPU
clij2.multiplyImages(clij, factor1, factor2, destination);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(factor1);
clij2.release(factor2);
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/"><img src="images/language_java.png" height="20"/></a> [BenchmarkingWorkflowDemo.java](https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/BenchmarkingWorkflowDemo.java)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
