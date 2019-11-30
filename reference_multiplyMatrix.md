## multiplyMatrix
![Image](images/mini_clijx_logo.png)

Multiplies two matrices with each other.

### Usage in ImageJ macro
```
Ext.CLIJx_multiplyMatrix(Image matrix1, Image matrix2, Image matrix_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer matrix1 = clijx.push(matrix1ImagePlus);
ClearCLBuffer matrix2 = clijx.push(matrix2ImagePlus);
matrix_destination = clij.create(matrix1);
```

```
// Execute operation on GPU
clijx.multiplyMatrix(clij, matrix1, matrix2, matrix_destination);
```

```
//show result
matrix_destinationImagePlus = clij.pull(matrix_destination);
matrix_destinationImagePlus.show();

// cleanup memory on GPU
matrix1.close();
matrix2.close();
matrix_destination.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [matrix_multiply.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/matrix_multiply.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
