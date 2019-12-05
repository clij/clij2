
__kernel void maximum_x_projection(
    DTYPE_IMAGE_OUT_2D dst_max,
    DTYPE_IMAGE_IN_3D src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int z = get_global_id(0);
  const int y = get_global_id(1);
  float max = 0;
  for(int x = 0; x < GET_IMAGE_IN_WIDTH(src); x++)
  {
    float value = READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
    if (value > max || x == 0) {
      max = value;
    }
  }
  WRITE_IMAGE_2D(dst_max,(int2)(z,y), CONVERT_DTYPE_OUT(max));
}