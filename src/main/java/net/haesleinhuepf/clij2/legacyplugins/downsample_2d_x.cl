
__kernel void downsample_2d(
    IMAGE_dst_TYPE dst,
    IMAGE_src_TYPE src,
    float factor_x,
    float factor_y
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);

  const int sx = factor_x * dx;
  const int sy = factor_y * dy;
  const float out = READ_src_IMAGE(src,sampler,((int2){sx,sy})).x;
  WRITE_dst_IMAGE(dst,((int2){dx,dy}), CONVERT_dst_PIXEL_TYPE(out));
}
