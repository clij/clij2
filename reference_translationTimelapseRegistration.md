## translationTimelapseRegistration
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Applies 2D translation registration to every pair of t, t+1 slices of a 2D+t image stack.

### Usage in ImageJ macro
```
Ext.CLIJx_translationTimelapseRegistration(Image input, Image output);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer input = clij2.push(inputImagePlus);
output = clij.create(input);
```

```
// Execute operation on GPU
clij2.translationTimelapseRegistration(clij, input, output);
```

```
//show result
outputImagePlus = clij2.pull(output);
outputImagePlus.show();

// cleanup memory on GPU
clij2.release(input);
clij2.release(output);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
