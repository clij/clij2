## writeValuesToPositions
![Image](images/mini_clijx_logo.png)

Takes an image with three/four rows (2D: height = 3; 3D: height = 4): x, y [, z] and v and target image. The value v will be written at position x/y[/z] in the target image.

### Usage in ImageJ macro
```
Ext.CLIJx_writeValuesToPositions(Image positionsAndValues, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer positionsAndValues = clijx.push(positionsAndValuesImagePlus);
destination = clij.create(positionsAndValues);
```

```
// Execute operation on GPU
clijx.writeValuesToPositions(clij, positionsAndValues, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
positionsAndValues.close();
destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
