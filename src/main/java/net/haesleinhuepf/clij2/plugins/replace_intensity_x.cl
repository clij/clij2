__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void replace_intensity
(
  IMAGE_dst_OUT dst,
  IMAGE_src_IN src,
  const float in,
  const float out
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);
  const int k = get_global_id(2);

  const int w = GET_IMAGE_WIDTH(src);
  const int h = GET_IMAGE_HEIGHT(src);
  const int d = GET_IMAGE_DEPTH(src);

  float pixelindex = i * h * d + j * d + k;
  float value = (float)(READ_src_IMAGE(src,sampler,(int4)(i,j,k,0)).x);
  if (fabs(value - in) < 0.1) {
    WRITE_dst_IMAGE(dst, (int4)(i,j,k,0), CONVERT_dst_PIXEL_TYPE(out));
  } else {
    WRITE_dst_IMAGE(dst, (int4)(i,j,k,0), CONVERT_dst_PIXEL_TYPE(value));
  }
}

