## applyVectorfield
![Image](images/mini_clij1_logo.png)

Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 

### Usage in ImageJ macro
```
Ext.CLIJ_applyVectorfield(Image source, Image vectorX, Image vectorY, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
ClearCLBuffer vectorX = clijx.push(vectorXImagePlus);
ClearCLBuffer vectorY = clijx.push(vectorYImagePlus);
destination = clij.create(source);
```

```
// Execute operation on GPU
clijx.applyVectorfield(clij, source, vectorX, vectorY, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
source.close();
vectorX.close();
vectorY.close();
destination.close();
```




### Example scripts
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [applyVectorField.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/applyVectorField.py)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/"><img src="images/language_java.png" height="20"/></a> [ApplyVectorFieldDemo.java](https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/ApplyVectorFieldDemo.java)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
