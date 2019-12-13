## paste3D
![Image](images/mini_clijx_logo.png)

Pastes an image into another image at a given position.

### Usage in ImageJ macro
```
Ext.CLIJx_paste3D(Image source, Image destination, Number destinationX, Number destinationY, Number destinationZ);
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
int arg3 = 10;
int arg4 = 20;
int arg5 = 30;
```

```
// Execute operation on GPU
clijx.paste3D(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
