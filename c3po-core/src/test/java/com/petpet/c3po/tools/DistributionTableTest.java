package com.petpet.c3po.tools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.petpet.c3po.datamodel.Element;
import com.petpet.c3po.datamodel.Feature;
import com.petpet.c3po.datamodel.Property;
import com.petpet.c3po.datamodel.StringValue;
import com.petpet.c3po.datamodel.Value;

public class DistributionTableTest {

	private Element e1, e2, e3;

	private Value v1, v2, v3, v4;

	private Property p1, p2;

	private Feature f1, f2;

	private DistributionTable table;
	
	private GreedyRepresentative gr;

	@Before
	public void before() {
		p1 = new Property("prop1");
		p2 = new Property("prop2");

		e1 = new Element("e1", "path1");
		e2 = new Element("e2", "path2");
		e3 = new Element("e3", "path3");

		v1 = new StringValue("a");
		v1.setElement(e1);
		v1.setProperty(p1);

		v2 = new StringValue("b");
		v2.setElement(e2);
		v2.setProperty(p2);

		v3 = new StringValue("b");
		v3.setElement(e3);
		v3.setProperty(p2);

		v4 = new StringValue("a");
		v4.setElement(e3);
		v4.setProperty(p1);

		e1.getValues().add(v1);
		e2.getValues().add(v2);
		e3.getValues().add(v3);
		e3.getValues().add(v4);

		f1 = new Feature(p1.getName(), v1.getValue());
		f2 = new Feature(p2.getName(), v2.getValue());

		this.table = new DistributionTable(new HashSet<Element>(Arrays.asList(
				e1, e2, e3)), new HashSet<Feature>(Arrays.asList(f1, f2)));
		
		this.gr = new GreedyRepresentative();
		this.gr.setTable(table);
	}

	@Test
	public void testDistributionTableCreation() throws Exception {
		double[][] t = this.table.getDistributionTable();

		Assert.assertEquals(t.length, 3);
		Assert.assertEquals(t[0].length, 2);

	}

	@Test
	public void testGetSpecificDistribution() throws Exception {
		double d1 = this.table.getDistribution(e1, f1);
		double d2 = this.table.getDistribution(e1, f2);
		double d3 = this.table.getDistribution(e2, f1);
		double d4 = this.table.getDistribution(e2, f2);
		double d5 = this.table.getDistribution(e3, f1);
		double d6 = this.table.getDistribution(e3, f2);
		this.table.print();
		Assert.assertEquals(1d, d1, 0d); // expected, actual, delta
		Assert.assertEquals(0d, d2, 0d);
		Assert.assertEquals(0d, d3, 0d);
		Assert.assertEquals(1d, d4, 0d);
		Assert.assertEquals(0.5, d5, 0d);
		Assert.assertEquals(0.5, d6, 0d);

	}
	
	@Test
	public void testEntropyCalculation() throws Exception {
	  Set<Element> e1 = new HashSet<Element>();
	  Set<Element> e2 = new HashSet<Element>();
	  
	  e1.add(this.e1);
//	  e1.add(this.e2);
//	  e1.add(this.e3);
	  
	  e2.add(this.e2);
//	  e2.add(this.e3);
	  double d = this.gr.calculateEntropy(e1, e2);
	  System.out.println("Entropy: " + d);
	}

}
