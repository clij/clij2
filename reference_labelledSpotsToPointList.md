## labelledSpotsToPointList
![Image](images/mini_clijx_logo.png)

Transforms a labelmap of spots (single pixels with values 1, 2, ..., n for n spots) as resulting from connected components analysis in an image where every column contains d 
pixels (with d = dimensionality of the original image) with the coordinates of the maxima/minima.

### Usage in ImageJ macro
```
Ext.CLIJx_labelledSpotsToPointList(Image input_labelled_spots, Image destination_pointlist);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer input_labelled_spots = clijx.push(input_labelled_spotsImagePlus);
destination_pointlist = clij.create(input_labelled_spots);
```

```
// Execute operation on GPU
clijx.labelledSpotsToPointList(clij, input_labelled_spots, destination_pointlist);
```

```
//show result
destination_pointlistImagePlus = clij.pull(destination_pointlist);
destination_pointlistImagePlus.show();

// cleanup memory on GPU
input_labelled_spots.close();
destination_pointlist.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
