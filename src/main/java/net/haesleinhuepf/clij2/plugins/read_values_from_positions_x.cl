
__kernel void read_values_from_positions(
    IMAGE_pointlist_TYPE pointlist,
    IMAGE_map_image_TYPE map_image,
    IMAGE_intensities_TYPE intensities
)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    const int p = get_global_id(0);

    const int x = READ_IMAGE(pointlist, sampler, POS_pointlist_INSTANCE(p, 0, 0, 0)).x;
    const int y = READ_IMAGE(pointlist, sampler, POS_pointlist_INSTANCE(p, 1, 0, 0)).x;
    const int z = READ_IMAGE(pointlist, sampler, POS_pointlist_INSTANCE(p, 2, 0, 0)).x;

    float intensity = READ_IMAGE(map_image, sampler, POS_map_image_INSTANCE(x, y, z, 0)).x;

    POS_intensities_TYPE dpos = POS_intensities_INSTANCE(p, 0, 0, 0);
    WRITE_intensities_IMAGE(intensities, dpos, CONVERT_intensities_PIXEL_TYPE(intensity));
}
