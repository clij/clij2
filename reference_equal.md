## equal
![Image](images/mini_clijx_logo.png)

Determines if two images A and B equal pixel wise.

f(a, b) = 1 if a == b; 0 otherwise. 

### Usage in ImageJ macro
```
Ext.CLIJx_equal(Image source1, Image source2, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source1 = clijx.push(source1ImagePlus);
ClearCLBuffer source2 = clijx.push(source2ImagePlus);
destination = clij.create(source1);
```

```
// Execute operation on GPU
clijx.equal(clij, source1, source2, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
source1.close();
source2.close();
destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
