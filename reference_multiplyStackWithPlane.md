## multiplyStackWithPlane
![Image](images/mini_clij1_logo.png)

Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. x and y are at 
the same spatial position within a plane.

<pre>f(x, y) = x * y</pre>

### Usage in ImageJ macro
```
Ext.CLIJ_multiplyStackWithPlane(Image sourceStack, Image sourcePlane, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer sourceStack = clijx.push(sourceStackImagePlus);
ClearCLBuffer sourcePlane = clijx.push(sourcePlaneImagePlus);
destination = clij.create(sourceStack);
```

```
// Execute operation on GPU
clijx.multiplyStackWithPlane(clij, sourceStack, sourcePlane, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
sourceStack.close();
sourcePlane.close();
destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
