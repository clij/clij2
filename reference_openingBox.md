## openingBox
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Apply a binary opening to the input image by calling n erosions and n dilations subsequenntly.

### Usage in ImageJ macro
```
Ext.CLIJx_openingBox(Image input, Image destination, Number number_of_erotions_and_dilations);
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
int number_of_erotions_and_dilations = 10;
```

```
// Execute operation on GPU
clij2.openingBox(clij, input, destination, number_of_erotions_and_dilations);
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
