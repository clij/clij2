## organiseWindows
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Organises windows on screen.

### Usage in ImageJ macro
```
Ext.CLIJx_organiseWindows(Number startX, Number startY, Number tilesX, Number tilesY, Number tileWidth, Number tileHeight);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
int startX = 10;
int startY = 20;
int tilesX = 30;
int tilesY = 40;
int tileWidth = 50;
int tileHeight = 60;
```

```
// Execute operation on GPU
clij2.organiseWindows(clij, startX, startY, tilesX, tilesY, tileWidth, tileHeight);
```

```
//show result

// cleanup memory on GPU
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
