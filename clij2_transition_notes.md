

breaking backwards compatibility:

meanSphere and meanSliceBySliceSphere had kernel sizes as parameter (java)
mean2DSphere, mean3DSphere and MeanSliceBySliceSphere3D have radii as parameters
for macro users everythin stays the same

meanIJ was removed. Use meanSphere2D or meanSphere3D instead