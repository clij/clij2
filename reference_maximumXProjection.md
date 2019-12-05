## maximumXProjection
![Image](images/mini_clijx_logo.png)

Determines the maximum projection of an image along X.

### Usage in ImageJ macro
```
Ext.CLIJx_maximumXProjection(Image source, Image destination_max);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
destination_max = clij.create(source);
```

```
// Execute operation on GPU
clijx.maximumXProjection(clij, source, destination_max);
```

```
//show result
destination_maxImagePlus = clij.pull(destination_max);
destination_maxImagePlus.show();

// cleanup memory on GPU
source.close();
destination_max.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [halfCylinderProjection.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/halfCylinderProjection.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
