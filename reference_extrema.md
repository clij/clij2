## extrema
![Image](images/mini_clijx_logo.png)

Returns an image with pixel values most distant from 0: f(x, y) = x if abs(x) > abs(y), y else.

### Usage in ImageJ macro
```
Ext.CLIJx_extrema(Image input1, Image input2, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer input1 = clijx.push(input1ImagePlus);
ClearCLBuffer input2 = clijx.push(input2ImagePlus);
destination = clij.create(input1);
```

```
// Execute operation on GPU
clijx.extrema(clij, input1, input2, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
input1.close();
input2.close();
destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
