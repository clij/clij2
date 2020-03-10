// Author: Robert Haase
//         March 2020
//

__kernel void flood_fill_diamond(
    IMAGE_src_TYPE src,
    IMAGE_flag_dst_TYPE flag_dst,
    IMAGE_dst_TYPE dst,
    const float value_to_replace,
    const float value_replacement,
    int dimension
)
{
  const sampler_t sampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = (dimension == 3)?get_global_id(2):0;

  const POS_src_TYPE pos = POS_src_INSTANCE(x, y, z, 0);

  float pixel = READ_IMAGE(src, sampler, pos).x;
  if (pixel != value_to_replace) {
    WRITE_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(pixel));
    return; // pixel is not changed.
  }

  bool doReplace = false;

  if(READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(-1, 0, 0, 0)).x == value_replacement ) {
    doReplace = true;
  }
  if (!doReplace && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(1, 0, 0, 0)).x == value_replacement ) {
    doReplace = true;
  }
  if (!doReplace && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(0, -1, 0, 0)).x == value_replacement ) {
    doReplace = true;
  }
  if (!doReplace && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(0, 1, 0, 0)).x == value_replacement ) {
    doReplace = true;
  }

  if (dimension == 3) {
      if (!doReplace && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(0, 0, -1, 0)).x == value_replacement ) {
        doReplace = true;
      }
      if (!doReplace && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(0, 0, 1, 0)).x == value_replacement ) {
        doReplace = true;
      }
  }

  if(doReplace) {
    WRITE_IMAGE (flag_dst, POS_flag_dst_INSTANCE(0, 0, 0, 0), 1);
    WRITE_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(value_replacement));
  } else {
    WRITE_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(pixel));
  }


}
