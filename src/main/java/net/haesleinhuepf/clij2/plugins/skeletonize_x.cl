// Author: Robert Haase
//         March 2020
//
// Translated from:
// https://github.com/fiji/Skeletonize3D/blob/master/src/main/java/sc/fiji/skeletonize3D/Skeletonize3D_.java
/**
 * Skeletonize3D plugin for ImageJ(C).
 * Copyright (C) 2008 Ignacio Arganda-Carreras
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation (http://www.gnu.org/licenses/gpl.txt )
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

// EULER LUT
// source: https://github.com/fiji/Skeletonize3D/blob/master/src/main/java/sc/fiji/skeletonize3D/Skeletonize3D_.java#L494
__constant int LUT[256] = {
0,  1,  0, -1,  0, -1,  0,  1,
0, -3,  0, -1,  0, -1,  0,  1,
0, -1,  0,  1,  0,  1,  0, -1,
0,  3,  0,  1,  0,  1,  0, -1,
		
0, -3,  0, -1,  0,  3,  0,  1,
0,  1,  0, -1,  0,  3,  0,  1,
0, -1,  0,  1,  0,  1,  0, -1,
0,  3,  0,  1,  0,  1,  0, -1,

0, -3,  0,  3,  0, -1,  0,  1,
0,  1,  0,  3,  0, -1,  0,  1,
0, -1,  0,  1,  0,  1,  0, -1,
0,  3,  0,  1,  0,  1,  0, -1,

0,  1,  0,  3,  0,  3,  0,  1,
0,  5,  0,  3,  0,  3,  0,  1,
0, -1,  0,  1,  0,  1,  0, -1,
0,  3,  0,  1,  0,  1,  0, -1,

0, -7,  0, -1,  0, -1,  0,  1,
0, -3,  0, -1,  0, -1,  0,  1,
0, -1,  0,  1,  0,  1,  0, -1,
0,  3,  0,  1,  0,  1,  0, -1,

0, -3,  0, -1,  0,  3,  0,  1,
0,  1,  0, -1,  0,  3,  0,  1,
0, -1,  0,  1,  0,  1,  0, -1,
0,  3,  0,  1,  0,  1,  0, -1,

0, -3,  0,  3,  0, -1,  0,  1,
0,  1,  0,  3,  0, -1,  0,  1,
0, -1,  0,  1,  0,  1,  0, -1,
0,  3,  0,  1,  0,  1,  0, -1,

0,  1,  0,  3,  0,  3,  0,  1,
0,  5,  0,  3,  0,  3,  0,  1,
0, -1,  0,  1,  0,  1,  0, -1,
0,  3,  0,  1,  0,  1,  0, -1
};


__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;



inline char indexOctantNEB(float neighbors[]) {
    char n;
    n = 1;
    if( neighbors[2]==1 )
      n |= 128;
    if( neighbors[1]==1 )
      n |=  64;
    if( neighbors[11]==1 )
      n |=  32;
    if( neighbors[10]==1 )
      n |=  16;
    if( neighbors[5]==1 )
      n |=   8;
    if( neighbors[4]==1 )
      n |=   4;
    if( neighbors[14]==1 )
      n |=   2;
    return n;
  }

inline char indexOctantNWB(float neighbors[]) {
    char n;
    n = 1;
    if( neighbors[0]==1 )
      n |= 128;
    if( neighbors[9]==1 )
      n |=  64;
    if( neighbors[3]==1 )
      n |=  32;
    if( neighbors[12]==1 )
      n |=  16;
    if( neighbors[1]==1 )
      n |=   8;
    if( neighbors[10]==1 )
      n |=   4;
    if( neighbors[4]==1 )
      n |=   2;
    return n;
  }

inline char indextOctantSEB(float neighbors[]) {
    char n;
    n = 1;
    if( neighbors[8]==1 )
      n |= 128;
    if( neighbors[7]==1 )
      n |=  64;
    if( neighbors[17]==1 )
      n |=  32;
    if( neighbors[16]==1 )
      n |=  16;
    if( neighbors[5]==1 )
      n |=   8;
    if( neighbors[4]==1 )
      n |=   4;
    if( neighbors[14]==1 )
      n |=   2;
    return n;
  }

inline char indexOctantSWB(float neighbors[]) {
    char n;
    n = 1;
    if( neighbors[6]==1 )
      n |= 128;
    if( neighbors[15]==1 )
      n |=  64;
    if( neighbors[7]==1 )
      n |=  32;
    if( neighbors[16]==1 )
      n |=  16;
    if( neighbors[3]==1 )
      n |=   8;
    if( neighbors[12]==1 )
      n |=   4;
    if( neighbors[4]==1 )
      n |=   2;
    return n;
  }

inline char indexOctantNEU(float neighbors[]) {
    char n;
    n = 1;
    if( neighbors[20]==1 )
      n |= 128;
    if( neighbors[23]==1 )
      n |=  64;
    if( neighbors[19]==1 )
      n |=  32;
    if( neighbors[22]==1 )
      n |=  16;
    if( neighbors[11]==1 )
      n |=   8;
    if( neighbors[14]==1 )
      n |=   4;
    if( neighbors[10]==1 )
      n |=   2;
    return n;
  }

inline char indexOctantNWU(float neighbors[]) {
    char n;
    n = 1;
    if( neighbors[18]==1 )
      n |= 128;
    if( neighbors[21]==1 )
      n |=  64;
    if( neighbors[9]==1 )
      n |=  32;
    if( neighbors[12]==1 )
      n |=  16;
    if( neighbors[19]==1 )
      n |=   8;
    if( neighbors[22]==1 )
      n |=   4;
    if( neighbors[10]==1 )
      n |=   2;
    return n;
  }

inline char indexOctantSEU(float neighbors[]) {
    char n;
    n = 1;
    if( neighbors[26]==1 )
      n |= 128;
    if( neighbors[23]==1 )
      n |=  64;
    if( neighbors[17]==1 )
      n |=  32;
    if( neighbors[14]==1 )
      n |=  16;
    if( neighbors[25]==1 )
      n |=   8;
    if( neighbors[22]==1 )
      n |=   4;
    if( neighbors[16]==1 )
      n |=   2;
    return n;
  }

inline char indexOctantSWU(float neighbors[]) {
    char n;
    n = 1;
    if( neighbors[24]==1 )
      n |= 128;
    if( neighbors[25]==1 )
      n |=  64;
    if( neighbors[15]==1 )
      n |=  32;
    if( neighbors[16]==1 )
      n |=  16;
    if( neighbors[21]==1 )
      n |=   8;
    if( neighbors[22]==1 )
      n |=   4;
    if( neighbors[12]==1 )
      n |=   2;
    return n;
  }


  /**
   * Check if a point is Euler invariant
   * 
   * @param neighbors neighbor pixels of the point
   * @return true or false if the point is Euler invariant or not
   */
inline bool isEulerInvariant(float neighbors[])
  {
    // Calculate Euler characteristic for each octant and sum up
    int eulerChar = 0;
    char n;
    // Octant SWU
    n = indexOctantSWU(neighbors);
    eulerChar += LUT[n];
    
    // Octant SEU
    n = indexOctantSEU(neighbors);
    eulerChar += LUT[n];
    
    // Octant NWU
    n = indexOctantNWU(neighbors);
    eulerChar += LUT[n];
    
    // Octant NEU
    n = indexOctantNEU(neighbors);
    eulerChar += LUT[n];
    
    // Octant SWB
    n = indexOctantSWB(neighbors);
    eulerChar += LUT[n];
    
    // Octant SEB
    n = indextOctantSEB(neighbors);
    eulerChar += LUT[n];
    
    // Octant NWB
    n = indexOctantNWB(neighbors);
    eulerChar += LUT[n];
    
    // Octant NEB
    n = indexOctantNEB(neighbors);
    eulerChar += LUT[n];

    return eulerChar == 0;
}


__kernel void skeletonize(
    IMAGE_src_TYPE src,
    IMAGE_flag_dst_TYPE flag_dst,
    IMAGE_dst_TYPE dst,
    const int direction,
    const int dimension
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = (dimension == 3)?get_global_id(2):0;

  const POS_src_TYPE pos = POS_src_INSTANCE(x, y, z, 0);

  float pixel = READ_IMAGE(src, sampler, pos).x;
  if (pixel == 0) {
    WRITE_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(pixel));
    return; // pixel is background already.
  }


  const POS_src_TYPE pos_dist;

  int isBorderPoint = 0;

  // North
  if( direction == 1 && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(-1, 0, 0, 0)).x <= 0 ) {
    isBorderPoint = 1;
  }
  // South
  else if( direction == 2 && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(1, 0, 0, 0)).x <= 0 ) {
    isBorderPoint = 1;
  }
  // East
  else if( direction == 3 && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(0, -1, 0, 0)).x <= 0 ) {
    isBorderPoint = 1;
  }
  // West
  else if( direction == 4 && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(0, 1, 0, 0)).x <= 0 ) {
    isBorderPoint = 1;
  }
  if (dimension == 3) {
      // Lower
      if( direction == 5 && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(0, 0, -1, 0)).x <= 0 ) {
        isBorderPoint = 1;
      }
      // Upper
      else if( direction == 6 && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(0, 0, 1, 0)).x <= 0 ) {
        isBorderPoint = 1;
      }
  }

  if(isBorderPoint == 0) {
    WRITE_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(pixel));
    return;         // current point is not deletable
  }

  float neighborhood[27];
  if (dimension == 2) {
    for (int i = 0; i < 27; i++) {
      neighborhood[i] = 0;
    }
  }

  if (dimension == 3) {
    neighborhood[0] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( -1, -1, -1, 0 )).x;
    neighborhood[1] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 0,  -1, -1, 0 )).x;
    neighborhood[2] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 1,  -1, -1, 0 )).x;

    neighborhood[3] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( -1, 0, -1, 0 )).x;
    neighborhood[4] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 0,  0, -1, 0 )).x;
    neighborhood[5] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 1,  0, -1, 0 )).x;

    neighborhood[6] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( -1, 1, -1, 0 )).x;
    neighborhood[7] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 0,  1, -1, 0 )).x;
    neighborhood[8] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 1,  1, -1, 0 )).x;
  }

  neighborhood[ 9] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( -1, -1, 0, 0 )).x;
  neighborhood[10] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 0,  -1, 0, 0 )).x;
  neighborhood[11] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 1,  -1, 0, 0 )).x;

  neighborhood[12] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( -1, 0, 0, 0 )).x;
  neighborhood[13] = pixel;
  neighborhood[14] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 1,  0, 0, 0 )).x;

  neighborhood[15] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( -1, 1, 0, 0 )).x;
  neighborhood[16] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 0,  1, 0, 0 )).x;
  neighborhood[17] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 1,  1, 0, 0 )).x;

  if (dimension == 3) {
    neighborhood[18] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( -1, -1, 1, 0 )).x;
    neighborhood[19] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 0,  -1, 1, 0 )).x;
    neighborhood[20] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 1,  -1, 1, 0 )).x;

    neighborhood[21] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( -1, 0, 1, 0 )).x;
    neighborhood[22] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 0,  0, 1, 0 )).x;
    neighborhood[23] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 1,  0, 1, 0 )).x;

    neighborhood[24] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( -1, 1, 1, 0 )).x;
    neighborhood[25] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 0,  1, 1, 0 )).x;
    neighborhood[26] = READ_IMAGE(src, sampler, pos + POS_src_INSTANCE( 1,  1, 1, 0 )).x;
  }


  int neighbor_count = -1;  // -1 and not 0 because the center pixel will be counted as well
  for (int i = 0; i < 27; i++) {
    if (neighborhood[i] != 0) {
      neighbor_count++;
    }
  }

  if (neighbor_count == 1) { // current pixel is an end point and cannot be deleted
    WRITE_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(pixel));
    return;
  }


  // Check if point is Euler invariant (condition 1 in Lee[94])
  if( !isEulerInvariant( neighborhood ) )
  {
    WRITE_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(pixel));
    return;         // current point is not deletable
  }

  WRITE_IMAGE (flag_dst, POS_flag_dst_INSTANCE(0, 0, 0, 0), 1);
  WRITE_IMAGE (dst, pos, 0);

}
