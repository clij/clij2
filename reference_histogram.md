## histogram
![Image](images/mini_clij1_logo.png)

Determines the histogram of a given image.

### Usage in ImageJ macro
```
Ext.CLIJ_histogram(Image source, Image destination, Number numberOfBins, Number minimumGreyValue, Number maximumGreyValue, Boolean determineMinAndMax);
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
int arg4 = 10;
```

```
// Execute operation on GPU
float[] resultHistogram = clijx.histogram(clij, arg1, arg2, arg3, arg4);
```

```
//show result
System.out.println(resultHistogram);

// cleanup memory on GPU
arg1.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
