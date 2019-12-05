
__kernel void maximum_y_projection(
    DTYPE_IMAGE_OUT_2D dst_max,
    DTYPE_IMAGE_IN_3D src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int z = get_global_id(1);
  float max = 0;
  for(int y = 0; y < GET_IMAGE_IN_HEIGHT(src); y++)
  {
    float value = READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
    if (value > max || y == 0) {
      max = value;
    }
  }
  WRITE_IMAGE_2D(dst_max,(int2)(x,z), CONVERT_DTYPE_OUT(max));
}