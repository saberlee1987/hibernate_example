package com.saber.chapter03;

import com.saber.chapter3.entities.PersonEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PersonTest {
    private SessionFactory factory;

    @BeforeClass
    public void setup(){

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                 .configure().build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }


    @Test
    public void testSavePerson(){
        try (Session session = factory.openSession()){
            Transaction transaction = session.beginTransaction();
            PersonEntity person = new PersonEntity();
            person.setName("saber66");
            session.persist(person);
            transaction.commit();
            System.out.println(person);
        }
    }

    @Test
    public void testFindPerson(){
        testSavePerson();
        try (Session session = factory.openSession()){
            Query<PersonEntity> query = session.createQuery("from PersonEntity where name=:name", PersonEntity.class);
            query.setParameter("name","saber66");
            PersonEntity person = query.uniqueResult();
            Assert.assertNotNull(person);
            Assert.assertEquals(person.getName(),"saber66");
            System.out.println("person ===> "+person);
        }
    }
}
