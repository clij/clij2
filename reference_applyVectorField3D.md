## applyVectorField3D
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Deforms an image stack according to distances provided in the given vector image stacks. It is recommended to use 32-bit image stacks for input, output and vector image stacks. 

### Usage in ImageJ macro
```
Ext.CLIJx_applyVectorField3D(Image source, Image vectorX, Image vectorY, Image vectorZ, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
ClearCLBuffer vectorX = clij2.push(vectorXImagePlus);
ClearCLBuffer vectorY = clij2.push(vectorYImagePlus);
ClearCLBuffer vectorZ = clij2.push(vectorZImagePlus);
destination = clij.create(source);
```

```
// Execute operation on GPU
clij2.applyVectorField3D(clij, source, vectorX, vectorY, vectorZ, destination);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(vectorX);
clij2.release(vectorY);
clij2.release(vectorZ);
clij2.release(destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
