## readImageFromDisc
![Image](images/mini_clijx_logo.png)

Read an image from disc.

### Usage in ImageJ macro
```
Ext.CLIJx_readImageFromDisc(Image destination, String filename);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
```

```
// Execute operation on GPU
ClearCLBuffer resultReadImageFromDisc = clijx.readImageFromDisc(clij, arg1);
```

```
//show result
System.out.println(resultReadImageFromDisc);

// cleanup memory on GPU
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
