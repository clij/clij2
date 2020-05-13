 #ifndef SAMPLER_FILTER
#define SAMPLER_FILTER CLK_FILTER_LINEAR
#endif

#ifndef SAMPLER_ADDRESS
#define SAMPLER_ADDRESS CLK_ADDRESS_CLAMP
#endif

__kernel void reslice_radial_2d(
    IMAGE_dst_TYPE dst,
    IMAGE_src_TYPE src,
    float deltaAngle,
    float centerX,
    float centerY,
    float startAngleDegrees,
    float scaleX,
    float scaleY
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_TRUE | SAMPLER_ADDRESS |	SAMPLER_FILTER;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  uint Nx = GET_IMAGE_WIDTH(src);
  uint Ny = GET_IMAGE_HEIGHT(src);
  uint Nz = GET_IMAGE_DEPTH(src);

  const float imageHalfWidth = GET_IMAGE_WIDTH(src) / 2;
  const float imageHalfHeight = GET_IMAGE_HEIGHT(src) / 2;

  float angleInRad = (((float)z) * deltaAngle + startAngleDegrees) / 180.0 * M_PI;
  //float maxRadius = sqrt(pow(imageHalfWidth, 2.0f) + pow(imageHalfHeight, 2.0f));
  float radius = x;

  const float sx = (centerX + sin(angleInRad) * radius * scaleX) + 0.5f;
  const float sy = (centerY + cos(angleInRad) * radius * scaleY) + 0.5f;
  const float sz = y + 0.5f;

  IMAGE_src_PIXEL_TYPE value = READ_src_IMAGE(src,sampler,(float2)(sx / Nx, sy / Ny)).x;
  WRITE_dst_IMAGE(dst,(int4)(x, y, z, 0), CONVERT_dst_PIXEL_TYPE(value));
}