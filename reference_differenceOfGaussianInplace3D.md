## differenceOfGaussianInplace3D
![Image](images/mini_clijx_logo.png)

Applies Gaussian blur to the input image twice resulting in two images which are then subtracted from each other.

It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.

### Usage in ImageJ macro
```
Ext.CLIJx_differenceOfGaussianInplace3D(Image input_and_destination, Number sigma1x, Number sigma1y, Number sigma1z, Number sigma2x, Number sigma2y, Number sigma2z);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
float arg2 = 1.0;
float arg3 = 2.0;
float arg4 = 3.0;
float arg5 = 4.0;
float arg6 = 5.0;
float arg7 = 6.0;
```

```
// Execute operation on GPU
clijx.differenceOfGaussianInplace3D(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
```

```
//show result

// cleanup memory on GPU
arg1.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
