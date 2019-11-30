## generateTouchMatrix
![Image](images/mini_clijx_logo.png)

Takes a labelmap with n labels and generates a (n+1)*(n+1) matrix where all pixels are set to 0 exept those where labels are touching.Only half of the matrix is filled (with x < y). For example, if labels 3 and 4 are touching then the pixel (3,4) in the matrix will be set to 1.

### Usage in ImageJ macro
```
Ext.CLIJx_generateTouchMatrix(Image label_map, Image touch_matrix_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer label_map = clijx.push(label_mapImagePlus);
touch_matrix_destination = clij.create(label_map);
```

```
// Execute operation on GPU
clijx.generateTouchMatrix(clij, label_map, touch_matrix_destination);
```

```
//show result
touch_matrix_destinationImagePlus = clij.pull(touch_matrix_destination);
touch_matrix_destinationImagePlus.show();

// cleanup memory on GPU
label_map.close();
touch_matrix_destination.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
