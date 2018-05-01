package com.example.xiaoqiang.baoxiao.common.view;

import com.example.xiaoqiang.baoxiao.common.been.Company;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;

import java.util.List;

public interface QueryView extends BmobView {

    void onQuerySuccess(List<Company> result);

    void onQueryCompayCreator(MyUser name);
}
