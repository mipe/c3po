package com.petpet.c3po.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.petpet.c3po.adaptor.fits.FITSHelper;
import com.petpet.c3po.api.dao.PersistenceLayer;
import com.petpet.c3po.datamodel.Property;
import com.petpet.c3po.db.PreparedQueries;

public class Configurator {

  private static final Logger LOG = LoggerFactory.getLogger(Configurator.class);

  private PersistenceLayer persistence;

  public Configurator(PersistenceLayer p) {
    this.persistence = p;
  }

  public void configure() {
    LOG.debug("Configuring application.");
    this.initializeHelpers();
    this.loadKnownProperties();
    // eventually load mapping of properties, e.g. lastModified maps to
    // lastChanged
    // TODO load properties files and setup preferences
  }

  private void initializeHelpers() {
    LOG.debug("Initializing helpers.");
    DBHelper.init(this.persistence);
    XMLUtils.init();
    Helper.init();
    FITSHelper.init();
  }

  private void loadKnownProperties() {
    LOG.debug("Loading known properties");
    PreparedQueries pq = new PreparedQueries(this.persistence.getEntityManager());
    List<Property> props = pq.getAllProperties();
    LOG.debug("Loaded {} properties ", props.size());

    for (Property p : props) {
      Helper.KNOWN_PROPERTIES.put(p.getName(), p);
    }
  }
}
