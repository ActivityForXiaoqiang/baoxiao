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
    private Double budgetAmount;
    private Double sumAmountProcess;
    private int cordon;

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

    public Double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(Double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public Double getSumAmountProcess() {
        return sumAmountProcess;
    }

    public void setSumAmountProcess(Double sumAmountProcess) {
        this.sumAmountProcess = sumAmountProcess;
    }

    public int getCordon() {
        return cordon;
    }

    public void setCordon(int cordon) {
        this.cordon = cordon;
    }
}
