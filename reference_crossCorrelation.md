## crossCorrelation
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)![Image](images/mini_clijx_logo.png)

Performs cross correlation analysis between two images. The second image is shifted by deltaPos in the given dimension. The cross correlation coefficient is calculated for each pixel in a range around the given pixel with given radius in the given dimension. Together with the original images it is recommended to hand over mean filtered images using the same radius.  

### Usage in ImageJ macro
```
Ext.CLIJx_crossCorrelation(Image input1, Image meanInput1, Image input2, Image meanInput2, Image destination, Number radius, Number deltaPos, Number dimension);
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
ClearCLBuffer arg3 = clij2.push(arg3ImagePlus);
ClearCLBuffer arg4 = clij2.push(arg4ImagePlus);
ClearCLBuffer arg5 = clij2.push(arg5ImagePlus);
```

```
// Execute operation on GPU
clij2.crossCorrelation(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
clij2.release(arg3);
clij2.release(arg4);
clij2.release(arg5);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
