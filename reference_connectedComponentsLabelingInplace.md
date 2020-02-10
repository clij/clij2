## connectedComponentsLabelingInplace
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Performs connected components analysis to a binary image and generates a label map.

### Usage in ImageJ macro
```
Ext.CLIJx_connectedComponentsLabelingInplace(Image binary_source_labeling_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
binary_source_labeling_destination = clij.create();
```

```
// Execute operation on GPU
clij2.connectedComponentsLabelingInplace(clij, binary_source_labeling_destination);
```

```
//show result
binary_source_labeling_destinationImagePlus = clij2.pull(binary_source_labeling_destination);
binary_source_labeling_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(binary_source_labeling_destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
