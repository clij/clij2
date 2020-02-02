__kernel void watershed_binarize_labelmap_3d
(
  IMAGE_src_labelmap_TYPE src_labelmap,
  IMAGE_dst_binary_TYPE dst_binary
)
{
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){ x, y, z, 0 };

  float currentlabel = READ_src_labelmap_IMAGE(src_labelmap, sampler, pos).x;

  if (currentlabel > 0) {
      for (int ax = -1; ax <= 1; ax++) {
        for (int ay = -1; ay <= 1; ay++) {
          for (int az = -1; az <= 1; az++) {
            if (ax != 0 || ay != 0 || az != 0) {
              float otherlabel = READ_src_labelmap_IMAGE(src_labelmap, sampler, (pos + (int4){ax, ay, az, 0})).x;
              if (otherlabel < currentlabel) {
                currentlabel = 0;
                break;
              }
            }
          }
        }
        if (currentlabel < 1) {
          break;
        }
      }
  }
  if (currentlabel > 0) {
    currentlabel = 1;
  }

  WRITE_dst_binary_IMAGE (dst_binary, pos, CONVERT_dst_binary_PIXEL_TYPE(currentlabel));
}
