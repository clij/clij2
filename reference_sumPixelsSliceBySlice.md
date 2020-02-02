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
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer arg1 = clij2.push(arg1ImagePlus);
```

```
// Execute operation on GPU
double[] resultSumPixelsSliceBySlice = clij2.sumPixelsSliceBySlice(clij, arg1);
```

```
//show result
System.out.println(resultSumPixelsSliceBySlice);

// cleanup memory on GPU
clij2.release(arg1);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
