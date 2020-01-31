## labelSpots
![Image](images/mini_clijx_logo.png)

Transforms a spots image as resulting from maximum/minimum detection in an image of the same size where every spot has a number 1, 2, ... n.

### Usage in ImageJ macro
```
Ext.CLIJx_labelSpots(Image input_spots, Image labelled_spots_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer input_spots = clijx.push(input_spotsImagePlus);
labelled_spots_destination = clij.create(input_spots);
```

```
// Execute operation on GPU
clijx.labelSpots(clij, input_spots, labelled_spots_destination);
```

```
//show result
labelled_spots_destinationImagePlus = clij.pull(labelled_spots_destination);
labelled_spots_destinationImagePlus.show();

// cleanup memory on GPU
input_spots.close();
labelled_spots_destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
