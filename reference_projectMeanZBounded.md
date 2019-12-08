## projectMeanZBounded
![Image](images/mini_clijx_logo.png)

Determines the mean projection of an image along Z within a given z range.

### Usage in ImageJ macro
```
Ext.CLIJx_projectMeanZBounded(Image source, Image destination_mean, Number min_z, Number max_z);
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
int arg4 = 20;
```

```
// Execute operation on GPU
clijx.projectMeanZBounded(clij, arg1, arg2, arg3, arg4);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)