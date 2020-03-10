## skeletonize
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

By Robert Haase translated original work by Ignacio Arganda-Carreras

Erodes a binary image until just its skeleton is left. The result is similar to Skeletonize3D in Fiji.

### Usage in ImageJ macro
```
Ext.CLIJx_skeletonize(Image source, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source = clij2.push(sourceImagePlus);
destination = clij.create(source);
```

```
// Execute operation on GPU
clij2.skeletonize(clij, source, destination);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [skeleton.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/skeleton.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [skeleton3D.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/skeleton3D.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [benchmarkSkeletonize.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/benchmarkSkeletonize.ijm)  




### License terms
  
 Skeletonize3D plugin for ImageJ(C).  
 Copyright (C) 2008 Ignacio Arganda-Carreras   
   
 This program is free software; you can redistribute it and/or  
 modify it under the terms of the GNU General Public License  
 as published by the Free Software Foundation (http://www.gnu.org/licenses/gpl.txt )  
  
 This program is distributed in the hope that it will be useful,  
 but WITHOUT ANY WARRANTY; without even the implied warranty of  
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the  
 GNU General Public License for more details.  
   
 You should have received a copy of the GNU General Public License  
 along with this program; if not, write to the Free Software  
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.  
   


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
