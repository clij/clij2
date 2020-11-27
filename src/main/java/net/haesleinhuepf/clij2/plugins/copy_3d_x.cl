__kernel void copy_3d (
    IMAGE_dst_TYPE dst, 
    IMAGE_src_TYPE src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_id(2);

  const POS_src_TYPE pos_src = POS_src_INSTANCE(dx, dy, dz, 0);
  const POS_dst_TYPE pos_dst = POS_dst_INSTANCE(dx, dy, dz, 0);

  const float out = READ_src_IMAGE(src,sampler,pos_src).x;
  WRITE_dst_IMAGE(dst, pos_dst, CONVERT_dst_PIXEL_TYPE(out));
}
