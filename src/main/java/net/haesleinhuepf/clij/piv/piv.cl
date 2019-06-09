
__kernel void cross_correlation_3d( DTYPE_IMAGE_IN_3D src1,
                                    DTYPE_IMAGE_IN_3D mean_src1,
                                    DTYPE_IMAGE_IN_3D src2,
                                    DTYPE_IMAGE_IN_3D mean_src2,
                                    DTYPE_IMAGE_OUT_3D dst,
                                    int radiusx,
                                    int radiusy,
                                    int radiusz,
                                    int ix,
                                    int iy,
                                    int iz)
{

    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    int4 pos = {get_global_id(0), get_global_id(1), get_global_id(2), 0};
    int4 deltaPos = {get_global_id(0), get_global_id(1), get_global_id(2), 0};

    int4 deltaPosI = {get_global_id(0), get_global_id(1), get_global_id(2), 0};


    float sum1 = 0;
    float sum2 = 0;
    float sum3 = 0;
    for(int kx = -radiusx; kx < radiusx + 1; kx++)
    {
        deltaPos[0] = get_global_id(0) + kx;
        deltaPosI[0] = get_global_id(0) + kx + ix;
        for(int ky = -radiusy; ky < radiusy + 1; ky++)
        {
            deltaPos[1] = get_global_id(1) + ky;
            deltaPosI[1] = get_global_id(1) + ky + iy;
            for(int kz = -radiusz; kz < radiusz + 1; kz++)
            {
                deltaPos[2] = get_global_id(2) + kz;
                deltaPosI[2] = get_global_id(2) + kz + iz;

                float Ia = READ_IMAGE_3D(src1, sampler, deltaPos).x;
                float meanIa = READ_IMAGE_3D(mean_src1, sampler, deltaPos).x;

                float Ib = READ_IMAGE_3D(src2, sampler, deltaPosI).x;
                float meanIb = READ_IMAGE_3D(mean_src2, sampler, deltaPosI).x;

                sum1 = sum1 + (Ia - meanIa) * (Ib - meanIb);
                sum2 = sum2 + pow((float)(Ia - meanIa), (float)2.0);
                sum3 = sum3 + pow((float)(Ib - meanIb), (float)2.0);
            }
        }
    }

    float result = sum1 / pow((float)(sum2 * sum3), (float)0.5);

    WRITE_IMAGE_3D(dst, pos, (DTYPE_OUT) result);
}

__kernel void index_projection_3d( DTYPE_IMAGE_IN_3D index_src1,
                                    DTYPE_IMAGE_IN_3D index_map_src1,
                                    DTYPE_IMAGE_OUT_3D dst,
                                    int indexDimension,
                                    int fixed1,
                                    int fixedDimension1,
                                    int fixed2,
                                    int fixedDimension2
                                  )
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    int4 pos = {get_global_id(0), get_global_id(1), get_global_id(2), 0};
    float index = READ_IMAGE_3D(index_src1, sampler, pos).x;

    int4 indexPos = {0, 0, 0, 0};
    indexPos[indexDimension] = index;
    indexPos[fixedDimension1] = fixed1;
    indexPos[fixedDimension2] = fixed2;

    float value = READ_IMAGE_3D(index_map_src1, sampler, indexPos).x;

    WRITE_IMAGE_3D(dst, pos, (DTYPE_OUT) value);
}


