## subtractBackground3D
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Applies Gaussian blur to the input image and subtracts the result from the original image.

### Usage in ImageJ macro
```
Ext.CLIJx_subtractBackground3D(Image input, Image destination, Number sigmaX, Number sigmaY, Number sigmaZ);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer input = clij2.push(inputImagePlus);
destination = clij.create(input);
float sigmaX = 1.0;
float sigmaY = 2.0;
float sigmaZ = 3.0;
```

```
// Execute operation on GPU
clij2.subtractBackground3D(clij, input, destination, sigmaX, sigmaY, sigmaZ);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(input);
clij2.release(destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
