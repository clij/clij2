## subtractImages
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Subtracts one image X from another image Y pixel wise.

<pre>f(x, y) = x - y</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_subtractImages(Image subtrahend, Image minuend, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer subtrahend = clij2.push(subtrahendImagePlus);
ClearCLBuffer minuend = clij2.push(minuendImagePlus);
destination = clij.create(subtrahend);
```

```
// Execute operation on GPU
clij2.subtractImages(clij, subtrahend, minuend, destination);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(subtrahend);
clij2.release(minuend);
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [workflow.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/workflow.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [topHat.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/topHat.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
