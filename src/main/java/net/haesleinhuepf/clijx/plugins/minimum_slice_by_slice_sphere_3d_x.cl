__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void minimum_slice_by_slice_sphere_3d
(
  IMAGE_dst_TYPE dst,
  IMAGE_src_TYPE src,
  const int Nx,
  const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4){i,j,k,0};

    const int4   e = (int4)  {(Nx-1)/2, (Ny-1)/2, 0, 0 };

    IMAGE_dst_PIXEL_TYPE minimumValue = CONVERT_dst_PIXEL_TYPE(READ_src_IMAGE(src,sampler,coord).x);
    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            if (xSquared / aSquared + ySquared / bSquared <= 1.0) {
                IMAGE_dst_PIXEL_TYPE value = CONVERT_dst_PIXEL_TYPE(READ_src_IMAGE(src,sampler,coord+((int4){x,y,0,0})).x);
                if (value < minimumValue) {
                    minimumValue = value;
                }
            }
        }
    }

  IMAGE_dst_PIXEL_TYPE res = minimumValue;
  WRITE_dst_IMAGE(dst, coord, res);
}
