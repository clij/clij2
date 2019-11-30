## powerImages
![Image](images/mini_clijx_logo.png)

Calculates x to the power of y pixel wise of two images X and Y.

### Usage in ImageJ macro
```
Ext.CLIJx_powerImages(Image input, Image exponent, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer input = clijx.push(inputImagePlus);
ClearCLBuffer exponent = clijx.push(exponentImagePlus);
destination = clij.create(input);
```

```
// Execute operation on GPU
clijx.powerImages(clij, input, exponent, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
input.close();
exponent.close();
destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
