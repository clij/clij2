
#ifndef SAMPLER_FILTER
#define SAMPLER_FILTER CLK_FILTER_LINEAR
#endif

#ifndef SAMPLER_ADDRESS
#define SAMPLER_ADDRESS CLK_ADDRESS_CLAMP
#endif

__kernel void apply_vectorfield_2d_interpolate(
    IMAGE_src_TYPE src,
    IMAGE_vectorX_TYPE vectorX,
    IMAGE_vectorY_TYPE vectorY,
    IMAGE_dst_TYPE dst
)
{
  const sampler_t sampler = CLK_NORMALIZED_COORDS_TRUE|
      SAMPLER_ADDRESS |	SAMPLER_FILTER;

  uint i = get_global_id(0);
  uint j = get_global_id(1);

  uint Nx = GET_IMAGE_WIDTH(src);
  uint Ny = GET_IMAGE_HEIGHT(src);

  float x = i+0.5f;
  float y = j+0.5f;


  int2 pos = (int2){i, j};

  float x2 = x + (float)(READ_vectorX_IMAGE(vectorX, sampler, pos).x);
  float y2 = y + (float)(READ_vectorY_IMAGE(vectorY, sampler, pos).x);


  float2 coord_norm = (float2)(x2 / Nx, y2 / Ny);

  float pix = 0;
  if (x2 >= 0 && y2 >= 0 &&
      x2 < GET_IMAGE_WIDTH(src) && y2 < GET_IMAGE_HEIGHT(src)
  ) {
    pix = (float)(READ_src_IMAGE(src, sampler, coord_norm).x);
  }


  WRITE_dst_IMAGE(dst, pos, CONVERT_dst_PIXEL_TYPE(pix));
}