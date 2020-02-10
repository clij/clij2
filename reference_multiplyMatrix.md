## multiplyMatrix
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Multiplies two matrices with each other.

### Usage in ImageJ macro
```
Ext.CLIJx_multiplyMatrix(Image matrix1, Image matrix2, Image matrix_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer matrix1 = clij2.push(matrix1ImagePlus);
ClearCLBuffer matrix2 = clij2.push(matrix2ImagePlus);
matrix_destination = clij.create(matrix1);
```

```
// Execute operation on GPU
clij2.multiplyMatrix(clij, matrix1, matrix2, matrix_destination);
```

```
//show result
matrix_destinationImagePlus = clij2.pull(matrix_destination);
matrix_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(matrix1);
clij2.release(matrix2);
clij2.release(matrix_destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [matrix_multiply.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/matrix_multiply.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
