package org.example.config;

import org.example.entity.Song;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;

    private SessionFactory sessionFactory;

    private FactoryConfiguration(){
        org.hibernate.cfg.Configuration configuration = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Song.class);
        System.out.println("Hibernate Java Configured Successfully...");
        sessionFactory = configuration.buildSessionFactory();
    }
    public static FactoryConfiguration getInstance(){
        return factoryConfiguration == null ? factoryConfiguration = new FactoryConfiguration() : factoryConfiguration;
    }
    public Session getSession(){ return sessionFactory.openSession();}
}

