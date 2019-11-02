__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void onlyzero_overwrite_maximum_box_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_OUT_3D flag_dst, DTYPE_IMAGE_IN_3D src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float foundMaximum = READ_IMAGE_3D(src, sampler, pos).x;
  if (foundMaximum == 0) {
    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        for (int z = -1; z <= 1; z++) {
          float value = READ_IMAGE_3D(src, sampler, (pos + (int4){x, y, z, 0})).x;
          if (value > foundMaximum) {
            foundMaximum = value;
          }
        }
      }
    }
  }
  WRITE_IMAGE_3D (dst, pos, CONVERT_DTYPE_OUT(foundMaximum));
}