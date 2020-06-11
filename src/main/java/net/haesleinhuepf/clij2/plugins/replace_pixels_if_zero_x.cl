__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void replace_pixels_if_zero(
    IMAGE_src1_TYPE  src1,
    IMAGE_src1_TYPE  src2,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const POS_src1_TYPE pos1 = POS_src1_INSTANCE(x,y,z,0);
  const POS_src2_TYPE pos2 = POS_src2_INSTANCE(x,y,z,0);

  float value = READ_IMAGE(src1, sampler, pos1).x;
  if (value == 0) {
    value = READ_IMAGE(src2, sampler, pos2).x;
  }

  const POS_dst_TYPE pos = POS_dst_INSTANCE(x,y,z,0);
  WRITE_dst_IMAGE(dst, pos, CONVERT_dst_PIXEL_TYPE(value));
}
