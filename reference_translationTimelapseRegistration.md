## translationTimelapseRegistration
![Image](images/mini_clijx_logo.png)

Applies 2D translation registration to every pair of t, t+1 slices of a 2D+t image stack.

### Usage in ImageJ macro
```
Ext.CLIJx_translationTimelapseRegistration(Image input, Image output);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer input = clijx.push(inputImagePlus);
output = clij.create(input);
```

```
// Execute operation on GPU
clijx.translationTimelapseRegistration(clij, input, output);
```

```
//show result
outputImagePlus = clij.pull(output);
outputImagePlus.show();

// cleanup memory on GPU
input.close();
output.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
