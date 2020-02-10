## labelToMask
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Masks a single label in a label map: Sets all pixels in the target image to 1, where the given label index was present in the label map. Other pixels are set to 0.

### Usage in ImageJ macro
```
Ext.CLIJx_labelToMask(Image label_map_source, Image mask_destination, Number label_index);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer label_map_source = clij2.push(label_map_sourceImagePlus);
mask_destination = clij.create(label_map_source);
float label_index = 1.0;
```

```
// Execute operation on GPU
clij2.labelToMask(clij, label_map_source, mask_destination, label_index);
```

```
//show result
mask_destinationImagePlus = clij2.pull(mask_destination);
mask_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(label_map_source);
clij2.release(mask_destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [measure_area_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measure_area_per_label.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [boundingBoxes.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/boundingBoxes.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
