__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void minimalistic_nonzero_minimum_diamond_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_OUT_3D flag_dst, DTYPE_IMAGE_IN_2D src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  float foundMinimum = READ_IMAGE_2D(src, sampler, pos).x;
  if (foundMinimum != 0) {
      float originalValue = foundMinimum;
      float value = READ_IMAGE_2D(src, sampler, (pos + (int2){1, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_IMAGE_2D(src, sampler, (pos + (int2){-1, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_IMAGE_2D(src, sampler, (pos + (int2){0, 1})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_IMAGE_2D(src, sampler, (pos + (int2){0, -1})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }

      if (foundMinimum != originalValue) {
        WRITE_IMAGE_3D(flag_dst,(int4)(0,0,0,0),1);
      }
      WRITE_IMAGE_2D (dst, pos, CONVERT_DTYPE_OUT(foundMinimum));
  }
}

__kernel void minimalistic_nonzero_maximum_diamond_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_OUT_3D flag_dst, DTYPE_IMAGE_IN_2D src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  float foundMaximum = READ_IMAGE_2D(src, sampler, pos).x;
  if (foundMaximum != 0) {
      float originalValue = foundMaximum;
      float value = READ_IMAGE_2D(src, sampler, (pos + (int2){1, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_IMAGE_2D(src, sampler, (pos + (int2){-1, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_IMAGE_2D(src, sampler, (pos + (int2){0, 1})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_IMAGE_2D(src, sampler, (pos + (int2){0, -1})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }

      if (foundMaximum != originalValue) {
        WRITE_IMAGE_3D(flag_dst,(int4)(0,0,0,0),1);
      }
      WRITE_IMAGE_2D (dst, pos, CONVERT_DTYPE_OUT(foundMaximum));
  }
}

__kernel void onlyzero_overwrite_maximum_diamond_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_OUT_3D flag_dst, DTYPE_IMAGE_IN_2D src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  float foundMaximum = READ_IMAGE_2D(src, sampler, pos).x;
  if (foundMaximum == 0) {
      float originalValue = foundMaximum;
      float value = READ_IMAGE_2D(src, sampler, (pos + (int2){1, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_IMAGE_2D(src, sampler, (pos + (int2){-1, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_IMAGE_2D(src, sampler, (pos + (int2){0, 1})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_IMAGE_2D(src, sampler, (pos + (int2){0, -1})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }

      if (foundMaximum != originalValue) {
        WRITE_IMAGE_3D(flag_dst,(int4)(0,0,0,0),1);
      }
  }
  WRITE_IMAGE_2D (dst, pos, CONVERT_DTYPE_OUT(foundMaximum));
}