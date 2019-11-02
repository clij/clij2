
__kernel void cross_correlation_3d(DTYPE_IMAGE_IN_3D src1,
                                    DTYPE_IMAGE_IN_3D mean_src1,
                                    DTYPE_IMAGE_IN_3D src2,
                                    DTYPE_IMAGE_IN_3D mean_src2,
                                    DTYPE_IMAGE_OUT_3D dst,
                                    int radius,
                                    int i,
                                    int dimension)
{

    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    int4 pos = {get_global_id(0), get_global_id(1), get_global_id(2), 0};
    int4 deltaPos = {get_global_id(0), get_global_id(1), get_global_id(2), 0};


    float sum1 = 0;
    float sum2 = 0;
    float sum3 = 0;
    for(int k = -radius; k < radius + 1; k++)
    {
        deltaPos[dimension] = get_global_id(dimension) + k;
        float Ia = READ_IMAGE_3D(src1, sampler, deltaPos).x;
        float meanIa = READ_IMAGE_3D(mean_src1, sampler, deltaPos).x;
        deltaPos[dimension] = get_global_id(dimension) + k + i;
        float Ib = READ_IMAGE_3D(src2, sampler, deltaPos).x;
        float meanIb = READ_IMAGE_3D(mean_src2, sampler, deltaPos).x;

        sum1 = sum1 + (Ia - meanIa) * (Ib - meanIb);
        sum2 = sum2 + pow((float)(Ia - meanIa), (float)2.0);
        sum3 = sum3 + pow((float)(Ib - meanIb), (float)2.0);
    }

    float result = sum1 / pow((float)(sum2 * sum3), (float)0.5);

    WRITE_IMAGE_3D(dst, pos, CONVERT_DTYPE_OUT(result));
}
