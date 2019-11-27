package com.tagbio.umap;

import java.util.Arrays;

import junit.framework.TestCase;

public class MathUtilsTest extends TestCase {

  public void testLog2() {
    assertEquals(0, MathUtils.log2(1), 1e-10);
    assertEquals(1, MathUtils.log2(2), 1e-10);
    assertEquals(4, MathUtils.log2(16), 1e-10);
    assertEquals(1.6514961294723187, MathUtils.log2(Math.PI), 1e-10);
  }

  public void testMax() {
    assertEquals(42, MathUtils.max(42), 1e-10);
    assertEquals(42, MathUtils.max(42, 1), 1e-10);
    assertEquals(42, MathUtils.max(1, 42), 1e-10);
    assertEquals(42, MathUtils.max(0, 42, -300.5F), 1e-10);
    assertEquals(Float.NEGATIVE_INFINITY, MathUtils.max());
  }

  public void testMin() {
    assertEquals(42, MathUtils.min(42), 1e-10);
    assertEquals(1, MathUtils.min(42, 1), 1e-10);
    assertEquals(1, MathUtils.min(1, 42), 1e-10);
    assertEquals(-300.5F, MathUtils.min(0, 42, -300.5F), 1e-10);
    assertEquals(Float.POSITIVE_INFINITY, MathUtils.min());
  }

  public void testMean() {
    assertEquals(42, MathUtils.mean(42), 1e-10);
    assertEquals(21, MathUtils.mean(42, 0), 1e-10);
    assertEquals(21, MathUtils.mean(0, 42), 1e-10);
    assertEquals(2.5, MathUtils.mean(0, 1, 2, 3, 4, 5), 1e-10);
  }

  public void testMean2D() {
    assertEquals(7.833333333333333, MathUtils.mean(new float[][] {{0, 42}, {2, 3}, {-7, 7}}), 1e-10);
  }

  public void testFilterPositive() {
    assertTrue(Arrays.equals(new float[0], MathUtils.filterPositive()));
    assertTrue(Arrays.equals(new float[0], MathUtils.filterPositive(-0.1F, -1F, Float.NEGATIVE_INFINITY)));
    assertTrue(Arrays.equals(new float[] {1, 42}, MathUtils.filterPositive(-7, 1, 0, -42, 42)));
  }

  public void testContainsNegative() {
    assertFalse(MathUtils.containsNegative(new int[0][0]));
    assertFalse(MathUtils.containsNegative(new int[][] {{1}}));
    assertTrue(MathUtils.containsNegative(new int[][] {{-1}}));
    assertFalse(MathUtils.containsNegative(new int[][] {{1, 0, 42}, {1, 2, 3}}));
    assertTrue(MathUtils.containsNegative(new int[][] {{1, 0, 42}, {1, 2, -3}}));
    assertTrue(MathUtils.containsNegative(new int[][] {{-1, 0, 42}, {1, 2, 3}}));
  }

  public void testScalarMultiply() {
    assertTrue(Arrays.equals(new float[0], MathUtils.multiply(new float[0], 2)));
    assertTrue(Arrays.equals(new float[] {42}, MathUtils.multiply(new float[] {21}, 2)));
    assertTrue(Arrays.equals(new float[] {21, 0, 1}, MathUtils.multiply(new float[] {42, 0, 2}, 0.5F)));
  }

  public void testScalarDivide() {
    assertTrue(Arrays.equals(new float[0], MathUtils.divide(new float[0], 0.5F)));
    assertTrue(Arrays.equals(new float[] {42}, MathUtils.divide(new float[] {21}, 0.5F)));
    assertTrue(Arrays.equals(new float[] {21, 0, 1}, MathUtils.divide(new float[] {42, 0, 2}, 2)));
  }

  public void testLinspace() {
    final float[] res = MathUtils.linspace(2, 5, 15);
    assertNotNull(res);
    assertEquals(15, res.length);
    assertEquals(2.0F, res[0]);
    assertEquals(5.0F, res[res.length - 1]);
    for (int i = 0; i < res.length; i++) {
      assertEquals(2.0F + 3.0F * i / 14.0F, res[i]);
    }
  }

  public void testIdentity() {
    assertTrue(Arrays.equals(new int[] {0, 1, 2, 3, 4}, MathUtils.identity(5)));
  }

  public void testArgSort() {
    float[] data = new float[] {9,2,3,5,1,12,34,26,0,-43};
    int[] res = MathUtils.argsort(data);
    assertTrue(Arrays.equals(new int[]{9,8,4,1,2,3,0,5,7,6}, res));
  }

  public void testPromoteTranspose() {
    float[] data = {9, 2, 3, 5, 1, 12, 34, 26, 0, -43};
    final Matrix matrix = MathUtils.promoteTranspose(data);
    assertTrue(Arrays.equals(new int[]{10, 1}, matrix.shape));
    assertTrue(Arrays.equals(data, matrix.transpose().row(0)));
  }

  public void testSubArray1D() {
    float[] floatSubArray = MathUtils.subarray(new float[]{9, 2, 3, 5, 1, 12, 34, 26, 0, -43}, 2, 7);
    assertTrue(Arrays.equals(new float[]{3, 5, 1, 12, 34}, floatSubArray));
    int[] intSubArray = MathUtils.subarray(new int[]{9, 2, 3, 5, 1, 12, 34, 26, 0, -43}, 1, 4);
    assertTrue(Arrays.equals(new int[]{2, 3, 5}, intSubArray));
    try {
      MathUtils.subarray(new float[]{9, 2, 3, 5, 1, 12, 34, 26, 0, -43}, 7, 2);
      fail("bad array limits");
    } catch (NegativeArraySizeException nase) {
      ;
    }
     try {
      MathUtils.subarray(new int[]{9, 2, 3, 5, 1, 12, 34, 26, 0, -43}, 7, 2);
      fail("bad array limits");
    } catch (NegativeArraySizeException nase) {
      ;
    }
   try {
      MathUtils.subarray(new float[]{9, 2, 3, 5, 1, 12, 34, 26, 0, -43}, 2, 11);
      fail("bad array limits");
    } catch (IndexOutOfBoundsException ioobe) {
      ;
    }
    try {
      MathUtils.subarray(new int[]{9, 2, 3, 5, 1, 12, 34, 26, 0, -43}, 2, 11);
      fail("bad array limits");
    } catch (IndexOutOfBoundsException ioobe) {
      ;
    }
  }

  public void testSubArray2D() {
    float[][] floatSubArray = MathUtils.subArray(new float[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 2);
    assertEquals(3, floatSubArray.length);
    for (int i = 0; i < 3; ++i) {
      assertEquals("row" + i, 2, floatSubArray[i].length);
    }
    assertTrue(Arrays.equals(new float[]{1,2}, floatSubArray[0]));
    assertTrue(Arrays.equals(new float[]{4,5}, floatSubArray[1]));
    assertTrue(Arrays.equals(new float[]{7,8}, floatSubArray[2]));

    int[][] intSubArray = MathUtils.subArray(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 2);
    assertEquals(3, intSubArray.length);
    for (int i = 0; i < 3; ++i) {
      assertEquals("row" + i, 2, intSubArray[i].length);
    }
    assertTrue(Arrays.equals(new int[]{1,2}, intSubArray[0]));
    assertTrue(Arrays.equals(new int[]{4,5}, intSubArray[1]));
    assertTrue(Arrays.equals(new int[]{7,8}, intSubArray[2]));

    floatSubArray = MathUtils.subArray(new float[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 0);
    assertEquals(3, floatSubArray.length);
    for (int i = 0; i < 3; ++i) {
      assertEquals("row" + i, 0, floatSubArray[i].length);
    }

    floatSubArray = MathUtils.subArray(new float[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 4);
    assertEquals(3, floatSubArray.length);
    for (int i = 0; i < 3; ++i) {
      assertEquals("row" + i, 3, floatSubArray[i].length);
    }
    try {
      MathUtils.subArray(new float[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, -22);
      fail("bad array limits");
    } catch (NegativeArraySizeException nase) {
      ;
    }

    intSubArray = MathUtils.subArray(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 0);
    assertEquals(3, intSubArray.length);
    for (int i = 0; i < 3; ++i) {
      assertEquals("row" + i, 0, intSubArray[i].length);
    }

    intSubArray = MathUtils.subArray(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 4);
    assertEquals(3, intSubArray.length);
    for (int i = 0; i < 3; ++i) {
      assertEquals("row" + i, 3, intSubArray[i].length);
    }
    try {
      MathUtils.subArray(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, -2);
      fail("bad array limits");
    } catch (NegativeArraySizeException nase) {
      ;
    }

 }

  public void testZeroEntriesBelowLimit() {
    float[] data = {9, 2, 3, 5, 1, 12, 34, 26, 0, -43};
    MathUtils.zeroEntriesBelowLimit(data, 9);
    assertTrue(Arrays.equals(new float[] {9, 0, 0, 0, 0, 12, 34, 26, 0, 0}, data));
  }

  public void testNegate() {
    float[] negate = MathUtils.negate(new float[] {9, 2, 3, 5, 1, 12, 34, 26, 0, -43});
    System.out.println(Arrays.toString(negate));
    assertTrue(Arrays.equals(new float[] {-9, -2, -3, -5, -1, -12, -34, -26, 0, 43}, negate));
    assertEquals(0.0F, negate[8]);
  }

  public void testConcatenate() {
    int[] res = MathUtils.concatenate(new int []{9, 2, 3, 5, 1, 12}, new int[] { 34, 26, 0, -43});
    assertTrue(Arrays.equals(new int[]{9, 2, 3, 5, 1, 12, 34, 26, 0, -43}, res));
  }
}

