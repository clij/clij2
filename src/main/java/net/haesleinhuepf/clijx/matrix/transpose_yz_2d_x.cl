__kernel void transpose_yz_2d (
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const sampler_t intsampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 spos = (int2)(x, y);
  const int4 tpos = (int4)(x, 0, y,0);

  float value = READ_src_IMAGE(src, intsampler, spos).x;

  WRITE_dst_IMAGE (dst, tpos, CONVERT_dst_PIXEL_TYPE(value));
}
