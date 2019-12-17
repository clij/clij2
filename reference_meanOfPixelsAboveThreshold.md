## meanOfPixelsAboveThreshold
![Image](images/mini_clijx_logo.png)

Determines the mean intensity in an image, but only in pixels which are above a given threshold.

### Usage in ImageJ macro
```
Ext.CLIJx_meanOfPixelsAboveThreshold(Image source, Number threshold);
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
```

```
// Execute operation on GPU
double resultMeanOfPixelsAboveThreshold = clijx.meanOfPixelsAboveThreshold(clij, arg1, arg2);
```

```
//show result
System.out.println(resultMeanOfPixelsAboveThreshold);

// cleanup memory on GPU
arg1.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
