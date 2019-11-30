## meanZProjection
![Image](images/mini_clij1_logo.png)

Determines the mean average projection of an image along Z.

### Usage in ImageJ macro
```
Ext.CLIJ_meanZProjection(Image source, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
destination = clij.create(new long[]{source.getWidth(), source.getHeight()}, source.getNativeType());
```

```
// Execute operation on GPU
clijx.meanZProjection(clij, source, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
source.close();
destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
