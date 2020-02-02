## standardDeviationOfAllPixels
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)![Image](images/mini_clijx_logo.png)

Determines the standard deviation of all pixels in an image. The value will be stored in a new row of ImageJs
Results table in the column 'Standard_deviation'.

### Usage in ImageJ macro
```
Ext.CLIJx_standardDeviationOfAllPixels(Image source);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer arg1 = clij2.push(arg1ImagePlus);
```

```
// Execute operation on GPU
double resultStandardDeviationOfAllPixels = clij2.standardDeviationOfAllPixels(clij, arg1);
```

```
//show result
System.out.println(resultStandardDeviationOfAllPixels);

// cleanup memory on GPU
clij2.release(arg1);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
