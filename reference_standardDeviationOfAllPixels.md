## standardDeviationOfAllPixels
![Image](images/mini_clijx_logo.png)

Determines the standard deviation of all pixels in an image. The value will be stored in a new row of ImageJs
Results table in the column 'Standard_deviation'.

### Usage in ImageJ macro
```
Ext.CLIJx_standardDeviationOfAllPixels(Image source);
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
double resultStandardDeviationOfAllPixels = clijx.standardDeviationOfAllPixels(clij, source);
```

```
//show result
System.out.println(resultStandardDeviationOfAllPixels);

// cleanup memory on GPU
source.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
