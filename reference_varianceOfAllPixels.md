## varianceOfAllPixels
![Image](images/mini_clijx_logo.png)

Determines the variance of all pixels in an image. The value will be stored in a new row of ImageJs
Results table in the column 'Variance'.

### Usage in ImageJ macro
```
Ext.CLIJx_varianceOfAllPixels(Image source);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
```

```
// Execute operation on GPU
double resultVarianceOfAllPixels = clijx.varianceOfAllPixels(clij, source);
```

```
//show result
System.out.println(resultVarianceOfAllPixels);

// cleanup memory on GPU
source.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
