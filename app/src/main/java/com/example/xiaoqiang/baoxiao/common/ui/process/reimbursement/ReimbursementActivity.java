package com.example.xiaoqiang.baoxiao.common.ui.process.reimbursement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.adapter.PicturesAdapter;
import com.example.xiaoqiang.baoxiao.common.been.PointEntity;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.FastTitleActivity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.NumberFormatterUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.SelectListDialog;
import com.example.xiaoqiang.baoxiao.common.ui.process.controller.IReimbursementView;
import com.example.xiaoqiang.baoxiao.common.ui.process.controller.ReimbursementController;
import com.example.xiaoqiang.baoxiao.common.view.NoScollGridView;
import com.google.gson.Gson;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/**
 * Created: yhx on 2018/4/26
 * Function:
 * Desc:报销填写
 */
public class ReimbursementActivity extends FastTitleActivity<ReimbursementController> implements IReimbursementView, View.OnClickListener {
    private final String TAG = "ReimbursementActivity";
    private EditText mEtDepartment;
    private String[] contents = {"总经理", "财务主管", "部门主管", "财务专员", "出纳", "普通职员"};
    @BindView(R.id.gridView)
    NoScollGridView mGridImgs;
    @BindView(R.id.reimbursement_position_et)
    TextView mEtPosition;//职位
    @BindView(R.id.reimbursement_personnel_et)
    TextView mEtPersonnel;//申请人姓名
    @BindView(R.id.reimbursement_amount_et)
    TextView mEtAmount;//申请金额
    @BindView(R.id.reimbursement_account_et)
    TextView mEtAccount;//职位
    @BindView(R.id.reimbursement_reason_et)
    TextView mEtReason;//原因
    @BindView(R.id.reimbursement_set_out_et)
    TextView mEtSetOUt;//出发地
    @BindView(R.id.reimbursement_destination_et)
    TextView mEtDestination;//目的地
    @BindView(R.id.reimbursement_vehicle_et)
    TextView mEtVehicle;//交通工具
    @BindView(R.id.reimbursement_start_time)
    TextView mTvStartTime;//开始时间
    @BindView(R.id.reimbursement_end_time)
    TextView mTvEndTime;//结束时间
    @BindView(R.id.reimbursement_account_type_tv)
    TextView mTvAccountType;//账号类型
    private ArrayList<AlbumFile> mAlbumFiles, upDataFiles;
    private PicturesAdapter mAdapter;
    private long startTime = -1, endTime = -1;
    private boolean isStartTime;
    private List<StateUser> mUsersList;
    private StateUser indexUser;

    @Override
    public int getContentLayout() {
        //让布局向上移来显示软键盘
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_reimbursement;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        Gson gson = new Gson();
        Timber.tag(TAG).i(gson.toJson(BmobUser.getCurrentUser()));
        indexUser = SpManager.getInstance().getUserInfo();//默認自己
        initUserParameters();
        initGridView();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("报销申请");
    }

    @Override
    protected ReimbursementController initController() {
        return new ReimbursementController().setmContext(this);
    }

    @Override
    protected void attachView() {
        mController.attachView(this);
    }

    @Override
    public void onRequestCompleted() {
        //提交成功
        finish();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onAlbumSuccess(@NonNull ArrayList<AlbumFile> result) {
        if (mAlbumFiles != null && mAlbumFiles.size() > 0) {
            mAlbumFiles.clear();
        }
        upDataFiles = result;
        if (result.size() < 6) {
            AlbumFile af = new AlbumFile();
            af.setPath("000000");
            result.add(af);
        }
        mAlbumFiles.addAll(result);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAlbumCancel(String result) {

    }

    /**
     * 公司人员列表获取成功
     */
    @Override
    public void onShowPersonList(List<StateUser> result) {
        mUsersList = result;
        showSelectDialog();
    }

    private void initUserParameters() {
        if (TextUtils.isEmpty(indexUser.getUser().getNickName())) {
            mEtPersonnel.setText(indexUser.getUser().getUsername());
        } else {
            mEtPersonnel.setText(indexUser.getUser().getNickName());
        }
        mEtPosition.setText(SpManager.getInstance().mPositionManager.get(indexUser.getPosition() + ""));
    }

    private void initGridView() {
        mAlbumFiles = new ArrayList<>();
        AlbumFile af = new AlbumFile();
        af.setPath("000000");
        mAlbumFiles.add(af);
        mAdapter = new PicturesAdapter(mContext, mAlbumFiles);
        mGridImgs.setAdapter(mAdapter);

        mGridImgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                if ("000000".equals(imgs)) {
                    mController.openAlbum(mContext, upDataFiles);
                } else {
                }
            }
        });
    }

    @Override
    @OnClick({R.id.reimbursement_personnel_search, R.id.reimbursement_vehicle_search, R.id.reimbursement_account_search,
            R.id.reimbursement_submit, R.id.reimbursement_start_time, R.id.reimbursement_end_time})
    public void onClick(View v) {
        switch (v.getId()) {
            //人员搜索
            case R.id.reimbursement_personnel_search:
                dialogType = FastConstant.SELECT_DIALOG_USER_MODE;
                if (mUsersList == null) {
                    mController.getPersonListByCompanyId(indexUser.getCompany());
                } else {
                    showSelectDialog();
                }
                break;
            //账号类型选择
            case R.id.reimbursement_account_search:
                dialogType = FastConstant.SELECT_DIALOG_RELATION_ACCOUNT_MODE;
                showSelectDialog();
                break;
            //交通工具搜索
            case R.id.reimbursement_vehicle_search:
                dialogType = FastConstant.SELECT_DIALOG_VEHICLE_MODE;
                showSelectDialog();
                break;
            //选择时间
            case R.id.reimbursement_start_time:
                isStartTime = true;
                showTimePickerDialog();
                break;
            //选择时间
            case R.id.reimbursement_end_time:
                isStartTime = false;
                showTimePickerDialog();
                break;

            //提交
            case R.id.reimbursement_submit:
                submit();
                break;
        }
    }

    private void showTimePickerDialog() {
        Date minDate = TimeFormatUtil.stringToDate("2010-01-01 00:00", FastConstant.TIME_FORMAT_TYPE);
        TimePickerDialog startTimeDialog = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        if (isStartTime) {
                            startTime = millseconds;
                            mTvStartTime.setText(TimeFormatUtil.formatTime(millseconds, FastConstant.TIME_FORMAT_TYPE));
                        } else {
                            endTime = millseconds;
                            mTvEndTime.setText(TimeFormatUtil.formatTime(millseconds, FastConstant.TIME_FORMAT_TYPE));
                        }
                    }
                })
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择时间")
                .setCyclic(false)
                .setMinMillseconds(minDate.getTime())
                .setCurrentMillseconds(System.currentTimeMillis())
//                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
//                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.txt_title_blue))
                .setWheelItemTextSize(12)
                .build();
        startTimeDialog.show(getSupportFragmentManager(), "all");
    }

    /**
     * 提交数据：
     * <p>
     * creatorName;//创建人姓名 M
     * position;//创建人職位 M
     * account;//账号 M
     * accountType;//账号类型 M
     * amount;//金额 M
     * reason;//原因
     * startTime;
     * endTime;
     * userId;//创建人id
     * companyId;//创建人所属公司
     * setout;//出发地
     * destination;//目的地
     * vehicle;//交通工具
     * imgs;//附件
     * point;//当前节点
     * processType;//流程类型 和流程节点 一起判断  1普通員工申請  2主管申請  3總經理申請
     * pointCreateTimes;//節點創建時間
     */
    private void submit() {
        String name = mEtPersonnel.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请填写申请人姓名");
            return;
        }
        String account = mEtAccount.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtil.show("请填写关联账号");
            return;
        }
        String accountType = mTvAccountType.getText().toString().trim();
        if (TextUtils.isEmpty(accountType)) {
            ToastUtil.show("请选择关联账号类型");
            return;
        }
        String amount = mEtAmount.getText().toString().trim();
        if (TextUtils.isEmpty(amount)) {
            ToastUtil.show("请填写报销金额");
            return;
        }
        String reason = mEtReason.getText().toString().trim();
        if (TextUtils.isEmpty(reason)) {
            ToastUtil.show("请填写报销事由");
            return;
        }
        StateUser user = SpManager.getInstance().getUserInfo();
        if (user != null) {
            int myPosition = user.getPosition();
            int processType = 0;
            if (myPosition == 0 || myPosition == 1 || myPosition == 2) {
                processType = FastConstant.PROCESS_TYPE_ONE;
            } else if (myPosition == 3 || myPosition == 4) {
                processType = FastConstant.PROCESS_TYPE_TWO;
            } else if (myPosition == 5) {
                processType = FastConstant.PROCESS_TYPE_THREE;
            }

            ProcessEntity pe = new ProcessEntity();
            //关联信息
            pe.setCreatorName(name);
            pe.setUserId(user.getObjectId());
            pe.setCompanyId(user.getCompany().getObjectId());
            //报销流程信息
            pe.setPosition(myPosition);
            pe.setPoint(FastConstant.PROCESS_POINT_TWO);//去往下一个节点
            pe.setProcessType(processType);
            pe.setAccount(account);
            pe.setAccountType(accountType);
            pe.setAmount(NumberFormatterUtil.parseDouble(amount));
            pe.setReason(reason);

            List<PointEntity> plist = new ArrayList<>();
            PointEntity pointE = new PointEntity();
            pointE.setCreateTime(System.currentTimeMillis());
            pointE.setCreatorName(name);
            pointE.setUserId(user.getObjectId());
            pointE.setCreatorHeadImg(user.getUser().getPhotoPath());
            pointE.setPointStatus(FastConstant.POINT_STATUS_TWO);
            pointE.setPoint(FastConstant.PROCESS_POINT_ONE);
//            pointE.setRemark("发起申请");
            plist.add(pointE);

            PointEntity pointE1 = new PointEntity();
            pointE1.setPointStatus(FastConstant.POINT_STATUS_ONE);
            pointE1.setPoint(FastConstant.PROCESS_POINT_TWO);
            plist.add(pointE1);
            pe.setPointList(plist);

            pe.setCreateTime(System.currentTimeMillis());

            String setout = mEtSetOUt.getText().toString();
            if (!TextUtils.isEmpty(setout)) {
                pe.setSetout(setout);
            }

            String destination = mEtDestination.getText().toString();
            if (!TextUtils.isEmpty(destination)) {
                pe.setDestination(destination);
            }

            String vehicle = mEtVehicle.getText().toString();
            if (!TextUtils.isEmpty(vehicle)) {
                pe.setVehicle(vehicle);
            }

            boolean isTimeSelectFinish = false;
            if (startTime != -1 || endTime != -1) {
                if (startTime != -1 && endTime != -1) {
                    pe.setStartTime(startTime);
                    pe.setEndTime(endTime);
                    isTimeSelectFinish = true;
                }
            } else {
                isTimeSelectFinish = true;
            }
            if (!isTimeSelectFinish) {
                ToastUtil.show("开始时间和结束时间必须关联，不能只填其一。");
                return;
            }

            if (upDataFiles != null && upDataFiles.size() > 0) {
                mController.upDataFiles(upDataFiles, pe);
                return;
            }
            mController.saveProcess(pe);
        }

    }

    private SelectListDialog radioDialog;
    private int dialogType = 0;

    private void showSelectDialog() {
        String title = "请选择人员";
        if (dialogType == FastConstant.SELECT_DIALOG_USER_MODE) {
            title = "请选择人员";
        } else if (dialogType == FastConstant.SELECT_DIALOG_VEHICLE_MODE) {
            title = "请选择交通工具";
        } else if (dialogType == FastConstant.SELECT_DIALOG_RELATION_ACCOUNT_MODE) {
            title = "请选择账号类型";
        }
        if (radioDialog == null) {
            radioDialog = new SelectListDialog(this, mUsersList,
                    dialogType, title)
                    .setItemSelectListener(new SelectListDialog.ItemSelect() {
                        @Override
                        public void onItemUserSelect(StateUser user) {
                            indexUser = user;
                            initUserParameters();
                            ToastUtil.show(user.getUser().getUsername());
                        }

                        @Override
                        public void onItemSelect(String content) {
                            if (dialogType == FastConstant.SELECT_DIALOG_VEHICLE_MODE) {
                                initVehicle(content);
                            } else if (dialogType == FastConstant.SELECT_DIALOG_RELATION_ACCOUNT_MODE) {
                                initAccountType(content);
                            }
                        }
                    });
        } else {
            radioDialog.setTitle(title);
            if (dialogType == FastConstant.SELECT_DIALOG_USER_MODE) {
                radioDialog.setDialogType(mUsersList);
            } else if (dialogType == FastConstant.SELECT_DIALOG_VEHICLE_MODE) {
                radioDialog.setDialogType(dialogType);
            } else if (dialogType == FastConstant.SELECT_DIALOG_RELATION_ACCOUNT_MODE) {
                radioDialog.setDialogType(dialogType);
            }
        }
        radioDialog.show();
    }

    private void initVehicle(String content) {
        mEtVehicle.setText(content);
    }

    private void initAccountType(String content) {
        mTvAccountType.setText(content);
    }

}
