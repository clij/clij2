## laplaceSphere
![Image](images/mini_clijx_logo.png)

Applies the Laplace operator (Diamond neighborhood) to an image.

### Usage in ImageJ macro
```
Ext.CLIJx_laplaceSphere(Image input, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer input = clijx.push(inputImagePlus);
destination = clij.create(input);
```

```
// Execute operation on GPU
clijx.laplaceSphere(clij, input, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
input.close();
destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
