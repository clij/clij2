__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void count_nonzero_pixels_slice_by_slice_sphere_3d
(
  IMAGE_dst_TYPE dst,
  IMAGE_src_TYPE src,
  const int Nx,
  const int Ny
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);
  const int k = get_global_id(2);

  const POS_src_TYPE coord = POS_dst_INSTANCE(i,j,k,0);
  const POS_dst_TYPE dcoord = POS_dst_INSTANCE(i,j,k,0);

    const int4   e = (int4)  {(Nx-1)/2, (Ny-1)/2, 0, 0 };

    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;
    if (aSquared == 0) {
        aSquared = FLT_MIN;
    }
    if (bSquared == 0) {
        bSquared = FLT_MIN;
    }
    int count = 0;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            if (xSquared / aSquared + ySquared / bSquared <= 1.0) {
                float value = (float)READ_src_IMAGE(src,sampler,coord+(POS_src_INSTANCE(x,y,k,0))).x;
                if (value != 0) {
                    count++;
                }
            }
        }
    }

  WRITE_dst_IMAGE(dst, coord, CONVERT_dst_PIXEL_TYPE(count));
}
