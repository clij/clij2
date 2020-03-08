__kernel void multiply_stack_with_scalars(
    IMAGE_src_TYPE  src,
    IMAGE_src_scalar_list_TYPE  src_scalar_list,
    IMAGE_dst_TYPE  dst
)
{
  const sampler_t sampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos3d = (int4){x,y,z,0};
  const POS_src_scalar_list_TYPE pos1d = POS_src_scalar_list_INSTANCE(z, 0, 0, 0);

  const float value1 = READ_src_IMAGE(src, sampler, pos3d).x;
  const float value2 = READ_src_scalar_list_IMAGE(src_scalar_list, sampler, pos1d).x;

  const float value = value1 * value2;

  WRITE_IMAGE (dst, pos3d, CONVERT_dst_PIXEL_TYPE(value));
}