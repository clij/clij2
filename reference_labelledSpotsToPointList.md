## labelledSpotsToPointList
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Transforms a labelmap of spots (single pixels with values 1, 2, ..., n for n spots) as resulting from connected components analysis in an image where every column contains d 
pixels (with d = dimensionality of the original image) with the coordinates of the maxima/minima.

### Usage in ImageJ macro
```
Ext.CLIJx_labelledSpotsToPointList(Image input_labelled_spots, Image destination_pointlist);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer input_labelled_spots = clij2.push(input_labelled_spotsImagePlus);
destination_pointlist = clij.create(input_labelled_spots);
```

```
// Execute operation on GPU
clij2.labelledSpotsToPointList(clij, input_labelled_spots, destination_pointlist);
```

```
//show result
destination_pointlistImagePlus = clij2.pull(destination_pointlist);
destination_pointlistImagePlus.show();

// cleanup memory on GPU
clij2.release(input_labelled_spots);
clij2.release(destination_pointlist);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [rotating_sphere.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/rotating_sphere.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
