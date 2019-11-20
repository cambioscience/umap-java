package com.tagbio.umap;

import java.util.Arrays;

/**
 * @author Sean A. Irvine
 */
class CooMatrix extends Matrix {
  // todo -- replacement fo scipy coo_matrix

  // todo I think this is internal rep of data for this form of matrix -- currently some direct external access
  int[] row;
  int[] col;
  float[] data;

  CooMatrix(final float[] vals, final int[] rows, final int[] cols, final int[] lengths) {
    super(lengths);
    if (rows.length != cols.length || rows.length != vals.length) {
      throw new IllegalArgumentException();
    }
    row = rows;
    col = cols;
    data = vals;
  }

  void sum_duplicates() {
    // todo add identical entries -- this would be fairly easy if we knew arrays we sorted by (row,col)
    // todo for now ugliness ...

    final DefaultMatrix res = new DefaultMatrix(shape);
    for (int k = 0; k < row.length; ++k) {
      res.set(row[k], col[k], res.get(row[k], col[k]) + data[k]);
    }
    CooMatrix coo = res.tocoo(); // todo yikes!!
    row = coo.row;
    col = coo.col;
    data = coo.data;
  }

  @Override
  float get(final int r, final int c) {
    // todo this could be made faster if can assume sorted row[] col[]
    // todo there may be duplicate coords, need to sum result?
    for (int k = 0; k < row.length; ++k) {
      if (row[k] == r && col[k] == c) {
        return data[k];
      }
    }
    return 0;
  }

  @Override
  void set(final int row, final int col, final float val) {
    throw new UnsupportedOperationException();
  }

  @Override
  Matrix copy() {
    return new CsrMatrix(Arrays.copyOf(data, data.length), Arrays.copyOf(row, row.length), Arrays.copyOf(col, col.length), Arrays.copyOf(shape, shape.length));
  }

  @Override
  Matrix transpose() {
    // todo note this is not copying the arrays -- might be a mutability issue
    return new CooMatrix(data, col, row, new int[] {shape[1], shape[0]});
  }

  @Override
  CooMatrix tocoo() {
    return this;
  }

  void eliminate_zeros() {
    int zeros = 0;
    for (final float v : data) {
      if (v == 0) {
        ++zeros;
      }
    }
    if (zeros > 0) {
      final int[] r = new int[row.length - zeros];
      final int[] c = new int[row.length - zeros];
      final float[] d = new float[row.length - zeros];
      for (int k = 0, j = 0; k < data.length; ++k) {
        if (data[k] != 0) {
          r[j] = row[k];
          c[j] = col[k];
          d[j++] = data[k];
        }
      }
      row = r;
      col = c;
      data = d;
    }
  }

  @Override
  Matrix add(final Matrix m) {
    // todo this could do this without using super
    return super.add(m).tocoo();
  }

  @Override
  Matrix subtract(final Matrix m) {
    // todo this could do this without using super
    return super.subtract(m).tocoo();
  }

  @Override
  Matrix multiply(final Matrix m) {
    // todo this could do this without using super
    return super.multiply(m).tocoo();
  }

  @Override
  Matrix multiply(final float x) {
    final float[] newData = Arrays.copyOf(data, data.length);
    for (int i = 0; i < newData.length; ++i) {
      newData[i] *= x;
    }
    return new CooMatrix(newData, row, col, shape);
  }
}
