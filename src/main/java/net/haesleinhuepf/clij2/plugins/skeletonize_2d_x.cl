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



  char indexOctantNEB(float neighbors[]) {
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

  char indexOctantNWB(float neighbors[]) {
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

  char indextOctantSEB(float neighbors[]) {
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

  char indexOctantSWB(float neighbors[]) {
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

  char indexOctantNEU(float neighbors[]) {
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

  char indexOctantNWU(float neighbors[]) {
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

  char indexOctantSEU(float neighbors[]) {
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

  char indexOctantSWU(float neighbors[]) {
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
  bool isEulerInvariant(float neighbors[])
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


/* -----------------------------------------------------------------------*/
	/**
	 * This is a recursive method that calculates the number of connected
	 * components in the 3D neighborhood after the center pixel would
	 * have been removed.
	 *
	 * @param octant
	 * @param label
	 * @param cube
	 */
	void octreeLabeling(int octant, int label, float cube[])
	{
		// check if there are points in the octant with value 1
		  if( octant==1 )
		  {
		  	// set points in this octant to current label
		  	// and recursive labeling of adjacent octants
		    if( cube[0] == 1 )
		      cube[0] = label;
		    if( cube[1] == 1 )
		    {
		      cube[1] = label;
		      octreeLabeling( 2, label, cube);
		    }
		    if( cube[3] == 1 )
		    {
		      cube[3] = label;
		      octreeLabeling( 3, label, cube);
		    }
		    if( cube[4] == 1 )
		    {
		      cube[4] = label;
		      octreeLabeling( 2, label, cube);
		      octreeLabeling( 3, label, cube);
		      octreeLabeling( 4, label, cube);
		    }
		    if( cube[9] == 1 )
		    {
		      cube[9] = label;
		      octreeLabeling( 5, label, cube);
		    }
		    if( cube[10] == 1 )
		    {
		      cube[10] = label;
		      octreeLabeling( 2, label, cube);
		      octreeLabeling( 5, label, cube);
		      octreeLabeling( 6, label, cube);
		    }
		    if( cube[12] == 1 )
		    {
		      cube[12] = label;
		      octreeLabeling( 3, label, cube);
		      octreeLabeling( 5, label, cube);
		      octreeLabeling( 7, label, cube);
		    }
		  }
		  if( octant==2 )
		  {
		    if( cube[1] == 1 )
		    {
		      cube[1] = label;
		      octreeLabeling( 1, label, cube);
		    }
		    if( cube[4] == 1 )
		    {
		      cube[4] = label;
		      octreeLabeling( 1, label, cube);
		      octreeLabeling( 3, label, cube);
		      octreeLabeling( 4, label, cube);
		    }
		    if( cube[10] == 1 )
		    {
		      cube[10] = label;
		      octreeLabeling( 1, label, cube);
		      octreeLabeling( 5, label, cube);
		      octreeLabeling( 6, label, cube);
		    }
		    if( cube[2] == 1 )
		      cube[2] = label;
		    if( cube[5] == 1 )
		    {
		      cube[5] = label;
		      octreeLabeling( 4, label, cube);
		    }
		    if( cube[11] == 1 )
		    {
		      cube[11] = label;
		      octreeLabeling( 6, label, cube);
		    }
		    if( cube[13] == 1 )
		    {
		      cube[13] = label;
		      octreeLabeling( 4, label, cube);
		      octreeLabeling( 6, label, cube);
		      octreeLabeling( 8, label, cube);
		    }
		  }
		  if( octant==3 )
		  {
		    if( cube[3] == 1 )
		    {
		      cube[3] = label;
		      octreeLabeling( 1, label, cube);
		    }
		    if( cube[4] == 1 )
		    {
		      cube[4] = label;
		      octreeLabeling( 1, label, cube);
		      octreeLabeling( 2, label, cube);
		      octreeLabeling( 4, label, cube);
		    }
		    if( cube[12] == 1 )
		    {
		      cube[12] = label;
		      octreeLabeling( 1, label, cube);
		      octreeLabeling( 5, label, cube);
		      octreeLabeling( 7, label, cube);
		    }
		    if( cube[6] == 1 )
		      cube[6] = label;
		    if( cube[7] == 1 )
		    {
		      cube[7] = label;
		      octreeLabeling( 4, label, cube);
		    }
		    if( cube[14] == 1 )
		    {
		      cube[14] = label;
		      octreeLabeling( 7, label, cube);
		    }
		    if( cube[15] == 1 )
		    {
		      cube[15] = label;
		      octreeLabeling( 4, label, cube);
		      octreeLabeling( 7, label, cube);
		      octreeLabeling( 8, label, cube);
		    }
		  }
		  if( octant==4 )
		  {
		  	if( cube[4] == 1 )
		    {
		      cube[4] = label;
		      octreeLabeling( 1, label, cube);
		      octreeLabeling( 2, label, cube);
		      octreeLabeling( 3, label, cube);
		    }
		  	if( cube[5] == 1 )
		    {
		      cube[5] = label;
		      octreeLabeling( 2, label, cube);
		    }
		    if( cube[13] == 1 )
		    {
		      cube[13] = label;
		      octreeLabeling( 2, label, cube);
		      octreeLabeling( 6, label, cube);
		      octreeLabeling( 8, label, cube);
		    }
		    if( cube[7] == 1 )
		    {
		      cube[7] = label;
		      octreeLabeling( 3, label, cube);
		    }
		    if( cube[15] == 1 )
		    {
		      cube[15] = label;
		      octreeLabeling( 3, label, cube);
		      octreeLabeling( 7, label, cube);
		      octreeLabeling( 8, label, cube);
		    }
		    if( cube[8] == 1 )
		      cube[8] = label;
		    if( cube[16] == 1 )
		    {
		      cube[16] = label;
		      octreeLabeling( 8, label, cube);
		    }
		  }
		  if( octant==5 )
		  {
		  	if( cube[9] == 1 )
		    {
		      cube[9] = label;
		      octreeLabeling( 1, label, cube);
		    }
		    if( cube[10] == 1 )
		    {
		      cube[10] = label;
		      octreeLabeling( 1, label, cube);
		      octreeLabeling( 2, label, cube);
		      octreeLabeling( 6, label, cube);
		    }
		    if( cube[12] == 1 )
		    {
		      cube[12] = label;
		      octreeLabeling( 1, label, cube);
		      octreeLabeling( 3, label, cube);
		      octreeLabeling( 7, label, cube);
		    }
		    if( cube[17] == 1 )
		      cube[17] = label;
		    if( cube[18] == 1 )
		    {
		      cube[18] = label;
		      octreeLabeling( 6, label, cube);
		    }
		    if( cube[20] == 1 )
		    {
		      cube[20] = label;
		      octreeLabeling( 7, label, cube);
		    }
		    if( cube[21] == 1 )
		    {
		      cube[21] = label;
		      octreeLabeling( 6, label, cube);
		      octreeLabeling( 7, label, cube);
		      octreeLabeling( 8, label, cube);
		    }
		  }
		  if( octant==6 )
		  {
		  	if( cube[10] == 1 )
		    {
		      cube[10] = label;
		      octreeLabeling( 1, label, cube);
		      octreeLabeling( 2, label, cube);
		      octreeLabeling( 5, label, cube);
		    }
		    if( cube[11] == 1 )
		    {
		      cube[11] = label;
		      octreeLabeling( 2, label, cube);
		    }
		    if( cube[13] == 1 )
		    {
		      cube[13] = label;
		      octreeLabeling( 2, label, cube);
		      octreeLabeling( 4, label, cube);
		      octreeLabeling( 8, label, cube);
		    }
		    if( cube[18] == 1 )
		    {
		      cube[18] = label;
		      octreeLabeling( 5, label, cube);
		    }
		    if( cube[21] == 1 )
		    {
		      cube[21] = label;
		      octreeLabeling( 5, label, cube);
		      octreeLabeling( 7, label, cube);
		      octreeLabeling( 8, label, cube);
		    }
		    if( cube[19] == 1 )
		      cube[19] = label;
		    if( cube[22] == 1 )
		    {
		      cube[22] = label;
		      octreeLabeling( 8, label, cube);
		    }
		  }
		  if( octant==7 )
		  {
		  	if( cube[12] == 1 )
		    {
		      cube[12] = label;
		      octreeLabeling( 1, label, cube);
		      octreeLabeling( 3, label, cube);
		      octreeLabeling( 5, label, cube);
		    }
		  	if( cube[14] == 1 )
		    {
		      cube[14] = label;
		      octreeLabeling( 3, label, cube);
		    }
		    if( cube[15] == 1 )
		    {
		      cube[15] = label;
		      octreeLabeling( 3, label, cube);
		      octreeLabeling( 4, label, cube);
		      octreeLabeling( 8, label, cube);
		    }
		    if( cube[20] == 1 )
		    {
		      cube[20] = label;
		      octreeLabeling( 5, label, cube);
		    }
		    if( cube[21] == 1 )
		    {
		      cube[21] = label;
		      octreeLabeling( 5, label, cube);
		      octreeLabeling( 6, label, cube);
		      octreeLabeling( 8, label, cube);
		    }
		    if( cube[23] == 1 )
		      cube[23] = label;
		    if( cube[24] == 1 )
		    {
		      cube[24] = label;
		      octreeLabeling( 8, label, cube);
		    }
		  }
		  if( octant==8 )
		  {
		  	if( cube[13] == 1 )
		    {
		      cube[13] = label;
		      octreeLabeling( 2, label, cube);
		      octreeLabeling( 4, label, cube);
		      octreeLabeling( 6, label, cube);
		    }
		  	if( cube[15] == 1 )
		    {
		      cube[15] = label;
		      octreeLabeling( 3, label, cube);
		      octreeLabeling( 4, label, cube);
		      octreeLabeling( 7, label, cube);
		    }
		  	if( cube[16] == 1 )
		    {
		      cube[16] = label;
		      octreeLabeling( 4, label, cube);
		    }
		  	if( cube[21] == 1 )
		    {
		      cube[21] = label;
		      octreeLabeling( 5, label, cube);
		      octreeLabeling( 6, label, cube);
		      octreeLabeling( 7, label, cube);
		    }
		  	if( cube[22] == 1 )
		    {
		      cube[22] = label;
		      octreeLabeling( 6, label, cube);
		    }
		  	if( cube[24] == 1 )
		    {
		      cube[24] = label;
		      octreeLabeling( 7, label, cube);
		    }
		  	if( cube[25] == 1 )
		      cube[25] = label;
		  }

	}

/* -----------------------------------------------------------------------*/
	/**
	 * Check if current point is a Simple Point.
	 * This method is named 'N(v)_labeling' in [Lee94].
	 * Outputs the number of connected objects in a neighborhood of a point
	 * after this point would have been removed.
	 *
	 * @param neighbors neighbor pixels of the point
	 * @return true or false if the point is simple or not
	 */
	bool isSimplePoint(float neighbors[])
	{
		// copy neighbors for labeling
		float cube[26];
		int i;
		for( i = 0; i < 13; i++ )  // i =  0..12 -> cube[0..12]
			cube[i] = neighbors[i];
		// i != 13 : ignore center pixel when counting (see [Lee94])
		for( i = 14; i < 27; i++ ) // i = 14..26 -> cube[13..25]
			cube[i-1] = neighbors[i];
		// set initial label
		int label = 2;
		// for all points in the neighborhood
		for( i = 0; i < 26; i++ )
		{
			if( cube[i]==1 )     // voxel has not been labeled yet
			{
				// start recursion with any octant that contains the point i
				switch( i )
				{
				case 0:
				case 1:
				case 3:
				case 4:
				case 9:
				case 10:
				case 12:
					octreeLabeling(1, label, cube );
					break;
				case 2:
				case 5:
				case 11:
				case 13:
					octreeLabeling(2, label, cube );
					break;
				case 6:
				case 7:
				case 14:
				case 15:
					octreeLabeling(3, label, cube );
					break;
				case 8:
				case 16:
					octreeLabeling(4, label, cube );
					break;
				case 17:
				case 18:
				case 20:
				case 21:
					octreeLabeling(5, label, cube );
					break;
				case 19:
				case 22:
					octreeLabeling(6, label, cube );
					break;
				case 23:
				case 24:
					octreeLabeling(7, label, cube );
					break;
				case 25:
					octreeLabeling(8, label, cube );
					break;
				}
				label++;
				if( label-2 >= 2 )
				{
					return false;
				}
			}
		}
		//return label-2; in [Lee94] if the number of connected components would be needed
		return true;
	}

__kernel void skeletonize_2d(
    IMAGE_src_TYPE src,
    IMAGE_flag_dst_TYPE flag_dst,
    IMAGE_dst_TYPE dst,
    const int direction
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const POS_src_TYPE pos = POS_src_INSTANCE(x, y, 0, 0);

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
  else if( direction == 2 && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(-1, 0, 0, 0)).x <= 0 ) {
    isBorderPoint = 1;
  }
  // East
  else if( direction == 3 && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(-1, 0, 0, 0)).x <= 0 ) {
    isBorderPoint = 1;
  }
  // West
  else if( direction == 4 && READ_IMAGE(src, sampler, pos + POS_src_INSTANCE(-1, 0, 0, 0)).x <= 0 ) {
    isBorderPoint = 1;
  }

  if(isBorderPoint == 0) {
    WRITE_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(pixel));
    return;         // current point is not deletable
  }

  float neighborhood[27];
  for (int i = 0; i < 27; i++) {
    neighborhood[i] = 0;
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

  // Check if point is simple (deletion does not change connectivity in the 3x3x3 neighborhood)
  // (conditions 2 and 3 in Lee[94])
  if( !isSimplePoint( neighborhood ) )
  {
    WRITE_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(pixel));
    return;         // current point is not deletable
  }

  WRITE_IMAGE (flag_dst, POS_flag_dst_INSTANCE(0, 0, 0, 0), 1);
  WRITE_IMAGE (dst, pos, 0);
}
