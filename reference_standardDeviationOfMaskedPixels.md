## standardDeviationOfMaskedPixels
![Image](images/mini_clijx_logo.png)

Determines the standard deviation of all pixels in an image which have non-zero value in a corresponding mask image. The value will be stored in a new row of ImageJs
Results table in the column 'Masked_standard_deviation'.

### Usage in ImageJ macro
```
Ext.CLIJx_standardDeviationOfMaskedPixels(Image source, Image mask);
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
double resultStandardDeviationOfMaskedPixels = clijx.standardDeviationOfMaskedPixels(clij, source, mask);
```

```
//show result
System.out.println(resultStandardDeviationOfMaskedPixels);

// cleanup memory on GPU
source.close();
mask.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
