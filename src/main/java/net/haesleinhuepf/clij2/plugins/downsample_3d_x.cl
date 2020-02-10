__kernel void downsample_3d(
    IMAGE_dst_TYPE dst,
    IMAGE_src_TYPE src,
    float factor_x,
    float factor_y,
    float factor_z
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_id(2);

  const int sx = factor_x * dx;
  const int sy = factor_y * dy;
  const int sz = factor_z * dz;
  const float out = READ_src_IMAGE(src,sampler,((int4){sx,sy,sz,0})).x;
  WRITE_dst_IMAGE(dst,((int4){dx,dy,dz,0}), CONVERT_dst_PIXEL_TYPE(out));
}
