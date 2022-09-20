package com.saber.hibernate_example;

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
import java.util.List;

public class HibernatePersistenceTest {
    private SessionFactory sessionFactory;

    @BeforeClass
    public void setup() {
        //SessionFactory factory = new Configuration().configure().buildSessionFactory();

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    private MessageEntity saveMessage(String text) {
        MessageEntity entity = new MessageEntity(text);
        try (Session session = sessionFactory.openSession();) {
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        }
        return entity;
    }

    @Test
    public void readMessage() {
        MessageEntity messageEntity = saveMessage("Hello World !!!");
        List<MessageEntity> entities;
        try (Session session = sessionFactory.openSession()) {
            entities = session.createQuery("from MessageEntity", MessageEntity.class).list();
        }
        Assert.assertEquals(entities.size(), 1);
        entities.forEach(System.out::println);
        Assert.assertEquals(entities.get(0), messageEntity);
    }
}
