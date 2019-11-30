## divideImages
![Image](images/mini_clij1_logo.png)

Divides two images X and Y by each other pixel wise.

<pre>f(x, y) = x / y</pre>

### Usage in ImageJ macro
```
Ext.CLIJ_divideImages(Image divident, Image divisor, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer divident = clijx.push(dividentImagePlus);
ClearCLBuffer divisor = clijx.push(divisorImagePlus);
destination = clij.create(divident);
```

```
// Execute operation on GPU
clijx.divideImages(clij, divident, divisor, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
divident.close();
divisor.close();
destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
