package com.example.xiaoqiang.baoxiao.common.model;

import com.example.xiaoqiang.baoxiao.common.been.Applicant;
import com.example.xiaoqiang.baoxiao.common.been.Company;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;

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
        user.setSuper(false);
        return user.signUpObservable(MyUser.class);
    }

    public Observable<Void> updataUser(MyUser user) {
        return user.updateObservable();
    }

    public Observable<Void> updataUser(MyUser user, String uerID) {

        return user.updateObservable(uerID);
    }

    public Observable<List<StateUser>> queryCompanyUser(Company company) {
        BmobQuery<StateUser> query = new BmobQuery<>();
        query.addWhereEqualTo("company", company);
        query.include("user");
        return query.findObjectsObservable(StateUser.class);

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


    public Observable<Void> delectReuest(String objID) {
        Applicant applicant = new Applicant();
        return applicant.deleteObservable(objID);
    }

    public Observable<String> createStateUser(StateUser user) {

        return user.saveObservable();
    }

    public Observable<List<StateUser>> queryStatuser(MyUser user) {
        BmobQuery<StateUser> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.include("company");
        return query.findObjectsObservable(StateUser.class);
    }

    public Observable<Void> updataStateUser(StateUser user, String id) {
        return user.updateObservable(id);
    }


}
