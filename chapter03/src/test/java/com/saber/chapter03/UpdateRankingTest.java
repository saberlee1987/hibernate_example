package com.saber.chapter03;

import com.saber.chapter3.services.RankingService;
import com.saber.chapter3.services.impl.RankingServiceImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UpdateRankingTest {

    private RankingService rankingService;

    static final String SABER66 = "saber66";
    static final String BRUCE40 = "bruce40";
    static final String JAVA = "java";

    @BeforeClass
    public void setup(){
        rankingService = new RankingServiceImpl();
    }
    @Test
    public void updateExistingRanking(){
        rankingService.addRanking(SABER66,BRUCE40,JAVA,9);
        Assert.assertEquals(rankingService.getRankingFor(SABER66,JAVA),9);
        rankingService.updateRanking(SABER66,BRUCE40,JAVA,8);
        Assert.assertEquals(rankingService.getRankingFor(SABER66,JAVA),8);

    }
}
