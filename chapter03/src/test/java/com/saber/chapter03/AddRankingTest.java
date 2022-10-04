package com.saber.chapter03;

import com.saber.chapter3.services.RankingService;
import com.saber.chapter3.services.impl.RankingServiceImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AddRankingTest {

    private RankingService rankingService;

    @BeforeClass
    public void setup(){
        rankingService = new RankingServiceImpl();
    }
    @Test
    public void addRanking(){
        rankingService.addRanking("saber66","bruce40","java",9);
        int ranking = rankingService.getRankingFor("saber66", "java");
        Assert.assertEquals(ranking,9);
    }
}
