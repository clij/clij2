## labelSpots
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Transforms a spots image as resulting from maximum/minimum detection in an image of the same size where every spot has a number 1, 2, ... n.

### Usage in ImageJ macro
```
Ext.CLIJx_labelSpots(Image input_spots, Image labelled_spots_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer input_spots = clij2.push(input_spotsImagePlus);
labelled_spots_destination = clij.create(input_spots);
```

```
// Execute operation on GPU
clij2.labelSpots(clij, input_spots, labelled_spots_destination);
```

```
//show result
labelled_spots_destinationImagePlus = clij2.pull(labelled_spots_destination);
labelled_spots_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(input_spots);
clij2.release(labelled_spots_destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
