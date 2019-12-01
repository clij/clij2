## sumPixels
![Image](images/mini_clij1_logo.png)

Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
Results table in the column 'Sum'.

### Usage in ImageJ macro
```
Ext.CLIJ_sumPixels(Image source);
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
double resultSumPixels = clijx.sumPixels(clij, source);
```

```
//show result
System.out.println(resultSumPixels);

// cleanup memory on GPU
source.close();
```




### Example scripts
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [statistics.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/statistics.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
