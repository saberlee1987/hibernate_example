package com.saber.chapter3.services.impl;

import com.saber.chapter3.entities.PersonEntity;
import com.saber.chapter3.entities.RankingEntity;
import com.saber.chapter3.entities.SkillEntity;
import com.saber.chapter3.services.RankingService;
import com.saber.chapter3.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RankingServiceImpl implements RankingService {

    @Override
    public int getRankingFor(String subject, String skill) {
        try (Session session = SessionUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            int average = getRanking(session, subject, skill);
            System.out.println("average ===> " + average);
            transaction.commit();
            return average;
        }
    }

    private int getRanking(Session session, String subject, String skill) {
        Query<RankingEntity> query = session.createQuery("from RankingEntity r where r.subject.name=:subject and r.skill.name=:skill "
                , RankingEntity.class);
        List<RankingEntity> list = query.setParameter("subject", subject)
                .setParameter("skill", skill)
                .list();
        System.out.println(list);
        IntSummaryStatistics statistics = list.stream().collect(Collectors.summarizingInt(RankingEntity::getRanking));

        return (int) statistics.getAverage();
    }

    @Override
    public void addRanking(String subjectName, String observerName, String skillName, int ranking) {
        try (Session session = SessionUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            RankingEntity rankingEntity = addRanking(session, subjectName, observerName, skillName, ranking);
            transaction.commit();
            System.out.println("ranking inserted ===> " + rankingEntity);
        }
    }

    private RankingEntity addRanking(Session session, String subjectName, String observerName, String skillName, int ranking) {
        PersonEntity subject = savePerson(session, subjectName);
        PersonEntity observer = savePerson(session, observerName);
        SkillEntity skillEntity = saveSkill(session, skillName);

        RankingEntity rankingEntity = new RankingEntity();
        rankingEntity.setSubject(subject);
        rankingEntity.setObserver(observer);
        rankingEntity.setSkill(skillEntity);
        rankingEntity.setRanking(ranking);

        session.persist(rankingEntity);

        return rankingEntity;
    }

    @Override
    public void updateRanking(String subject, String observer, String skill, int ranking) {
        try (Session session = SessionUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            RankingEntity rankingEntity = findRanking(session, subject, observer, skill);
            if (rankingEntity == null) {
                addRanking(session, subject, observer, skill, ranking);
            } else {
                rankingEntity.setRanking(ranking);
            }
            transaction.commit();
        }
    }

    @Override
    public void removeRanking(String subject, String observer, String skill) {
        try (Session session = SessionUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            removeRanking(session, subject, observer, skill);
            transaction.commit();
        }
    }

    private void removeRanking(Session session, String subject, String observer, String skill) {
        RankingEntity rankingEntity = findRanking(session, subject, observer, skill);
        if (rankingEntity != null) {
            session.remove(rankingEntity);
        }
    }

    @Override
    public Map<String, Integer> findRankingsFor(String subject) {
        try (Session session = SessionUtil.openSession()) {
            return findRankingsFor(session, subject);
        }
    }

    public Map<String, Integer> findRankingsFor(Session session, String subject) {
        Map<String, Integer> result = new HashMap<>();

        List<RankingEntity> list = session
                .createQuery("from RankingEntity r where r.subject.name=:subject order by r.skill.name"
                        , RankingEntity.class)
                .setParameter("subject", subject)
                .list();
        String lastSkillName = "";
        int sum = 0;
        int count = 0;
        for (RankingEntity entity : list) {
            if (!lastSkillName.equals(entity.getSkill().getName())) {
                sum = 0;
                count = 0;
                lastSkillName = entity.getSkill().getName();
            }
            sum += entity.getRanking();
            count++;
            result.put(lastSkillName, sum / count);
        }
        return result;
    }

    @Override
    public PersonEntity findBestPersonFor(String skill) {
        PersonEntity person;

        try (Session session = SessionUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            person = findBesetPersonFor(session, skill);
            transaction.commit();
        }
        return person;
    }

    private PersonEntity findBesetPersonFor(Session session, String skill) {
        List<Object[]> list = session.createQuery("select r.subject.name , avg(r.ranking) from  RankingEntity r where r.skill.name=:skill group by r.subject.name order by avg(r.ranking) desc ", Object[].class)
                .setParameter("skill", skill)
                .setMaxResults(1).list();
        if (list.size() > 0) {
            Object[] row = list.get(0);
            String personName = row[0].toString();
            return findPerson(session, personName);
        }
        return null;
    }

    private RankingEntity findRanking(Session session, String subject, String observer, String skill) {
        return session.createQuery("from RankingEntity r where r.subject.name=:subject and r.observer.name=:observer and r.skill.name=:skill"
                , RankingEntity.class)
                .setParameter("subject", subject)
                .setParameter("observer", observer)
                .setParameter("skill", skill)
                .uniqueResult();
    }


    private PersonEntity findPerson(Session session, String name) {
        return session.createQuery("from PersonEntity p  where p.name=:name", PersonEntity.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    private SkillEntity findSkill(Session session, String name) {
        return session.createQuery("from SkillEntity s  where s.name=:name", SkillEntity.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    private SkillEntity saveSkill(Session session, String skillName) {
        SkillEntity skillEntity = findSkill(session, skillName);
        if (skillEntity == null) {
            skillEntity = new SkillEntity();
            skillEntity.setName(skillName);
            session.persist(skillEntity);
        }
        return skillEntity;
    }

    private PersonEntity savePerson(Session session, String name) {
        PersonEntity person = findPerson(session, name);
        if (person == null) {
            person = new PersonEntity();
            person.setName(name);
            session.persist(person);
        }
        return person;
    }
}