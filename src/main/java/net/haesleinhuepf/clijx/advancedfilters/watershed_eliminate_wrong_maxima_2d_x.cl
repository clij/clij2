__kernel void watershed_eliminate_wrong_maxima_2d
(
  IMAGE_src_maxima_TYPE src_maxima,
  IMAGE_src_distancemap_TYPE src_distancemap,
  IMAGE_dst_maxima_TYPE dst_maxima,
  IMAGE_flag_dst_TYPE flag_dst
)
{
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  float was_maximum = READ_src_maxima_IMAGE(src_maxima, sampler, pos).x;
  float value = READ_src_distancemap_IMAGE(src_distancemap, sampler, pos).x;
  float is_maximum = was_maximum;

  if (is_maximum > 0) {
      for (int ax = -1; ax <= 1; ax++) {
        for (int ay = -1; ay <= 1; ay++) {
          if (ax != 0 || ay != 0) {
            float other_is_maximum = READ_src_maxima_IMAGE(src_maxima, sampler, (pos + (int2){ax, ay})).x;
            float other_value = READ_src_distancemap_IMAGE(src_distancemap, sampler, (pos + (int2){ax, ay})).x;
            if (other_value > 0 && (! (fabs(other_value - value) > 0))) {
              if (other_is_maximum < 1) {
                is_maximum = 0;
              }
            }
          }
        }
      }

    if (fabs(((float)is_maximum - was_maximum)) > 0) {
      WRITE_flag_dst_IMAGE(flag_dst,(int4)(0,0,0,0),1);
    }
  }
  WRITE_dst_maxima_IMAGE (dst_maxima, pos, CONVERT_dst_maxima_PIXEL_TYPE(is_maximum));
}
