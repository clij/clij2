## centroidsOfLabels
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Determines the centroids of all labels in a label image or image stack and writes the resulting  coordinates in a pointlist image. Depending on the dimensionality d of the labelmap and the number  of labels n, the pointlist image will have n*d pixels.

### Usage in ImageJ macro
```
Ext.CLIJx_centroidsOfLabels(Image source);
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
```

```
// Execute operation on GPU
clij2.centroidsOfLabels(clij, arg1, arg2);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
