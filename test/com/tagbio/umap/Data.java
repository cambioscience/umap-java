/*
 * BSD 3-Clause License
 * Copyright (c) 2017, Leland McInnes, 2019 Tag.bio (Java port).
 * See LICENSE.txt.
 */
package com.tagbio.umap;

import com.tagbio.umap.metric.EuclideanMetric;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

abstract class Data {
  private float[][] mData;
  private final List<String> mAttributes = new ArrayList<>();
  private final List<String> mSampleNames = new ArrayList<>();

  protected static InputStream getStream(final String resource) throws IOException {
    final BufferedInputStream resourceAsStream = new BufferedInputStream(Objects.requireNonNull(Data.class.getClassLoader().getResourceAsStream(resource)));
    return resource.endsWith(".gz")
      ? new GZIPInputStream(resourceAsStream)
      : resourceAsStream;
  }

  Data(final String dataFile) throws IOException {
    try (final LineNumberReader r = new LineNumberReader(new InputStreamReader(getStream(dataFile)))) {
      final List<float[]> records = new ArrayList<>();
      String line = r.readLine();
      if (line == null) {
        throw new IOException("No header line");
      }
      // Process header line
      line = line.trim();
      try (final Scanner rowScanner = new Scanner(line)) {
        rowScanner.useDelimiter("\t");
        if (rowScanner.hasNext()) {
          final String next = rowScanner.next();
          assert "sample".equals(next);
        }
        while (rowScanner.hasNext()) {
          mAttributes.add(rowScanner.next());
        }
      }
      // Process data lines
      while ((line = r.readLine()) != null) {
        final String[] parts = line.trim().split("\t");
        if (parts.length != mAttributes.size() + 1) {
          throw new RuntimeException("Incorrect number of fields in: " + line);
        }
        mSampleNames.add(parts[0]);
        final float[] values = new float[mAttributes.size()];
        for (int k = 0; k < values.length; ++k) {
          values[k] = Float.parseFloat(parts[k + 1]);
        }
        records.add(values);
        if (records.size() % 100 == 0) {
          Utils.message("Read " + records.size() + " records");
        }
      }
      mData = records.toArray(new float[0][]);
    }
  }

  public float[][] getData() {
    return mData;
  }

  public String[] getAttributes() {
    return mAttributes.toArray(new String[0]);
  }

  public String[] getSampleNames() {
    return mSampleNames.toArray(new String[0]);
  }

  public void setSampleNames(String [] name) {
    if (name.length != mSampleNames.size()) {
      throw new IllegalArgumentException("Incorrect number of names.");
    }
    mSampleNames.clear();
    for (String a : name) {
      mSampleNames.add(a);
    }
  }

  abstract String getName();

  public int[] getSampleClassIndex() {
    final int[] res = new int[mSampleNames.size()];
    final Map<String, Integer> classIndex = new HashMap<>();
    for (int i = 0; i < mSampleNames.size(); i++) {
      final String name = mSampleNames.get(i).split(":")[0];
      if (!classIndex.containsKey(name)) {
        classIndex.put(name, classIndex.size());
      }
      res[i] = classIndex.get(name);
    }
    return res;
  }

  /**
   * Return this dataset as a square matrix of pairwise distances between instances,
   * with respect to Euclidean distance. Useful for testing.
   * @return distance matrix
   */
  public Matrix getDistances() {
    final Matrix instances = new DefaultMatrix(getData());
    return PairwiseDistances.pairwiseDistances(instances, EuclideanMetric.SINGLETON);
  }
}

