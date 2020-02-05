
__kernel void rgbReplaceBlackAndWhite(
       DTYPE_IMAGE_IN_3D src,
       DTYPE_IMAGE_OUT_3D dst
)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    // set positions from where to read
	int4 posR = {get_global_id(0), get_global_id(1), 0, 0};
    int4 posG = {get_global_id(0), get_global_id(1), 1, 0};
    int4 posB = {get_global_id(0), get_global_id(1), 2, 0};
 
    // read original pixel values
    float r = READ_IMAGE_3D(src, sampler, posR).x;
    float g = READ_IMAGE_3D(src, sampler, posG).x;
    float b = READ_IMAGE_3D(src, sampler, posB).x;

    // do the math suggested by Jan Eglinger  https://forum.image.sc/t/invert-rgb-image-without-changing-colors/33571
    float ir = 255 - (g + b) / 2;
    float ig = 255 - (r + b) / 2;
    float ib = 255 - (r + g) / 2;
    
	// write the pixels back to the destination image
    WRITE_IMAGE_3D(dst, posR, CONVERT_DTYPE_OUT(ir));
    WRITE_IMAGE_3D(dst, posG, CONVERT_DTYPE_OUT(ig));
    WRITE_IMAGE_3D(dst, posB, CONVERT_DTYPE_OUT(ib));
}
