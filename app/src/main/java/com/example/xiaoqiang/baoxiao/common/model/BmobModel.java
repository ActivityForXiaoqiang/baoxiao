package com.example.xiaoqiang.baoxiao.common.model;

import com.example.xiaoqiang.baoxiao.common.been.Applicant;
import com.example.xiaoqiang.baoxiao.common.been.Company;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import rx.Observable;

public class BmobModel {

    public rx.Observable<MyUser> login(String account, String password) {
        return BmobUser.loginByAccountObservable(MyUser.class, account, password);
    }

    public Observable<MyUser> signup(String account, String password) {
        MyUser user = new MyUser();
        user.setUsername(account);
        user.setPassword(password);
        user.setSex(true);
        user.setJoinCompany(false);
        user.setSuper(false);
        return user.signUpObservable(MyUser.class);
    }

    public Observable<Void> updataUser(MyUser user) {
        return user.updateObservable();
    }

    public Observable<String> createCompany(String name, String miaoshu) {
        Company company = new Company();
        company.setName(name);
        company.setDescribe(miaoshu);
        company.setCreator(BmobUser.getCurrentUser(MyUser.class));
        return company.saveObservable();
    }

    public Observable<List<Company>> queryAllCompay() {
        BmobQuery<Company> query = new BmobQuery<>();
        query.include("creator");
        return query.findObjectsObservable(Company.class);
    }

    public Observable<List<Company>> queryCompany(String userid) {
        BmobQuery<Company> query = new BmobQuery<>();
        query.addWhereEqualTo("creator", userid);
        query.include("creator");
        return query.findObjectsObservable(Company.class);
    }


    public Observable<MyUser> queryCompanyCreator(String objId) {
        BmobQuery<MyUser> bmobQuery = new BmobQuery<>();
        return bmobQuery.getObjectObservable(MyUser.class, objId);

    }

    public Observable<List<Applicant>> queryRequester(String comgpanyId) {
        BmobQuery<Applicant> query = new BmobQuery<>();
        query.addWhereEqualTo("companyId", comgpanyId);
        query.include("user");
        return query.findObjectsObservable(Applicant.class);
    }

    public Observable<String> createRequest(Applicant applicant) {
        return applicant.saveObservable();
    }

}
