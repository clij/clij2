## meanOfMaskedPixels
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Determines the mean intensity in an image, but only in pixels which have non-zero values in another binary mask image.

### Usage in ImageJ macro
```
Ext.CLIJx_meanOfMaskedPixels(Image source, Image mask);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
ClearCLBuffer mask = clij2.push(maskImagePlus);
```

```
// Execute operation on GPU
double resultMeanOfMaskedPixels = clij2.meanOfMaskedPixels(clij, source, mask);
```

```
//show result
System.out.println(resultMeanOfMaskedPixels);

// cleanup memory on GPU
clij2.release(source);
clij2.release(mask);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
