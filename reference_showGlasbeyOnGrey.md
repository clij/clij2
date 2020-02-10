## showGlasbeyOnGrey
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Visualises two 2D images as one RGB image. The first channel is shown in grey, the second with glasbey LUT.

### Usage in ImageJ macro
```
Ext.CLIJx_showGlasbeyOnGrey(Image red, Image labelling, String title);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer red = clij2.push(redImagePlus);
ClearCLBuffer labelling = clij2.push(labellingImagePlus);
```

```
// Execute operation on GPU
clij2.showGlasbeyOnGrey(clij, red, labelling, title);
```

```
//show result

// cleanup memory on GPU
clij2.release(red);
clij2.release(labelling);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
