## minimumOfAllPixels
![Image](images/mini_clij1_logo.png)

Determines the minimum of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Min'.

### Usage in ImageJ macro
```
Ext.CLIJ_minimumOfAllPixels(Image source);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
```

```
// Execute operation on GPU
double resultMinimumOfAllPixels = clijx.minimumOfAllPixels(clij, source);
```

```
//show result
System.out.println(resultMinimumOfAllPixels);

// cleanup memory on GPU
source.close();
```




### Example scripts
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [statistics.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/statistics.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
