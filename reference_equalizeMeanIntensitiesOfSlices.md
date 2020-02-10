## equalizeMeanIntensitiesOfSlices
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

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
ClearCLBuffer input = clij2.push(inputImagePlus);
destination = clij.create(input);
int referenceSlice = 10;
```

```
// Execute operation on GPU
clij2.equalizeMeanIntensitiesOfSlices(clij, input, destination, referenceSlice);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(input);
clij2.release(destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
