package com.saber.chapter3.services;

import com.saber.chapter3.entities.PersonEntity;

import java.util.Map;

public interface RankingService {
    int getRankingFor(String subject, String skill);
    void addRanking(String subject, String observer, String skill, int ranking);
    void updateRanking(String subject, String observer, String skill, int ranking);
    void removeRanking(String subject, String observer, String skill);
    Map<String, Integer> findRankingsFor(String subject);
    PersonEntity findBestPersonFor(String skill);
}
