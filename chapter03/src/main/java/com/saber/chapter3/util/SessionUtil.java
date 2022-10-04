package com.saber.chapter3.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class SessionUtil {

    private static final SessionFactory FACTORY;

    static {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        FACTORY = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static Session openSession() {
        return FACTORY.openSession();
    }

}
