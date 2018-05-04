package com.example.xiaoqiang.baoxiao.common.view;

import com.example.xiaoqiang.baoxiao.common.been.Applicant;
import com.example.xiaoqiang.baoxiao.common.been.Company;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;

import java.util.List;

public interface QueryView extends BmobView {

    void onQuerySuccess(List<Company> result);

    void onQueryCompayCreator(MyUser name);

    void onQueryRequester(List<Applicant> result);

    void onQueryStateUser(List<StateUser> result);
}
