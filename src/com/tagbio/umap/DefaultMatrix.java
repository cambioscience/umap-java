package com.tagbio.umap;

import java.util.Arrays;

/**
 * Default matrix implementation backed by a square matrix.
 * @author Sean A. Irvine
 * @author Richard Littin
 */
class DefaultMatrix extends Matrix {

  private final float[][] mData;

  /**
   * Construct a matrix backed by the given array.  Note the array is NOT copied, so that
   * any external changes to the underlying array will affect that matrix as well.
   * @param matrix matrix values
   */
  DefaultMatrix(final float[][] matrix) {
    super(matrix.length, matrix[0].length);
    mData = matrix;
  }

  /**
   * Construct a new zero matrix of specified dimensions.
   * @param rows number of rows
   * @param cols number of columns
   */
  DefaultMatrix(final int rows, final int cols) {
    this(new float[rows][cols]);
  }

  /**
   * Construct a new zero matrix of specified dimensions.
   * @param shape shape specification (only first two values of shape are used)
   */
  DefaultMatrix(final int[] shape) {
    this(shape[0], shape[1]);
  }

  @Override
  float get(final int row, final int col) {
    return mData[row][col];
  }

  @Override
  void set(final int row, final int col, final float val) {
    mData[row][col] = val;
  }

  @Override
  Matrix copy() {
    final float[][] copy = new float[mData.length][];
    for (int k = 0; k < copy.length; ++k) {
      copy[k] = Arrays.copyOf(mData[k], mData[k].length);
    }
    return new DefaultMatrix(copy);
  }

  @Override
  float[] row(int row) {
    return mData[row];
  }

  @Override
  Matrix eliminate_zeros() {
    // There is nothing to be done in this implementation (zeros cannot be removed)
    return this;
  }
}
