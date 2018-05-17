package com.example.xiaoqiang.baoxiao.common.been;

import com.chad.library.adapter.base.entity.SectionEntity;

public class MySection extends SectionEntity<StateUser> {
    public boolean isBig = false;

    public MySection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MySection(StateUser stateUser) {
        super(stateUser);
    }
}
