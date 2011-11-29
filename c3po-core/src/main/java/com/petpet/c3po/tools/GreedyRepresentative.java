package com.petpet.c3po.tools;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.petpet.c3po.datamodel.Element;
import com.petpet.c3po.datamodel.Feature;

public class GreedyRepresentative {

  private static final Logger LOG = LoggerFactory.getLogger(GreedyRepresentative.class);

  private static int T_MAX = 2; // if 1 or smaller, than L(r) = {r};

  private DistributionTable table;

  public double calculateEntropy(Element r, Element e) {
    Set<Element> e1 = new HashSet<Element>();
    Set<Element> e2 = new HashSet<Element>();
    e1.add(r);
    e2.add(e);

    return this.calculateEntropy(e1, e2);
  }

  /*
   * calculates the Kullback-Leibler divergence between the distributions of the
   * two sets or the entropy of these. Low entropy means higher similarity, high
   * entropy - lower similarity.
   */
  public double calculateEntropy(Set<Element> e1, Set<Element> e2) {
    LOG.debug("calculating Kullback-Leibler divergence: s1 = {} s2 = {}", e1.size(), e2.size());
    double[][] t = getTable().getDistributionTable();

    double kl = 0d;

    for (Feature f : getTable().getFeatures()) {
      double e1f_sum = 0d;
      double e2f_sum = 0d;

      int j = getTable().getFeatures().indexOf(f);

      for (Element e : e1) {
        int i = getTable().getElements().indexOf(e);
        e1f_sum += t[i][j];

      }

      for (Element e : e2) {
        int i = getTable().getElements().indexOf(e);
        e2f_sum += t[i][j];
      }

      e1f_sum /= e1.size();
      e2f_sum /= e2.size();

      // LOG.debug("pfe1 = {}, pfe2 = {}", e1f_sum, e2f_sum);

      e1f_sum = this.correctZeroValues(e1f_sum);
      e2f_sum = this.correctZeroValues(e2f_sum);

      // LOG.debug("corrected");
      // LOG.debug("pfe1 = {}, pfe2 = {}", e1f_sum, e2f_sum);

      double quot = e1f_sum / e2f_sum;
      // LOG.debug("pfe1/pfe2 = {}", quot);

      kl += e1f_sum * Math.log10(quot);

      LOG.debug("kullback-leibler divergence = {}", kl);

    }

    return kl;
  }

  // algorithm
  public Set<Element> getRepresentativeSet(Set<Element> e_tetha, int size) {

    Set<Element> representatives = new HashSet<Element>();

    while (representatives.size() < size) {
      LOG.debug("Size of representaive set is: {}", representatives.size());
      double f_max = Double.MIN_VALUE;
      Element e_max = null;

      Iterator<Element> iterator = e_tetha.iterator();
      while (iterator.hasNext()) {
        LOG.debug("e_tetha size is {}", e_tetha.size());
        Element e = iterator.next();
        double f = this.calculateObjectiveFunction(e, representatives, e_tetha);

        if (f > f_max) {
          f_max = f;
          e_max = e;
        }
      }

      representatives.add(e_max);
      LOG.debug("Added a new representative");
    }

    return representatives;

  }

  // f - objective function
  private double calculateObjectiveFunction(Element r_new, Set<Element> representaives, Set<Element> e_tetha) {

    double min_klr = Double.MAX_VALUE;

    for (Element r : representaives) {
      double tmp_entropy = this.calculateEntropy(r_new, r);

      if (min_klr > tmp_entropy) {
        min_klr = tmp_entropy;
      }
    }

    Set<Element> old_e_tetha = ((Set<Element>) ((HashSet<Element>) e_tetha).clone());
    Set<Element> r_new_related = this.getRelatedTo(r_new, e_tetha);

    double im1a = (e_tetha.size() / this.table.getElements().size()) * calculateEntropy(e_tetha, old_e_tetha);
    double im2a = (r_new_related.size() / this.table.getElements().size())
        * calculateEntropy(r_new_related, old_e_tetha);

    double delta_i = im1a + im2a;

    return delta_i + min_klr;
  }

  // get related elements to r
  // TODO optimize double calculation of entropy.. could be done once.
  private Set<Element> getRelatedTo(Element r, Set<Element> e_tetha) {
    Set<Element> related = new HashSet<Element>();

    double mindkl = Double.MAX_VALUE;
    for (Element e : e_tetha) {
      if (!e.equals(r)) {
        double klre = this.calculateEntropy(r, e);
        if (klre < mindkl) {
          mindkl = klre;
        }
      }
    }

    for (Element e : e_tetha) {
      if (!e.equals(r)) {
        double klre = this.calculateEntropy(r, e);
        LOG.debug("klre = {} mindkl * TMAX = {}", klre, mindkl * T_MAX);
        if (klre < mindkl * T_MAX) {
          LOG.debug("Elements {} and {} are related!", e.getName(), e.getName());
          related.add(e);
        } else {
          LOG.debug("Elements {} and {} are not related!", e.getName(), e.getName());
        }
      }
    }

    e_tetha.remove(related); // not sure wether we need that here.
    return related;

  }

  private double correctZeroValues(double value) {
    double almost_zero = Math.pow(10d, -10d);
    if (value == 0d) {
      value = almost_zero;
    }

    return value;
  }

  public DistributionTable getTable() {
    return table;
  }

  public void setTable(DistributionTable table) {
    this.table = table;
  }
}
