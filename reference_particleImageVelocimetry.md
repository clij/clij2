## particleImageVelocimetry
![Image](images/mini_clijx_logo.png)

For every pixel in source image 1, determine the pixel with the most similar intensity in 
 the local neighborhood with a given radius in source image 2. Write the distance in 
X and Y in the two corresponding destination images.

### Usage in ImageJ macro
```
Ext.CLIJx_particleImageVelocimetry(Image source1, Image source2, Image destinationDeltaX, Image destinationDeltaY, Image destinationDeltaZ, Number maxDeltaX, Number maxDeltaY, Number maxDeltaZ, Boolean correctLocalShift);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
ClearCLBuffer arg2 = clijx.push(arg2ImagePlus);
ClearCLBuffer arg3 = clijx.push(arg3ImagePlus);
ClearCLBuffer arg4 = clijx.push(arg4ImagePlus);
ClearCLBuffer arg5 = clijx.push(arg5ImagePlus);
int arg6 = 10;
int arg7 = 20;
int arg8 = 30;
```

```
// Execute operation on GPU
clijx.particleImageVelocimetry(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
arg3.close();
arg4.close();
arg5.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
