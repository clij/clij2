## maskStackWithPlane
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Computes a masked image by applying a 2D mask to an image stack. All pixel values x of image X will be copied
to the destination image in case pixel value m at the same spatial position in the mask image is not equal to 
zero.

<pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_maskStackWithPlane(Image source_3d, Image mask_2d, Image destination_3d);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source_3d = clij2.push(source_3dImagePlus);
ClearCLBuffer mask_2d = clij2.push(mask_2dImagePlus);
destination_3d = clij.create(source_3d);
```

```
// Execute operation on GPU
clij2.maskStackWithPlane(clij, source_3d, mask_2d, destination_3d);
```

```
//show result
destination_3dImagePlus = clij2.pull(destination_3d);
destination_3dImagePlus.show();

// cleanup memory on GPU
clij2.release(source_3d);
clij2.release(mask_2d);
clij2.release(destination_3d);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
