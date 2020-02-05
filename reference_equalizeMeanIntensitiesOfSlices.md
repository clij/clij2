## equalizeMeanIntensitiesOfSlices
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)![Image](images/mini_clijx_logo.png)

Determines correction factors for each z-slice so that the average intensity in all slices can be made the same and multiplies these factors with the slices.
This functionality is similar to the 'Simple Ratio Bleaching Correction' in Fiji.

### Usage in ImageJ macro
```
Ext.CLIJx_equalizeMeanIntensitiesOfSlices(Image input, Image destination, Number referenceSlice);
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
```

```
// Execute operation on GPU
clij2.equalizeMeanIntensitiesOfSlices(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
