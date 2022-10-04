package com.saber.chapter03;

import com.saber.chapter3.entities.PersonEntity;
import com.saber.chapter3.entities.RankingEntity;
import com.saber.chapter3.entities.SkillEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;

public class RankingTest {
    private SessionFactory factory;

    @BeforeClass
    public void setup() {

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @AfterMethod
    public void afterMethod() {
        factory.close();
    }


    private PersonEntity savePerson(Session session, String name) {
        PersonEntity person = new PersonEntity();
        person.setName(name);
        session.persist(person);
        System.out.println(person);
        return person;
    }

    private SkillEntity saveSkill(Session session, String name) {
        SkillEntity skill = new SkillEntity();
        skill.setName(name);
        session.persist(skill);
        System.out.println(skill);
        return skill;
    }

    @Test
    public void testPersistRanking() {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();

            PersonEntity subject = savePerson(session, "saber66");
            PersonEntity observer = savePerson(session, "bruce40");
            SkillEntity skill = saveSkill(session, "java");

            RankingEntity ranking = new RankingEntity();
            ranking.setSkill(skill);
            ranking.setObserver(observer);
            ranking.setSubject(subject);
            ranking.setRanking(6);
            session.persist(ranking);

            transaction.commit();

            System.out.println("ranking ===> " + ranking);
        }
    }

    private void populateRankingData() {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            createDate(session, "saber66", "bruce40", "java", 7);
            createDate(session, "saber66", "jet62", "java", 9);
            createDate(session, "saber66", "ali", "java", 8);
            createDate(session, "saber66", "sara", "java", 9);
            transaction.commit();
        }
    }

    private Integer getAverage(String subjectName, String skillName) {
        try (Session session = factory.openSession()) {
            Query<RankingEntity> query = session.createQuery("from RankingEntity r where r.subject.name=:name and r.skill.name=:skill", RankingEntity.class);
            query.setParameter("name", subjectName);
            query.setParameter("skill", skillName);
            IntSummaryStatistics statistics = query.list().stream().collect(Collectors.summarizingInt(RankingEntity::getRanking));
            return (int) statistics.getAverage();
        }
    }

    private void createDate(Session session, String subjectName, String observerName, String skillName, int rankingNumber) {
        PersonEntity subject = savePerson(session, subjectName);
        PersonEntity observer = savePerson(session, observerName);
        SkillEntity skill = saveSkill(session, skillName);
        RankingEntity ranking = new RankingEntity();
        ranking.setSkill(skill);
        ranking.setObserver(observer);
        ranking.setSubject(subject);
        ranking.setRanking(rankingNumber);
        session.persist(ranking);
    }

    @Test
    public void changeRanking() {
        populateRankingData();
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<RankingEntity> query = session.createQuery("from RankingEntity r where r.subject.name=:subject and r.observer.name=:observer and r.skill.name=:skill", RankingEntity.class);
            query.setParameter("subject", "saber66");
            query.setParameter("observer", "bruce40");
            query.setParameter("skill", "java");
            RankingEntity rankingEntity = query.uniqueResult();

            Assert.assertNotNull(rankingEntity, "Could not find ranking match");
            rankingEntity.setRanking(10);
            transaction.commit();

            Assert.assertEquals(getAverage("saber66", "java"), 9);
        }
    }

    private RankingEntity findRanking(Session session, String subject, String observer, String skill) {
        Query<RankingEntity> query = session.createQuery("from RankingEntity r where r.subject.name=:subject and r.observer.name=:observer and r.skill.name=:skill"
                , RankingEntity.class);
        return query.setParameter("subject", subject)
                .setParameter("observer", observer)
                .setParameter("skill", skill).uniqueResult();
    }

    @Test
    public void removeRanking(){
        populateRankingData();
        try (Session session = factory.openSession()){
            Transaction transaction = session.beginTransaction();
            RankingEntity ranking = findRanking(session,"saber66","bruce40","java");
            Assert.assertNotNull(ranking);
            session.remove(ranking);
            transaction.commit();
        }
        Assert.assertEquals(getAverage("saber66","java"),8);
    }

    @Test
    public void testRankings() {
        populateRankingData();
        try (Session session = factory.openSession()) {
            Query<RankingEntity> query = session.createQuery("from RankingEntity r where r.subject.name=:name and r.skill.name=:skill", RankingEntity.class);
            query.setParameter("name", "saber66");
            query.setParameter("skill", "java");
            IntSummaryStatistics statistics = query.list().stream().collect(Collectors.summarizingInt(RankingEntity::getRanking));
            long count = statistics.getCount();
            int average = (int) statistics.getAverage();
            Assert.assertEquals(count, 4);
            Assert.assertEquals(average, 7);
        }
    }


}
