## detectLabelEdges
![Image](images/mini_clijx_logo.png)

Takes a labelmap and returns an image where all pixels on label edges are set to 1 and all other pixels to 0.

### Usage in ImageJ macro
```
Ext.CLIJx_detectLabelEdges(Image label_map, Image touch_matrix_destination);
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
clijx.detectLabelEdges(clij, label_map, touch_matrix_destination);
```

```
//show result
touch_matrix_destinationImagePlus = clij.pull(touch_matrix_destination);
touch_matrix_destinationImagePlus.show();

// cleanup memory on GPU
label_map.close();
touch_matrix_destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
