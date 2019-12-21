__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void iterative_mean_box_2d
(
  IMAGE_dst_TYPE dst, IMAGE_src_TYPE src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  float sum = READ_src_IMAGE(src, sampler, pos).x;
  sum = sum + READ_src_IMAGE(src, sampler, (pos + (int2){-1, 0})).x;
  sum = sum + READ_src_IMAGE(src, sampler, (pos + (int2){0, -1})).x;
  sum = sum + READ_src_IMAGE(src, sampler, (pos + (int2){1, 0})).x;
  sum = sum + READ_src_IMAGE(src, sampler, (pos + (int2){0, 1})).x;
  sum = sum + READ_src_IMAGE(src, sampler, (pos + (int2){-1, -1})).x;
  sum = sum + READ_src_IMAGE(src, sampler, (pos + (int2){1, -1})).x;
  sum = sum + READ_src_IMAGE(src, sampler, (pos + (int2){1, 1})).x;
  sum = sum + READ_src_IMAGE(src, sampler, (pos + (int2){-1, 1})).x;

  WRITE_dst_IMAGE (dst_edge_image, pos, CONVERT_dst_edge_image_PIXEL_TYPE(sum / 9.0));
}

