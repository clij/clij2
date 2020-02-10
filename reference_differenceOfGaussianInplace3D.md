## differenceOfGaussianInplace3D
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.

It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.

### Usage in ImageJ macro
```
Ext.CLIJx_differenceOfGaussianInplace3D(Image input_and_destination, Number sigma1x, Number sigma1y, Number sigma1z, Number sigma2x, Number sigma2y, Number sigma2z);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
input_and_destination = clij.create();
float sigma1x = 1.0;
float sigma1y = 2.0;
float sigma1z = 3.0;
float sigma2x = 4.0;
float sigma2y = 5.0;
float sigma2z = 6.0;
```

```
// Execute operation on GPU
clij2.differenceOfGaussianInplace3D(clij, input_and_destination, sigma1x, sigma1y, sigma1z, sigma2x, sigma2y, sigma2z);
```

```
//show result
input_and_destinationImagePlus = clij2.pull(input_and_destination);
input_and_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(input_and_destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
