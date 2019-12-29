
__kernel void masked_squared_sum_project_3d_3d(
    IMAGE_dst_TYPE dst,
    IMAGE_src_TYPE src,
    IMAGE_src_mask_TYPE src_mask,
    float mean_intensity
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  float sum = 0;
  for(int z = 0; z < GET_IMAGE_DEPTH(src); z++)
  {
    if (READ_Isrc_MAGE(src_mask,sampler,(int4)(x,y,z,0)).x > 0) {
        float value = READ_src_IMAGE(src,sampler,(int4)(x,y,z,0)).x;
        sum = sum + pow(value - mean_intensity, 2);
    }
  }
  WRITE_dst_IMAGE(dst,(int4)(x,y,0,0), CONVERT_dst_PIXEL_TYPE(sum));
}