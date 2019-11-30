## minimumOfMaskedPixels
![Image](images/mini_clijx_logo.png)

Determines the minimum intensity in an image, but only in pixels which have non-zero values in another mask image.

### Usage in ImageJ macro
```
Ext.CLIJx_minimumOfMaskedPixels(Image source, Image mask);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
ClearCLBuffer mask = clijx.push(maskImagePlus);
```

```
// Execute operation on GPU
double resultMinimumOfMaskedPixels = clijx.minimumOfMaskedPixels(clij, source, mask);
```

```
//show result
System.out.println(resultMinimumOfMaskedPixels);

// cleanup memory on GPU
source.close();
mask.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
