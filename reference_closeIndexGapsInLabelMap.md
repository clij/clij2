## closeIndexGapsInLabelMap
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Analyses a label map and if there are gaps in the indexing (e.g. label 5 is not present) all 
subsequent labels will be relabelled. Thus, afterwards number of labels and maximum label index are equal.


### Usage in ImageJ macro
```
Ext.CLIJx_closeIndexGapsInLabelMap(Image binary_input, Image labeling_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer binary_input = clij2.push(binary_inputImagePlus);
labeling_destination = clij.create(binary_input);
```

```
// Execute operation on GPU
clij2.closeIndexGapsInLabelMap(clij, binary_input, labeling_destination);
```

```
//show result
labeling_destinationImagePlus = clij2.pull(labeling_destination);
labeling_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(binary_input);
clij2.release(labeling_destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
