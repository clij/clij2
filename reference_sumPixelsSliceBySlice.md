## sumPixelsSliceBySlice
![Image](images/mini_clij1_logo.png)

null

### Usage in ImageJ macro
```
Ext.CLIJ_sumPixelsSliceBySlice(null);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
```

```
// Execute operation on GPU
double[] resultSumPixelsSliceBySlice = clijx.sumPixelsSliceBySlice(clij, arg1);
```

```
//show result
System.out.println(resultSumPixelsSliceBySlice);

// cleanup memory on GPU
arg1.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
