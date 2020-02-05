
__kernel void detect_maxima_3d_slice_by_slice(
        IMAGE_src_TYPE src,
        IMAGE_dst_TYPE dst,
        __private int radius
)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    int4 pos = {get_global_id(0), get_global_id(1), get_global_id(2), 0};
    float localMax = READ_src_IMAGE(src, sampler, pos).x - 1;
    int4 localMaxPos = pos;

    for(int x = -radius; x < radius + 1; x++)
    {
        for(int y = -radius; y < radius + 1; y++)
        {
            int z = 0;
            const int4 localPos = pos + (int4){ x, y, z, 0};

            float value = READ_src_IMAGE(src, sampler, localPos).x;

            if (value > localMax) {
                localMax = value;
                localMaxPos = localPos;
            }
        }
    }

    if (pos.x == localMaxPos.x && pos.y == localMaxPos.y) {
        WRITE_dst_IMAGE(dst, pos, 1);
    } else {
        WRITE_dst_IMAGE(dst, pos, 0);
    }
}
