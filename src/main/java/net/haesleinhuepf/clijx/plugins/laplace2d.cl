__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void laplace_diamond_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  float valueCenter = READ_IMAGE_2D(src, sampler, pos).x;
  float valueRight = READ_IMAGE_2D(src, sampler, (pos + (int2){1, 0})).x;
  float valueLeft = READ_IMAGE_2D(src, sampler, (pos + (int2){-1, 0})).x;
  float valueBottom = READ_IMAGE_2D(src, sampler, (pos + (int2){0, 1})).x;
  float valueTop = READ_IMAGE_2D(src, sampler, (pos + (int2){0, -1})).x;

  float result = valueCenter * 4.0 +
                valueRight * -1.0 +
                valueLeft * -1.0 +
                valueTop * -1.0 +
                valueBottom * -1.0;

  WRITE_IMAGE_2D (dst, pos, CONVERT_DTYPE_OUT(result));
}

__kernel void laplace_box_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  float valueCenter = READ_IMAGE_2D(src, sampler, pos).x;
  float valueRight = READ_IMAGE_2D(src, sampler, (pos + (int2){1, 0})).x;
  float valueLeft = READ_IMAGE_2D(src, sampler, (pos + (int2){-1, 0})).x;
  float valueBottom = READ_IMAGE_2D(src, sampler, (pos + (int2){0, 1})).x;
  float valueTop = READ_IMAGE_2D(src, sampler, (pos + (int2){0, -1})).x;
  float valueA = READ_IMAGE_2D(src, sampler, (pos + (int2){-1, -1})).x;
  float valueB = READ_IMAGE_2D(src, sampler, (pos + (int2){1, -1})).x;
  float valueC = READ_IMAGE_2D(src, sampler, (pos + (int2){-1, 1})).x;
  float valueD = READ_IMAGE_2D(src, sampler, (pos + (int2){1, 1})).x;

  float result = valueCenter * 8.0 +
                valueRight * -1.0 +
                valueLeft * -1.0 +
                valueTop * -1.0 +
                valueBottom * -1.0 +
                valueA * -1.0 +
                valueB * -1.0 +
                valueC * -1.0 +
                valueD * -1.0;

  WRITE_IMAGE_2D (dst, pos, CONVERT_DTYPE_OUT(result));
}