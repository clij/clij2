 #ifndef SAMPLER_FILTER
#define SAMPLER_FILTER CLK_FILTER_LINEAR
#endif

#ifndef SAMPLER_ADDRESS
#define SAMPLER_ADDRESS CLK_ADDRESS_CLAMP
#endif

__kernel void reslice_polar(
    IMAGE_dst_TYPE dst,
    IMAGE_src_TYPE src,
    float deltaAngle,
    float centerX,
    float centerY,
    float centerZ,
    float startInclinationDegrees,
    float startAzimuthDegrees,
    float scaleX,
    float scaleY,
    float scaleZ,
    IMAGE_mat_TYPE mat
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_TRUE | SAMPLER_ADDRESS |	SAMPLER_FILTER;

  int inclination = get_global_id(0);
  int azimuth = get_global_id(1);
  int radius = get_global_id(2);

  uint Nx = GET_IMAGE_WIDTH(src);
  uint Ny = GET_IMAGE_HEIGHT(src);
  uint Nz = GET_IMAGE_DEPTH(src);

  const float imageHalfWidth = GET_IMAGE_WIDTH(src) / 2;
  const float imageHalfHeight = GET_IMAGE_HEIGHT(src) / 2;

  float inclinationInRad = (((float)inclination) * deltaAngle + startInclinationDegrees) / 180.0 * M_PI;
  float azimuthInRad = (((float)azimuth) * deltaAngle + startAzimuthDegrees) / 180.0 * M_PI;

  const float sx = (centerX + cos(inclinationInRad) * (float)radius * scaleZ) + 0.5f;
  const float sy = (centerY + sin(inclinationInRad) * cos(azimuthInRad) * (float)radius * scaleY) + 0.5f;
  const float sz = (centerZ + sin(inclinationInRad) * sin(azimuthInRad) * (float)radius * scaleX) + 0.5f;

  float z2 = (mat[8] * sx + mat[9] * sy + mat[10] * sz + mat[11]);
  float y2 = (mat[4] * sx + mat[5] * sy + mat[6]  * sz + mat[7]);
  float x2 = (mat[0] * sx + mat[1] * sy + mat[2]  * sz + mat[3]);



  IMAGE_src_PIXEL_TYPE value = READ_src_IMAGE(src,sampler,(float4)(x2 / Nx, y2 / Ny, z2 / Nz, 0)).x;
  WRITE_dst_IMAGE(dst,(int4)(inclination, azimuth, radius, 0), CONVERT_dst_PIXEL_TYPE(value));
}
