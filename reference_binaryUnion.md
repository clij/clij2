## binaryUnion
![Image](images/mini_clijx_logo.png)

Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
pixels x and y with the binary union operator |.
All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_binaryUnion(Image operand1, Image operand2, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer operand1 = clijx.push(operand1ImagePlus);
ClearCLBuffer operand2 = clijx.push(operand2ImagePlus);
destination = clij.create(operand1);
```

```
// Execute operation on GPU
clijx.binaryUnion(clij, operand1, operand2, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
operand1.close();
operand2.close();
destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
