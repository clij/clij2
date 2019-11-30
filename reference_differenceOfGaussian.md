## differenceOfGaussian
![Image](images/mini_clijx_logo.png)

Applies Gaussian blur to the input image twice resulting in two images which are then subtracted from each other.

### Usage in ImageJ macro
```
Ext.CLIJx_differenceOfGaussian(Image input, Image destination, Number sigma1x, Number sigma1y, Number sigma2x, Number sigma2y);
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
float arg4 = 2.0;
float arg5 = 3.0;
float arg6 = 4.0;
```

```
// Execute operation on GPU
clijx.differenceOfGaussian(clij, arg1, arg2, arg3, arg4, arg5, arg6);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
