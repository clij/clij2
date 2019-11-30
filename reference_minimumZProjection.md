## minimumZProjection
![Image](images/mini_clij1_logo.png)

Determines the minimum projection of an image along Z.

### Usage in ImageJ macro
```
Ext.CLIJ_minimumZProjection(Image source, Image destination_sum);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
destination_sum = clij.create(new long[]{source.getWidth(), source.getHeight()}, source.getNativeType());
```

```
// Execute operation on GPU
clijx.minimumZProjection(clij, source, destination_sum);
```

```
//show result
destination_sumImagePlus = clij.pull(destination_sum);
destination_sumImagePlus.show();

// cleanup memory on GPU
source.close();
destination_sum.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
