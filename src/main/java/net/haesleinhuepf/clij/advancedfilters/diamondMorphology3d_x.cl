__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void minimalistic_nonzero_minimum_diamond_image3d
(
  IMAGE_dst_TYPE dst, IMAGE_flag_dst_TYPE flag_dst, IMAGE_src_TYPE src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float foundMinimum = READ_src_IMAGE(src, sampler, pos).x;
  if (foundMinimum != 0) {
      float originalValue = foundMinimum;
      float value = READ_src_IMAGE(src, sampler, (pos + (int4){1, 0, 0, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){-1, 0, 0, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 1, 0, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){0, -1, 0, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 0, 1, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 0, -1, 0})).x;
      if ( value < foundMinimum && value > 0) {
        foundMinimum = value;
      }

      if (foundMinimum != originalValue) {
        WRITE_flag_dst_IMAGE(flag_dst,(int4)(0,0,0,0),1);
      }
      WRITE_dst_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(foundMinimum));
  }
}

__kernel void minimalistic_nonzero_maximum_diamond_image3d
(
  IMAGE_dst_TYPE dst, IMAGE_flag_dst_TYPE flag_dst, IMAGE_src_TYPE src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float foundMaximum = READ_src_IMAGE(src, sampler, pos).x;
  if (foundMaximum != 0) {
      float originalValue = foundMaximum;
      float value = READ_src_IMAGE(src, sampler, (pos + (int4){1, 0, 0, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){-1, 0, 0, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 1, 0, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){0, -1, 0, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 0, 1, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 0, -1, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }

      if (foundMaximum != originalValue) {
        WRITE_flag_dst_IMAGE(flag_dst,(int4)(0,0,0,0),1);
      }
      WRITE_dst_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(foundMaximum));
  }
}














__kernel void minimalistic_nonzero_minimum_box_image3d
(
  IMAGE_dst_TYPE dst, IMAGE_flag_dst_TYPE flag_dst, IMAGE_src_TYPE src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float foundMinimum = READ_src_IMAGE(src, sampler, pos).x;
  if (foundMinimum != 0) {
      float originalValue = foundMinimum;
      for (int ax = -1; ax <= 1; ax++) {
        for (int ay = -1; ay <= 1; ay++) {
          for (int az = -1; az <= 1; az++) {
            float value = READ_src_IMAGE(src, sampler, (pos + (int4){ax, ay, az, 0})).x;
            if ( value < foundMinimum && value > 0) {
              foundMinimum = value;
            }
          }
        }
      }

      if (foundMinimum != originalValue) {
        WRITE_flag_dst_IMAGE(flag_dst,(int4)(0,0,0,0),1);
      }
      WRITE_dst_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(foundMinimum));
  }
}

__kernel void minimalistic_nonzero_maximum_box_image3d
(
  IMAGE_dst_TYPE dst, IMAGE_flag_dst_TYPE flag_dst, IMAGE_src_TYPE src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float foundMaximum = READ_src_IMAGE(src, sampler, pos).x;
  if (foundMaximum != 0) {
      float originalValue = foundMaximum;
      float value = READ_src_IMAGE(src, sampler, (pos + (int4){1, 0, 0, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){-1, 0, 0, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 1, 0, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){0, -1, 0, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 0, 1, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }
      value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 0, -1, 0})).x;
      if ( value > foundMaximum && value > 0) {
        foundMaximum = value;
      }

      if (foundMaximum != originalValue) {
        WRITE_flag_dst_IMAGE(flag_dst,(int4)(0,0,0,0),1);
      }
      WRITE_dst_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(foundMaximum));
  }
}

