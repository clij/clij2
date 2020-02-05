__kernel void watershed_detect_maxima_region_box_2d(
        DTYPE_IMAGE_IN_2D src,
        DTYPE_IMAGE_OUT_2D dst
)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    int2 pos = {get_global_id(0), get_global_id(1)};
    float centerValue = READ_IMAGE_2D(src, sampler, pos).x;
    float result = 0;

    if (centerValue > 0) {
        result = 1;

        int radius = 1;
        for(int x = -radius; x < radius + 1; x++)
        {
            for(int y = -radius; y < radius + 1; y++)
            {
                if (x != y || y != 0) {
                    const int2 localPos = pos + (int2){ x, y};

                    float value = READ_IMAGE_2D(src, sampler, localPos).x;
                    if (value - centerValue > 0) {
                        result = 0;
                        break;
                    }
                }
            }
            if (result < 1) {
                break;
            }
        }
    }
    WRITE_IMAGE_2D(dst, pos, CONVERT_DTYPE_OUT(result));
}