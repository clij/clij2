## connectedComponentsLabelingInplace
![Image](images/mini_clijx_logo.png)

Performs connected components analysis to a binary image and generates a label map.

### Usage in ImageJ macro
```
Ext.CLIJx_connectedComponentsLabelingInplace(Image binary_source_labeling_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
binary_source_labeling_destination = clij.create();
```

```
// Execute operation on GPU
clijx.connectedComponentsLabelingInplace(clij, binary_source_labeling_destination);
```

```
//show result
binary_source_labeling_destinationImagePlus = clij.pull(binary_source_labeling_destination);
binary_source_labeling_destinationImagePlus.show();

// cleanup memory on GPU
binary_source_labeling_destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
