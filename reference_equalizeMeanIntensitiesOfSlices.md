## equalizeMeanIntensitiesOfSlices
![Image](images/mini_clijx_logo.png)

Determines correction factors for each z-slice so that the average intensity in all slices can be made the same and multiplies these factors with the slices.
This functionality is similar to the 'Simple Ratio Bleaching Correction' in Fiji.

### Usage in ImageJ macro
```
Ext.CLIJx_equalizeMeanIntensitiesOfSlices(Image input, Image destination, Number referenceSlice);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
ClearCLBuffer arg2 = clijx.push(arg2ImagePlus);
int arg3 = 10;
```

```
// Execute operation on GPU
clijx.equalizeMeanIntensitiesOfSlices(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
