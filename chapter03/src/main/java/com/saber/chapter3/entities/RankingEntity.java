package com.saber.chapter3.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "rankings")
public class RankingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private PersonEntity subject;
    @ManyToOne
    private PersonEntity observer;
    @ManyToOne
    private SkillEntity skill;

    private Integer ranking;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PersonEntity getSubject() {
        return subject;
    }

    public void setSubject(PersonEntity subject) {
        this.subject = subject;
    }

    public PersonEntity getObserver() {
        return observer;
    }

    public void setObserver(PersonEntity observer) {
        this.observer = observer;
    }

    public SkillEntity getSkill() {
        return skill;
    }

    public void setSkill(SkillEntity skill) {
        this.skill = skill;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankingEntity that = (RankingEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(subject, that.subject) && Objects.equals(observer, that.observer) && Objects.equals(skill, that.skill) && Objects.equals(ranking, that.ranking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, observer, skill, ranking);
    }

    @Override
    public String toString() {
        return "RankingEntity{" +
                "id=" + id +
                ", subject=" + subject +
                ", observer=" + observer +
                ", skill=" + skill +
                ", ranking=" + ranking +
                '}';
    }
}
