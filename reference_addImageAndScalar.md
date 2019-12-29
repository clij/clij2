## addImageAndScalar
![Image](images/mini_clijx_logo.png)

Adds a scalar value s to all pixels x of a given image X.

<pre>f(x, s) = x + s</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_addImageAndScalar(Image source, Image destination, Number scalar);
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
float arg3 = 1.0;
```

```
// Execute operation on GPU
clijx.addImageAndScalar(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [absolute.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/absolute.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
