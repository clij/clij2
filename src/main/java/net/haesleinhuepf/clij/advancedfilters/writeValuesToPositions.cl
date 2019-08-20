__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void write_values_to_positions_3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_2D src
)
{
  const int i = get_global_id(0);
  const int2 sourcePos = (int2)(i,0);

  const int x = READ_IMAGE_2D(src,sampler, (sourcePos + (int2){0, 0})).x;
  const int y = READ_IMAGE_2D(src,sampler, (sourcePos + (int2){0, 1})).x;
  const int z = READ_IMAGE_2D(src,sampler, (sourcePos + (int2){0, 2})).x;
  const DTYPE_IN v = READ_IMAGE_2D(src,sampler, (sourcePos + (int2){0, 3})).x;

  const int4 coord = (int4){x, y, z, 0};
  WRITE_IMAGE_3D(dst,coord, CONVERT_DTYPE_OUT(v));
}

__kernel void write_values_to_positions_2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src
)
{
  const int i = get_global_id(0);
  const int2 sourcePos = (int2)(i,0);

  const int x = READ_IMAGE_2D(src,sampler, (sourcePos + (int2){0, 0})).x;
  const int y = READ_IMAGE_2D(src,sampler, (sourcePos + (int2){0, 1})).x;
  const DTYPE_IN v = READ_IMAGE_2D(src,sampler, (sourcePos + (int2){0, 2})).x;

  const int2 coord = (int2){x, y};
  WRITE_IMAGE_2D(dst,coord, CONVERT_DTYPE_OUT(v));
}
