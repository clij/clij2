## excludeLabelsOnSurface
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

This operation follows a ray from a given position towards a label (or opposite direction) and checks if  there is another label between the label an the image border. If yes, this label is eliminated from the label map.

### Usage in ImageJ macro
```
Ext.CLIJx_excludeLabelsOnSurface(Image pointlist, Image label_map_input, Image label_map_destination, Number centerX, Number centerY, Number centerZ);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer pointlist = clij2.push(pointlistImagePlus);
ClearCLBuffer label_map_input = clij2.push(label_map_inputImagePlus);
label_map_destination = clij.create(pointlist);
float centerX = 1.0;
float centerY = 2.0;
float centerZ = 3.0;
```

```
// Execute operation on GPU
clij2.excludeLabelsOnSurface(clij, pointlist, label_map_input, label_map_destination, centerX, centerY, centerZ);
```

```
//show result
label_map_destinationImagePlus = clij2.pull(label_map_destination);
label_map_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(pointlist);
clij2.release(label_map_input);
clij2.release(label_map_destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
