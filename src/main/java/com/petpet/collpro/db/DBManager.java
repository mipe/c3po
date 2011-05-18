package com.petpet.collpro.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DBManager {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DBManager.class);
    
    private static DBManager uniqueInstance;
    
    private EntityManagerFactory emf;
    
    private EntityManager em;
    
    public static synchronized DBManager getInstance() {
        if (DBManager.uniqueInstance == null) {
            DBManager.uniqueInstance = new DBManager();
            DBManager.uniqueInstance.createEntityManagerFactory();
            DBManager.LOGGER.info("DBManager created");
        }
        
        return DBManager.uniqueInstance;
    }
    
    public EntityManager getEntityManager() {
        if (this.em == null) {
            em = this.emf.createEntityManager();
        }
        
        return this.em;
    }
    
    public void close() {
        if (this.em != null) {
            this.em.close();
        }
        
        if (this.emf != null) {
            this.emf.close();
        }
    }
    
    private void createEntityManagerFactory() {
        this.emf = Persistence.createEntityManagerFactory("CollProPersistenceUnit");
    }
    
    private DBManager() {
        
    }
}