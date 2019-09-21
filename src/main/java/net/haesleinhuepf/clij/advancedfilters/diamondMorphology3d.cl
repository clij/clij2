__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void minimalistic_nonzero_minimum_diamond_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_OUT_3D flag_dst, DTYPE_IMAGE_IN_3D src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float foundMinimum = READ_IMAGE_3D(src, sampler, pos).x;
  if (foundMinimum != 0) {
      float originalValue = foundMinimum;
      float value = READ_IMAGE_3D(src, sampler, (pos + (int4){1, 0, 0, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_IMAGE_3D(src, sampler, (pos + (int4){-1, 0, 0, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_IMAGE_3D(src, sampler, (pos + (int4){0, 1, 0, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_IMAGE_3D(src, sampler, (pos + (int4){0, -1, 0, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_IMAGE_3D(src, sampler, (pos + (int4){0, 0, 1, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_IMAGE_3D(src, sampler, (pos + (int4){0, 0, -1, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }

      if (foundMinimum != originalValue) {
        WRITE_IMAGE_3D(flag_dst,(int4)(0,0,0,0),1);
      }
      WRITE_IMAGE_3D (dst, pos, CONVERT_DTYPE_OUT(foundMinimum));
  }
}