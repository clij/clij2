__kernel void watershed_detect_maxima_region_box_3d(
        DTYPE_IMAGE_IN_3D src,
        DTYPE_IMAGE_OUT_3D dst
)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    int4 pos = {get_global_id(0), get_global_id(1), get_global_id(2), 0};
    float centerValue = READ_IMAGE_3D(src, sampler, pos).x;
    float result = 0;

    if (centerValue > 0) {
        result = 1;

        int radius = 1;
        for(int x = -radius; x < radius + 1; x++)
        {
            for(int y = -radius; y < radius + 1; y++)
            {
               for(int z = -radius; z < radius + 1; z++)
               {
                  if (x != y || y != 0) {
                    const int4 localPos = pos + (int4){ x, y, z, 0};

                    float value = READ_IMAGE_3D(src, sampler, localPos).x;
                    if (value - centerValue > 0) {
                        result = 0;
                        break;
                    }
                 }
               }
            }
            if (result < 1) {
                break;
            }
        }
    }
    WRITE_IMAGE_3D(dst, pos, CONVERT_DTYPE_OUT(result));
}