__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void laplace_diamond_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float valueCenter = READ_IMAGE_3D(src, sampler, pos).x;
  float valueRight = READ_IMAGE_3D(src, sampler, (pos + (int4){1, 0, 0, 0})).x;
  float valueLeft = READ_IMAGE_3D(src, sampler, (pos + (int4){-1, 0, 0, 0})).x;
  float valueBottom = READ_IMAGE_3D(src, sampler, (pos + (int4){0, 1, 0, 0})).x;
  float valueTop = READ_IMAGE_3D(src, sampler, (pos + (int4){0, -1, 0, 0})).x;
  float valueFront = READ_IMAGE_3D(src, sampler, (pos + (int4){0, 0, 1, 0})).x;
  float valueBack = READ_IMAGE_3D(src, sampler, (pos + (int4){0, 0, -1, 0})).x;

  float result = valueCenter * 6.0 +
                valueRight * -1.0 +
                valueLeft * -1.0 +
                valueTop * -1.0 +
                valueBottom * -1.0 +
                valueFront * -1.0 +
                valueBack * -1.0;

  WRITE_IMAGE_3D (dst, pos, CONVERT_DTYPE_OUT(result));
}

__kernel void laplace_box_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float result = 0;
  for (int ax = -1; ax <= 1; ax++) {
    for (int ay = -1; ay <= 1; ay++) {
      for (int az = -1; az <= 1; az++) {
        if (ax == 0 && ay == 0 && az == 0) {
          result = result + READ_IMAGE_3D(src, sampler, pos).x;
        } else {
          result = result + READ_IMAGE_3D(src, sampler, (pos + (int4){ax, ay, az, 0})).x * -1;
        }
      }
    }
  }

  WRITE_IMAGE_3D (dst, pos, CONVERT_DTYPE_OUT(result));
}
