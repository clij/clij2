## matrixEqual
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Checks if all elements of a matrix are different by less than or equal to a given tolerance.
The result will be put in the results table in column "MatrixEqual" as 1 if yes and 0 otherwise.

### Usage in ImageJ macro
```
Ext.CLIJx_matrixEqual(Image input1, Image input2, Number tolerance);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer input1 = clij2.push(input1ImagePlus);
ClearCLBuffer input2 = clij2.push(input2ImagePlus);
float tolerance = 1.0;
```

```
// Execute operation on GPU
clij2.matrixEqual(clij, input1, input2, tolerance);
```

```
//show result

// cleanup memory on GPU
clij2.release(input1);
clij2.release(input2);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
