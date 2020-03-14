__kernel void entropy_3d(
    IMAGE_src_TYPE src,
    IMAGE_dst_TYPE dst,
    int radiusX,
    int radiusY,
    int radiusZ,
    float minIntensity,
    float maxIntensity
)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    POS_src_TYPE pos = POS_src_INSTANCE(get_global_id(0), get_global_id(1), get_global_id(2), 0);
    float centerValue = READ_IMAGE(src, sampler, pos).x;
    float result = 0;

    int numBins = 256;
    float hist[256];
    for (int i = 0; i < numBins; i++) {
        hist[i] = 0;
    }

    float size_bin = (maxIntensity - minIntensity) / numBins;

    // create histogram over all pixels in radius
    for (int x = -radiusX; x <= radiusX; x++)
    {
        for (int y = -radiusY; y <= radiusY; y++)
        {
            for (int z = -radiusZ; z <= radiusZ; z++)
            {
                const POS_src_TYPE localPos = pos + POS_src_INSTANCE(x, y, z, 0);
                float value = READ_IMAGE(src, sampler, localPos).x;
                int bin = (int) (value / size_bin);
                hist[bin] = hist[bin] + 1;
            }
        }
    }


    float total = (radiusX * 2 + 1) * (radiusY * 2 + 1) * (radiusZ * 2 + 1);

    // compute entropy
    float entropy = 0;
    for (int k = 0; k < numBins; k++)
    {
        if (hist[k] > 0)
        {
            float p = hist[k] / total;
            entropy = entropy + (-p * log2(p));
        }
    }

    WRITE_IMAGE(dst, pos, CONVERT_dst_PIXEL_TYPE(entropy));
}