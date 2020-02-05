

__kernel void maximum_of_masked_pixels_3d_2d(
    IMAGE_dst_max_TYPE dst_max,
    IMAGE_dst_mask_TYPE dst_mask,
    IMAGE_mask_TYPE mask,
    IMAGE_src_TYPE src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  float max = 0;
  float mask_value = 0;
  bool initial = true;
  for(int z = 0; z < GET_IMAGE_DEPTH(src); z++)
  {
    float binary = READ_mask_IMAGE(mask,sampler,(int4)(x,y,z,0)).x;

    if (binary != 0) {
        mask_value = 1;
        float value = READ_src_IMAGE(src,sampler,(int4)(x,y,z,0)).x;
        if (value > max || initial) {
          max = value;
          initial = false;
        }
    }
  }
  WRITE_dst_max_IMAGE(dst_max,(int2)(x,y), CONVERT_dst_max_PIXEL_TYPE(max));
  WRITE_dst_mask_IMAGE(dst_mask,(int2)(x,y), CONVERT_dst_mask_PIXEL_TYPE(mask_value));
}

