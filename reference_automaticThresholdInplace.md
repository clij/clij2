## automaticThresholdInplace
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one 
of these methods in the method text field:
[Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]

### Usage in ImageJ macro
```
Ext.CLIJx_automaticThresholdInplace(Image input_and_destination, String method);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
input_and_destination = clij.create();
```

```
// Execute operation on GPU
clij2.automaticThresholdInplace(clij, input_and_destination, method);
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
