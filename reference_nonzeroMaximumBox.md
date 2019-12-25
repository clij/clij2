## nonzeroMaximumBox
![Image](images/mini_clijx_logo.png)

Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.

### Usage in ImageJ macro
```
Ext.CLIJx_nonzeroMaximumBox(Image input, Image destination);
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
ClearCLBuffer arg3 = clijx.push(arg3ImagePlus);
```

```
// Execute operation on GPU
ClearCLKernel resultNonzeroMaximumBox = clijx.nonzeroMaximumBox(clij, arg1, arg2, arg3, arg4);
```

```
//show result
System.out.println(resultNonzeroMaximumBox);

// cleanup memory on GPU
arg1.close();
arg2.close();
arg3.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)