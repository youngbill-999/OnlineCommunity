package com.nowcoder.community.util;

public interface CommunityConstant {
    int ACTIVATION_SUCCESS=0;
    int ACTIVATION_REPEAT=1;
    int ACTIVATION_FAILURE=2;

    //definition of login expired time
    int DEFAULT_EXPIRED_SECOND=3600*12;

    int REMEMBER_EXPIRED_SECOND=3600*24*100;


    /**
     * 实体类型: 帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型: 评论
     */
    int ENTITY_TYPE_COMMENT = 2;
}
