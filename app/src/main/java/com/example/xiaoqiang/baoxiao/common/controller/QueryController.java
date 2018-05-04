package com.example.xiaoqiang.baoxiao.common.controller;

import com.example.xiaoqiang.baoxiao.common.been.Applicant;
import com.example.xiaoqiang.baoxiao.common.been.Company;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.model.BmobModel;
import com.example.xiaoqiang.baoxiao.common.view.QueryView;

import java.util.List;

import rx.functions.Action1;

public class QueryController {
    private QueryView view;
    private BmobModel model;

    public QueryController(QueryView view) {
        this.view = view;
        model = new BmobModel();
    }


    public void queryAllCompany() {
        view.showDialog();
        model.queryAllCompay().subscribe(new Action1<List<Company>>() {
            @Override
            public void call(List<Company> companies) {
                view.hideDialog();
                view.onQuerySuccess(companies);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.hideDialog();
                view.showError(throwable);
            }
        });
    }

    public void queryCompany(String userId) {
        model.queryCompany(userId).subscribe(new Action1<List<Company>>() {
            @Override
            public void call(List<Company> companies) {
                view.onQuerySuccess(companies);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.showError(throwable);
            }
        });
    }

    public void queryCompanyCreator(String objId) {
        model.queryCompanyCreator(objId).subscribe(new Action1<MyUser>() {
            @Override
            public void call(MyUser user) {
                view.onQueryCompayCreator(user);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.showError(throwable);
            }
        });
    }

    public void queryRester(String companyId) {
        view.showDialog();
        model.queryRequester(companyId).subscribe(new Action1<List<Applicant>>() {
            @Override
            public void call(List<Applicant> applicants) {
                view.onQueryRequester(applicants);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.showError(throwable);
            }
        });
    }

    public void queryStatuser(MyUser user) {
        view.showDialog();
        model.queryStatuser(user).subscribe(new Action1<List<StateUser>>() {
            @Override
            public void call(List<StateUser> stateUsers) {
                view.hideDialog();
                view.onQueryStateUser(stateUsers);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.hideDialog();
                view.showError(throwable);
            }
        });
    }

    public void queryCompanyUser(Company company) {
        model.queryCompanyUser(company).subscribe(new Action1<List<StateUser>>() {
            @Override
            public void call(List<StateUser> stateUsers) {
                view.onQueryCompanyUser(stateUsers);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.showError(throwable);
            }
        });
    }
}
