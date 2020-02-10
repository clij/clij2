## deformableRegistration2D
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Applies particle image velocimetry to two images and registers them afterwards by warping input image 2 with a smoothed vector field.

### Usage in ImageJ macro
```
Ext.CLIJx_deformableRegistration2D(Image input1, Image input2, Image destination, Number maxDeltaX, Number maxDeltaY);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer input1 = clij2.push(input1ImagePlus);
ClearCLBuffer input2 = clij2.push(input2ImagePlus);
destination = clij.create(input1);
int maxDeltaX = 10;
int maxDeltaY = 20;
```

```
// Execute operation on GPU
clij2.deformableRegistration2D(clij, input1, input2, destination, maxDeltaX, maxDeltaY);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(input1);
clij2.release(input2);
clij2.release(destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
