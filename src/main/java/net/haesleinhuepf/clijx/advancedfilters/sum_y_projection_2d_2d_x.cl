

__kernel void sum_y_projection_2d_2d(
    IMAGE_dst_TYPE dst,
    IMAGE_src_TYPE src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int z = get_global_id(1);
  float sum = 0;
  for(int y = 0; y < GET_IMAGE_HEIGHT(src); z++)
  {
    sum = sum + READ_src_IMAGE(src,sampler,(int2)(x,y)).x;
  }
  WRITE_dst_IMAGE(dst,(int2)(x,z), CONVERT_dst_PIXEL_TYPE(sum));
}
