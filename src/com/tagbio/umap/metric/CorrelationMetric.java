package com.tagbio.umap.metric;

/**
 * Correlation distance.
 * @author Sean A. Irvine
 */
public class CorrelationMetric extends Metric {

  public static final CorrelationMetric SINGLETON = new CorrelationMetric();

  private CorrelationMetric() {
    super(true);
  }

  @Override
  public double distance(final float[] x, final float[] y) {
     float muX = 0.0F;
     float muY = 0.0F;
     float normX = 0.0F;
     float normY = 0.0F;
     float dotProduct = 0.0F;

    for (int i = 0; i < x.length; ++i) {
       muX += x[i];
       muY += y[i];
     }

     muX /= x.length;
     muY /= x.length;

    for (int i = 0; i < x.length; ++i) {
      final float shiftedX = x[i] - muX;
      final float shiftedY = y[i] - muY;
      normX += shiftedX * shiftedX;
      normY += shiftedY * shiftedY;
      dotProduct += shiftedX * shiftedY;
    }

     if (normX == 0.0 && normY == 0.0) {
       return 0.0;
     } else if (dotProduct == 0.0) {
       return 1.0;
     } else {
       return 1.0 - (dotProduct / Math.sqrt(normX * normY));
     }
  }
}
