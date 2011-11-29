package com.petpet.c3po.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.petpet.c3po.datamodel.Element;
import com.petpet.c3po.datamodel.Feature;
import com.petpet.c3po.datamodel.Value;

/**
 * Creates a distribution table of all elements and all features as described in
 * the paper 'Finding representative set from massive data'. A feature is always
 * a property with a specific value.
 * 
 * @author peter
 * 
 */
public class DistributionTable {

  private List<Element> elements;

  private List<Feature> features;

  private double[][] table;

  public DistributionTable(Set<Element> elmnts, Set<Feature> features) {
    this.setElements(new ArrayList<Element>(elmnts));
    this.setFeatures(new ArrayList<Feature>(features));
    this.createTable();
  }

  public double[][] getDistributionTable() {
    return this.table;
  }

  public double getDistribution(Element e, Feature f) {
    int i = this.getElements().indexOf(e);
    int j = this.getFeatures().indexOf(f);

    if (i == -1 || j == -1) {
      return Double.NaN;
    }

    return table[i][j];
  }

  public List<Element> getElements() {
    return elements;
  }

  public void setElements(List<Element> elements) {
    this.elements = elements;
  }

  public List<Feature> getFeatures() {
    return features;
  }

  public void setFeatures(List<Feature> features) {
    this.features = features;
  }

  public void print() {
    for (int i = 0; i < table.length; i++) {
      for (int j = 0; j < table[i].length; j++) {
        System.out.print(table[i][j] + " ");
      }
      System.out.println();
    }
  }

  private void createTable() {
    this.table = new double[getElements().size()][getFeatures().size()];
    for (int i = 0; i < table.length; i++) {
      Element tmpE = getElements().get(i);
      for (int j = 0; j < table[i].length; j++) {
        table[i][j] = 0d;
        Feature tmpF = getFeatures().get(j);

        if (containsFeature(tmpE, tmpF)) {
          table[i][j] = 1d;
        }
      }
    }

    for (int i = 0; i < table.length; i++) {
      double sum = 0;
      for (int j = 0; j < table[i].length; j++) {
        sum += table[i][j];
      }

      // because of conflicts it could happen that thes sum is
      // 0 and that leads to division by 0.
      if (sum != 0d) {
        for (int j = 0; j < table[i].length; j++) {
          table[i][j] = table[i][j] / sum;
        }
      }
    }

  }

  private boolean containsFeature(Element element, Feature f) {
    for (Value v : element.getValues()) {
      if (v.getProperty().getName().equals(f.getProperty()) && v.getValue().equals(f.getValue())) {
        return true;
      }
    }

    return false;
  }

}
