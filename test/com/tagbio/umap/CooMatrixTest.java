package com.tagbio.umap;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class CooMatrixTest extends AbstractMatrixTest {

  Matrix getMatrixA() {
    return new DefaultMatrix(new float[][] {{0, 1}, {0.5F, 2}, {1, 0}, {0, 3}}).tocoo();
  }

  public void testSorted() {
    CooMatrix matrix = new CooMatrix(new float[] {9.1F,9.2F,8.3F,1.2F,2.3F,3.2F,3.1F,3.3F,5.3F}, new int[] {9,9,8,1,2,3,3,3,5}, new int[] {1,2,3,2,3,2,1,3,3}, new int[]{10,4});
    System.out.println(matrix);
    for (int i = 1; i < matrix.mRow.length; ++i) {
      System.out.println(matrix.mRow[i] + " " + matrix.mCol[i] + " " + matrix.mData[i]);
    }
    for (int i = 1; i < matrix.mRow.length; ++i) {
      int r = Integer.compare(matrix.mRow[i], matrix.mRow[i - 1]);
      if (r == 0) {
        int c = Integer.compare(matrix.mCol[i], matrix.mCol[i - 1]);
        if (c <= 0) {
          fail("row " + i + " columns out of order: col["+(i-1)+"]:"+matrix.mCol[i-1]+" !<= col["+i+"]:"+matrix.mCol[i]);
        }
      } else if (r < 0) {
        fail("rows out of order: row["+(i-1)+"]:"+matrix.mRow[i-1]+" !<= row["+i+"]:"+matrix.mRow[i]);
      }
      assertEquals("data at " + i + " not correct", matrix.mData[i], matrix.mRow[i] + (float)(matrix.mCol[i] / 10.0f));
    }
  }

  public void testBadParams() {
    try {
      new CooMatrix(new float[]{9.1F, 9.2F, 8.3F, 1.2F, 2.3F, 3.2F, 3.1F, 3.2F, 5.3F}, new int[]{9, 9, 8, 1, 12, 3, 3, 3, 5}, new int[]{1, 2, 3, 2, 3, 2, 1, 3, 3}, new int[]{10, 4});
      fail("Accepted row out of range");
    } catch (IllegalArgumentException e) {
      // expected
    }
    try {
      new CooMatrix(new float[]{9.1F, 9.2F, 8.3F, 1.2F, 2.3F, 3.2F, 3.1F, 3.2F, 5.3F}, new int[]{9, 9, 8, 1, 2, 3, -33, 3, 5}, new int[]{1, 2, 3, 2, 3, 2, 1, 3, 3}, new int[]{10, 4});
      fail("Accepted row out of range");
    } catch (IllegalArgumentException e) {
      // expected
    }
    try {
      new CooMatrix(new float[]{9.1F, 9.2F, 8.3F, 1.2F, 2.3F, 3.2F, 3.1F, 3.2F, 5.3F}, new int[]{9, 9, 8, 1, 2, 3, 3, 3, 5}, new int[]{1, 2, 3, 2, 3, -2, 1, 3, 3}, new int[]{10, 4});
      fail("Accepted column out of range");
    } catch (IllegalArgumentException e) {
      // expected
    }
    try {
      new CooMatrix(new float[]{9.1F, 9.2F, 8.3F, 1.2F, 2.3F, 3.2F, 3.1F, 3.2F, 5.3F}, new int[]{9, 9, 8, 1, 2, 3, 3, 3, 5}, new int[]{1, 222, 3, 2, 3, 2, 1, 3, 3}, new int[]{10, 4});
      fail("Accepted column out of range");
    } catch (IllegalArgumentException e) {
      // expected
    }
    try {
      new CooMatrix(new float[]{9.1F, 9.2F, 8.3F, 1.2F, 2.3F, 3.2F, 3.1F, 3.2F, 5.3F}, new int[]{9, 9, 8, 1, 2, 3, 3, 3, 5}, new int[]{1, 2, 3, 2, 3, 2, 1, 3, 3}, new int[]{8, 4});
      fail("Accepted row out of range");
    } catch (IllegalArgumentException e) {
      // expected
    }
    try {
      new CooMatrix(new float[]{9.1F, 9.2F, 8.3F, 1.2F, 2.3F, 3.2F, 3.1F, 3.2F, 5.3F}, new int[]{9, 9, 8, 1, 12, 3, 3, 3, 5}, new int[]{1, 2, 3, 2, 3, 2, 1, 3, 3}, new int[]{-10, 4});
      fail("Accepted bad lengths");
    } catch (IllegalArgumentException e) {
      // expected
    }
    try {
      new CooMatrix(new float[]{9.1F, 9.2F, 8.3F, 1.2F, 2.3F, 3.2F, 3.1F, 3.2F, 5.3F}, new int[]{9, 9, 8, 1, 2, 3, 3, 3, 5}, new int[]{1, 2, 3, 2, 3, 2, 1, 2, 3}, new int[]{10, 4});
      fail("Accepted duplicate position");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
}
