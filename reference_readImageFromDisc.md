## readImageFromDisc
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)![Image](images/mini_clijx_logo.png)

Read an image from disc.

### Usage in ImageJ macro
```
Ext.CLIJx_readImageFromDisc(Image destination, String filename);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
```

```
// Execute operation on GPU
ClearCLBuffer resultReadImageFromDisc = clij2.readImageFromDisc(clij, arg1);
```

```
//show result
System.out.println(resultReadImageFromDisc);

// cleanup memory on GPU
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
