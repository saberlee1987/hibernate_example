package com.saber.chapter03;

import com.saber.chapter3.entities.PersonEntity;
import com.saber.chapter3.entities.RankingEntity;
import com.saber.chapter3.entities.SkillEntity;
import org.testng.annotations.Test;

public class ModelTest {

    @Test
    public void testModelCreation(){
        PersonEntity person =new PersonEntity();
        person.setName("saber66");

        SkillEntity skill = new SkillEntity();
        skill.setName("JavaEE");

        RankingEntity ranking = new RankingEntity();
        ranking.setSubject(person);
        ranking.setObserver(person);
        ranking.setSkill(skill);
        ranking.setRanking(12);

        System.out.println(ranking);
    }
}
