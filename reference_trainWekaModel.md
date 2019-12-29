## trainWekaModel
![Image](images/mini_clijx_logo.png)

By Robert Haase (rhaase@mpi-cbg.de, based on work by 
 Verena Kaynig (verena.kaynig@inf.ethz.ch), 
 Ignacio Arganda-Carreras (iarganda@mit.edu),
 Albert Cardona (acardona@ini.phys.ethz.ch))

Trains a Weka model using functionality of Fijis Trainable Weka Segmentation plugin.
It takes a 3D feature stack (e.g. first plane original image, second plane blurred, third plane edge image)and trains a Weka model. This model will be saved to disc.
The given groundTruth image is supposed to be a label map where pixels with value 1 represent class 1, pixels with value 2 represent class 2 and so on. Pixels with value 0 will be ignored for training.

### Usage in ImageJ macro
```
Ext.CLIJx_trainWekaModel(Image featureStack3D, Image groundTruth2D, String saveModelFilename);
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
```

```
// Execute operation on GPU
clijx.trainWekaModel(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### License terms
Parts of the code here were copied over from the Trainable_Segmentation repository (link above). Thus,  
 this code is licensed GPL2 as well.  
  
  License: GPL  
  
  This program is free software; you can redistribute it and/or  
  modify it under the terms of the GNU General Public License 2  
  as published by the Free Software Foundation.  
  
  This program is distributed in the hope that it will be useful,  
  but WITHOUT ANY WARRANTY; without even the implied warranty of  
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the  
  GNU General Public License for more details.  
  
  You should have received a copy of the GNU General Public License  
  along with this program; if not, write to the Free Software  
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.  
  Authors: Verena Kaynig (verena.kaynig@inf.ethz.ch), Ignacio Arganda-Carreras (iarganda@mit.edu)  
           Albert Cardona (acardona@ini.phys.ethz.ch)

[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
