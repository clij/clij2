
__kernel void copy_slice_from_3d(
    IMAGE_dst_TYPE dst, 
    IMAGE_src_TYPE src, 
    int slice
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);

  const POS_src_TYPE pos4 = POS_src_INSTANCE(dx, dy, slice, 0);
  const POS_dst_TYPE pos2 = POS_dst_INSTANCE(dx, dy, 0, 0);


  const float out = READ_src_IMAGE(src,sampler,pos4).x;
  WRITE_dst_IMAGE(dst,pos2, CONVERT_dst_PIXEL_TYPE(out));
}
