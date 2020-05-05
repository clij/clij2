# CLIJ2 documentation

This page is under construction. Links may change

# CLIJ2: GPU-accelerated image processing for everyone
## Introduction
CLIJ is an OpenCL - ImageJ bridge and a [Fiji](https://fiji.sc/) plugin allowing users with entry-level skills in programming 
to build GPU-accelerated workflows to speed up their image processing. Increased efforts were put on documentation, code examples, interoperability, and extensibility.
CLIJ is based on 
[ClearCL](http://github.com/ClearControl/ClearCL), 
[JOCL](http://github.com/gpu/JOCL), 
[Imglib2](https://github.com/imglib), 
[ImageJ](http://imagej.net) and 
[SciJava](https://github.com/SciJava).

**CLIJ2 is build on CLIJ. If you use it, please cite it:**

Robert Haase, Loic Alain Royer, Peter Steinbach, Deborah Schmidt, 
Alexandr Dibrov, Uwe Schmidt, Martin Weigert, Nicola Maghelli, Pavel Tomancak, 
Florian Jug, Eugene W Myers. 
*CLIJ: GPU-accelerated image processing for everyone*. [Nat Methods 17, 5-6 (2020) doi:10.1038/s41592-019-0650-1](https://doi.org/10.1038/s41592-019-0650-1)

[Older version in BioRxiv](https://doi.org/10.1101/660704)

If you search for support, please open a thread on the [image.sc](https://image.sc) forum.

[![Image.sc forum](https://img.shields.io/badge/dynamic/json.svg?label=forum&url=https%3A%2F%2Fforum.image.sc%2Ftags%2Fclij.json&query=%24.topic_list.tags.0.topic_count&colorB=brightgreen&suffix=%20topics&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAABPklEQVR42m3SyyqFURTA8Y2BER0TDyExZ+aSPIKUlPIITFzKeQWXwhBlQrmFgUzMMFLKZeguBu5y+//17dP3nc5vuPdee6299gohUYYaDGOyyACq4JmQVoFujOMR77hNfOAGM+hBOQqB9TjHD36xhAa04RCuuXeKOvwHVWIKL9jCK2bRiV284QgL8MwEjAneeo9VNOEaBhzALGtoRy02cIcWhE34jj5YxgW+E5Z4iTPkMYpPLCNY3hdOYEfNbKYdmNngZ1jyEzw7h7AIb3fRTQ95OAZ6yQpGYHMMtOTgouktYwxuXsHgWLLl+4x++Kx1FJrjLTagA77bTPvYgw1rRqY56e+w7GNYsqX6JfPwi7aR+Y5SA+BXtKIRfkfJAYgj14tpOF6+I46c4/cAM3UhM3JxyKsxiOIhH0IO6SH/A1Kb1WBeUjbkAAAAAElFTkSuQmCC)](https://forum.image.sc/tags/clij)

<img src="./images/clij_bridge.gif" width="245"> <img src="./images/clicy_bridge2.gif" width="245">

* Overview
  * Introduction
  * Installation
    * [Fiji update site](https://clij.github.io/clij2-docs/installationInFiji)
    * [Icy](https://github.com/clij/clicy)
    * [Matlab](https://github.com/clij/clatlab)
  * [Reference](https://clij.github.io/clij2-docs/reference)
  * [Cheat sheets](https://clij.github.io/clij2-docs/CLIJ2-cheatsheet_V3.pdf)
  * [Release notes](https://github.com/clij/clij2/releases)
  * [Release cycle](https://clij.github.io/clij2-docs/release_cycle)
  * [Future perspective: clEsperanto](http://clesperanto.net)
  
* CLIJ versus CLIJ2 
  * [CLIJ documentation (archived)](https://github.com/clij/clij-docs)
  * [What's different between CLIJ1, CLIJ2 and CLIJx?](https://clij.github.io/clij2-docs/clij12xAPIcomparison)
  * [CLIJ - CLIJ2 transition guide](clij2_transition_notes)

* Tutorials
  * [Basics](https://clij.github.io/clij2-docs/md/basics/)
  * [Combining CLIJ and CLIJ2](https://clij.github.io/clij2-docs/md/clij1_clij2_combination/)
  * Filtering and processing images
    * [Crop and paste images](https://clij.github.io/clij2-docs/md/crop_and_paste/)
    * [Gaussian blur](https://clij.github.io/clij2-docs/md/blur/)
    * [Maximum projections](https://clij.github.io/clij2-docs/md/maximumProjection/)
    * [Warp images](https://clij.github.io/clij2-docs/md/applyVectorFieldMD/)
    * [Drosophila embryo cell counting](https://clij.github.io/clij2-docs/md/drosophila_max_cylinder_projection/)

  * Segmentation and labelling
    * [Labeling](https://clij.github.io/clij2-docs/md/labeling/)
    * [Binary images](https://clij.github.io/clij2-docs/md/binary_processing/)
    * [Working with regions of interest (ROIs)](https://clij.github.io/clij2-docs/md/working_with_rois/)
    * [Voronoi diagrams](https://clij.github.io/clij2-docs/md/voronoi/)

  * Working with matrices and graphs
    * [Mulitiply vectors and matrices](https://clij.github.io/clij2-docs/md/multiply_vectors_matrices)
    * [Matrix multiplication](https://clij.github.io/clij2-docs/md/matrix_multiply/)
    * [Spots, pointlists, matrices and tables](https://clij.github.io/clij2-docs/md/spots_pointlists_matrices_tables/)
    * [Filtering in graphs](https://clij.github.io/clij2-docs/md/filtering_in_graphs/)
    * [Neighbors of neighbors](https://clij.github.io/clij2-docs/md/neighbors_of_neighbors)
    * [Tribolium embryo morphometry](https://clij.github.io/clij2-docs/md/tribolium_morphometry/)
    * [Superpixel segmentation](https://clij.github.io/clij2-docs/md/superpixel_segmentation/)
    
  * Statistics and measurements
    * [Images statistics](https://clij.github.io/clij2-docs/md/image_statistics/)
    * [Pixel statistics on labelled images](https://clij.github.io/clij2-docs/md/measure_statistics/)
    * [Colocalisation measurements using Jaccard index and Sorensen/Dice coefficient](https://clij.github.io/clij2-docs/md/measure_overlap/)

  * Benchmarking
    * [Measure speedup](https://clij.github.io/clij2-docs/md/benchmarking/)
    * [Detailed time tracing](https://clij.github.io/clij2-docs/md/time_tracing/)
    * [Comparing and benchmarking workflows](https://clij.github.io/clij2-docs/md/compare_workflows/)
    * [Comparing image rotation ImageJ vs. CLIJ](https://clij.github.io/clij2-docs/md/rotate_comparison/)
    
* Example code
  * [ImageJ macro](https://github.com/clij/clij2-docs/tree/master/src/main/macro)
  * [Icy javascript](https://github.com/clij/clicy/tree/master/src/main/javascript)
  * [Icy protocols](https://github.com/clij/clicy/tree/master/src/main/protocols)
  * [Matlab](https://github.com/clij/clatlab/tree/master/src/main/matlab)
* FAQ / support
  * [Frequently asked questions](https://clij.github.io/clij-docs/faq)
  * [Troubleshooting](https://clij.github.io/clij-docs/troubleshooting)
  * [Support](https://image.sc)
  * [Imprint](https://clij.github.io/imprint)

## Acknowledgements
Development of CLIJ is a community effort. We would like to thank everybody who helped developing and testing. In particular thanks goes to
Alex Herbert (University of Sussex),
Bram van den Broek (Netherlands Cancer Institute),
Brenton Cavanagh (RCSI),
Brian Northan (True North Intelligent Algorithms),
Bruno C. Vellutini (MPI CBG),
Curtis Rueden (UW-Madison LOCI),
Damir Krunic (DKFZ),
Daniela Vorkel (MPI CBG),
Daniel J. White (GE),
Gaby G. Martins (IGC),
Guillaume Witz (Bern University),
Si&acirc;n Culley (LMCB MRC),
Giovanni Cardone (MPI Biochem),
Jan Brocher (Biovoxxel), 
Jean-Yves Tinevez (Institute Pasteur),
Johannes Girstmair (MPI CBG),
Juergen Gluch (Fraunhofer IKTS),
Kota Miura,
Laurent Thomas (Acquifer), 
Matthew Foley (University of Sydney),
Matthias Arzt (MPI-CBG),
Nico Stuurman (UCSF),
Peter Haub,
Pete Bankhead (University of Edinburgh),
Pit Kludig,
Pradeep Rajasekhar (Monash University),
Ruth Whelan-Jeans,
Tanner Fadero (UNC-Chapel Hill),
Thomas Irmer (Zeiss),
Tobias Pietzsch (MPI-CBG),
Wilson Adams (VU Biophotonics)

R.H. was supported by the German Federal Ministry of
Research and Education (BMBF) under the code 031L0044
(Sysbio II) and D.S. received support from the German
Research Foundation (DFG) under the code JU3110/1-1.
P.T. was supported by the European Regional
Development Fund in the IT4Innovations national
supercomputing center-path to exascale project,
project number CZ.02.1.01/0.0/0.0/16_013/0001791
within the Operational Programme Research, Development
and Education.

[Imprint](https://clij.github.io/imprint)
  


