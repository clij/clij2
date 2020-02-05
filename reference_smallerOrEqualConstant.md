## smallerOrEqualConstant
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Determines if two images A and B smaller or equal pixel wise.

f(a, b) = 1 if a <= b; 0 otherwise. 

### Usage in ImageJ macro
```
Ext.CLIJx_smallerOrEqualConstant(Image source, Image destination, Number constant);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer arg1 = clij2.push(arg1ImagePlus);
ClearCLBuffer arg2 = clij2.push(arg2ImagePlus);
float arg3 = 1.0;
```

```
// Execute operation on GPU
clij2.smallerOrEqualConstant(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
