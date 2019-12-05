#ifndef SAMPLER_FILTER
#define SAMPLER_FILTER CLK_FILTER_LINEAR
#endif

#ifndef SAMPLER_ADDRESS
#define SAMPLER_ADDRESS CLK_ADDRESS_CLAMP
#endif

__kernel void radialProjection3d(
    DTYPE_IMAGE_OUT_3D dst,
    DTYPE_IMAGE_IN_3D src,
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

  const float imageHalfWidth = GET_IMAGE_IN_WIDTH(src) / 2;
  const float imageHalfHeight = GET_IMAGE_IN_HEIGHT(src) / 2;

  float angleInRad = (((float)z) * deltaAngle + startAngleDegrees) / 180.0 * M_PI;
  //float maxRadius = sqrt(pow(imageHalfWidth, 2.0f) + pow(imageHalfHeight, 2.0f));
  float radius = x;

  const float sx = (centerX + sin(angleInRad) * radius * scaleX);
  const float sy = (centerY + cos(angleInRad) * radius * scaleY);
  const float sz = y;

  DTYPE_IN value = READ_IMAGE_3D(src,sampler,(float4)(sx / Nx, sy / Ny, sz / Nz, 0)).x;
  WRITE_IMAGE_3D(dst,(int4)(x, y, z, 0), CONVERT_DTYPE_OUT(value));
}