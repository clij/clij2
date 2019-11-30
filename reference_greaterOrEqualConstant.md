## greaterOrEqualConstant
![Image](images/mini_clijx_logo.png)

Determines if two images A and B greater or equal pixel wise.

f(a, b) = 1 if a >= b; 0 otherwise. 

### Usage in ImageJ macro
```
Ext.CLIJx_greaterOrEqualConstant(Image source, Image destination, Number constant);
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
float arg3 = 1.0;
```

```
// Execute operation on GPU
clijx.greaterOrEqualConstant(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
