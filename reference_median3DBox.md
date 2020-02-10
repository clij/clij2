## median3DBox
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Computes the local median of a pixels cuboid neighborhood. The cuboid size is specified by 
its half-width, half-height and half-depth (radius).

For technical reasons, the volume of the cuboid must contain less than 1000 voxels.

### Usage in ImageJ macro
```
Ext.CLIJx_median3DBox(Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ);
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
int radiusX = 10;
int radiusY = 20;
int radiusZ = 30;
```

```
// Execute operation on GPU
clij2.median3DBox(clij, source, destination, radiusX, radiusY, radiusZ);
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
