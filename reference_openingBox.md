## openingBox
![Image](images/mini_clijx_logo.png)

Apply a binary opening to the input image by calling n erosions and n dilations subsequenntly.

### Usage in ImageJ macro
```
Ext.CLIJx_openingBox(Image input, Image destination, Number number_of_erotions_and_dilations);
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
```

```
// Execute operation on GPU
clijx.openingBox(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
