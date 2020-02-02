__kernel void count_non_zero_projection_3d_2d(
    IMAGE_dst_TYPE dst,
    IMAGE_src_TYPE src,
    float tolerance
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  float count = 0;
  for(int z = 0; z < GET_IMAGE_DEPTH(src); z++)
  {
    POS_src_TYPE pos = POS_src_INSTANCE(x,y,z,0);
    float value = READ_src_IMAGE(src,sampler,pos).x;
    if (value > tolerance || value < -tolerance) {
        count = count + 1;
    }
  }
  POS_dst_TYPE pos2 = POS_dst_INSTANCE(x,y,0,0);
  WRITE_dst_IMAGE(dst, pos2, CONVERT_dst_PIXEL_TYPE(count));
}
