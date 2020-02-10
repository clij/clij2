## paste3D
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Pastes an image into another image at a given position.

### Usage in ImageJ macro
```
Ext.CLIJx_paste3D(Image source, Image destination, Number destinationX, Number destinationY, Number destinationZ);
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
int destinationX = 10;
int destinationY = 20;
int destinationZ = 30;
```

```
// Execute operation on GPU
clij2.paste3D(clij, source, destination, destinationX, destinationY, destinationZ);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
