## particleImageVelocimetryTimelapse
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Run particle image velocimetry on a 2D+t timelapse.

### Usage in ImageJ macro
```
Ext.CLIJx_particleImageVelocimetryTimelapse(Image source, Image destinationDeltaX, Image destinationDeltaY, Image destinationDeltaZ, Number maxDeltaX, Number maxDeltaY, Number maxDeltaZ, Boolean correctLocalShift);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
destinationDeltaX = clij.create(source);
destinationDeltaY = clij.create(source);
destinationDeltaZ = clij.create(source);
int maxDeltaX = 10;
int maxDeltaY = 20;
int maxDeltaZ = 30;
boolean correctLocalShift = true;
```

```
// Execute operation on GPU
clij2.particleImageVelocimetryTimelapse(clij, source, destinationDeltaX, destinationDeltaY, destinationDeltaZ, maxDeltaX, maxDeltaY, maxDeltaZ, correctLocalShift);
```

```
//show result
destinationDeltaXImagePlus = clij2.pull(destinationDeltaX);
destinationDeltaXImagePlus.show();
destinationDeltaYImagePlus = clij2.pull(destinationDeltaY);
destinationDeltaYImagePlus.show();
destinationDeltaZImagePlus = clij2.pull(destinationDeltaZ);
destinationDeltaZImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(destinationDeltaX);
clij2.release(destinationDeltaY);
clij2.release(destinationDeltaZ);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
