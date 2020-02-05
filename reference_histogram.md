## histogram
![Image](images/mini_clij1_logo.png)![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Determines the histogram of a given image.

### Usage in ImageJ macro
```
Ext.CLIJx_histogram(Image source, Image destination, Number numberOfBins, Number minimumGreyValue, Number maximumGreyValue, Boolean determineMinAndMax);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer arg1 = clij2.push(arg1ImagePlus);
ClearCLBuffer arg2 = clij2.push(arg2ImagePlus);
int arg3 = 10;
float arg4 = 1.0;
float arg5 = 2.0;
boolean arg6 = true;
```

```
// Execute operation on GPU
clij2.histogram(clij, arg1, arg2, arg3, arg4, arg5, arg6);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
