package com.nowcoder.community.util;

public interface CommunityConstant {
    int ACTIVATION_SUCCESS=0;
    int ACTIVATION_REPEAT=1;
    int ACTIVATION_FAILURE=2;

    //definition of login expired time
    int DEFAULT_EXPIRED_SECOND=3600*12;

    int REMEMBER_EXPIRED_SECOND=3600*24*100;
}
