## maskLabel
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Computes a masked image by applying a label mask to an image. All pixel values x of image X will be copied
to the destination image in case pixel value m at the same position in the label_map image has the right index value i.

f(x,m,i) = (x if (m == i); (0 otherwise))

### Usage in ImageJ macro
```
Ext.CLIJx_maskLabel(Image source, Image label_map, Image destination, Number label_index);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
ClearCLBuffer label_map = clij2.push(label_mapImagePlus);
destination = clij.create(source);
float label_index = 1.0;
```

```
// Execute operation on GPU
clij2.maskLabel(clij, source, label_map, destination, label_index);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(label_map);
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [intensity_per_label.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/intensity_per_label.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
