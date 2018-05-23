package com.example.xiaoqiang.baoxiao.common.been;

import cn.bmob.v3.BmobObject;

/**
 * author : yhx
 * time   : 2018/5/9
 * desc   :
 */

public class BudgetEntity extends BmobObject {
    private String companyId;
    private long monthTime;
    private double budgetAmount;
    private double sumAmountProcess;
    private int cordon;
    private int department;//部门；

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public long getMonthTime() {
        return monthTime;
    }

    public void setMonthTime(long monthTime) {
        this.monthTime = monthTime;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public double getSumAmountProcess() {
        return sumAmountProcess;
    }

    public void setSumAmountProcess(double sumAmountProcess) {
        this.sumAmountProcess = sumAmountProcess;
    }

    public int getCordon() {
        return cordon;
    }

    public void setCordon(int cordon) {
        this.cordon = cordon;
    }
}

