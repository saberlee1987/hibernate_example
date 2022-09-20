package com.saber.hibernate_example.chapter2;

import com.saber.hibernate_example.chapter2.entities.MessageEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HibernatePersistenceTest {

    private SessionFactory factory;

    @BeforeClass
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
        factory = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
    }

    private MessageEntity save(String text) {
        MessageEntity entity = new MessageEntity(text);

        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            System.out.println("message entity persist successfully");
        }
        return entity;
    }

    private List<MessageEntity> save(List<MessageEntity> entities) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            for (MessageEntity entity : entities) {
                session.persist(entity);
            }
            transaction.commit();
            System.out.println("message entities persist successfully");
        }
        return entities;
    }
    private List<MessageEntity> findAll() {
        List<MessageEntity> entities = null;
        try (Session session = factory.openSession()) {
            entities = session.createQuery("from MessageEntity ", MessageEntity.class).list();
        }
        return entities;

    }

    @Test
    public void readMessage() {
        MessageEntity entity1 = new MessageEntity("Hello World@@@@@");
        MessageEntity entity2 = new MessageEntity("Hello saber azizi");
        MessageEntity entity3 = new MessageEntity("Hello bruce lee");
        MessageEntity entity4 = new MessageEntity("Hello jackie chan");

        List<MessageEntity> entities = Arrays.asList(entity1, entity2, entity3, entity4);
        save(entities);
        List<MessageEntity> list = findAll();

        Assert.assertEquals(list.size(), entities.size());
        list.forEach(System.out::println);
        Assert.assertEquals(list, entities);

    }
}
