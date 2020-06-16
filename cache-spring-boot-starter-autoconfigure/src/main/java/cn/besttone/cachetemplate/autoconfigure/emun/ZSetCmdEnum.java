package cn.besttone.cachetemplate.autoconfigure.emun;

/**
 * @Author: Huanghz
 * @Description: Zset操作
 * @Date:Created in 4:57 下午 2020/6/5
 * @ModifyBy:
 **/
public enum ZSetCmdEnum implements CacheTemplateCmd {
    addOne,add,remove,incrementScore,rank,reverseRank,rangeWithScores,reverseRangeWithScores,rangeByScoreWithScores,reverseRangeByScoreWithScores,
    count,zCard,score,removeRange,removeRangeByScore
}
