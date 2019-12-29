## multiplyStackWithPlane
![Image](images/mini_clijx_logo.png)

Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. x and y are at 
the same spatial position within a plane.

<pre>f(x, y) = x * y</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_multiplyStackWithPlane(Image sourceStack, Image sourcePlane, Image destination);
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
ClearCLBuffer arg3 = clijx.push(arg3ImagePlus);
```

```
// Execute operation on GPU
clijx.multiplyStackWithPlane(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
arg3.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
