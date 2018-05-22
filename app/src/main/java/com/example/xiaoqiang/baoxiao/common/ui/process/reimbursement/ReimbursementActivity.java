package com.example.xiaoqiang.baoxiao.common.ui.process.reimbursement;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.adapter.PicturesAdapter;
import com.example.xiaoqiang.baoxiao.common.adapter.TimeLineAdapter;
import com.example.xiaoqiang.baoxiao.common.been.AreaEntity;
import com.example.xiaoqiang.baoxiao.common.been.PointEntity;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.FastTitleActivity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.ThreadPoolManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.FastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.NumberFormatterUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.SelectListDialog;
import com.example.xiaoqiang.baoxiao.common.ui.TravelWayActivity;
import com.example.xiaoqiang.baoxiao.common.ui.process.controller.IReimbursementView;
import com.example.xiaoqiang.baoxiao.common.ui.process.controller.ReimbursementController;
import com.example.xiaoqiang.baoxiao.common.view.NoScollGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.yanzhenjie.album.AlbumFile;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created: yhx on 2018/4/26
 * Function:
 * Desc:报销填写
 */
public class ReimbursementActivity extends FastTitleActivity<ReimbursementController> implements IReimbursementView, View.OnClickListener {
    private final String TAG = "ReimbursementActivity";
    private EditText mEtDepartment;
    @BindView(R.id.gridView)
    NoScollGridView mGridImgs;
    @BindView(R.id.reimbursement_position_et)
    TextView mEtPosition;//职位
    @BindView(R.id.reimbursement_personnel_et)
    TextView mEtPersonnel;//申请人姓名
    @BindView(R.id.reimbursement_amount_et)
    EditText mEtAmount;//申请金额
    @BindView(R.id.reimbursement_account_et)
    EditText mEtAccount;//账号
    @BindView(R.id.reimbursement_reason_et)
    EditText mEtReason;//原因
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
    @BindView(R.id.reimbursement_point_listview)
    RecyclerView mTimeline;
    @BindView(R.id.reimbursement_is_travel_on_business_img)
    ImageView mCheckImg;
    //    @BindView(R.id.reimbursement_reference_price_tv)
//    TextView mTvVehiclePriceReference;
    private ArrayList<AlbumFile> mAlbumFiles, upDataFiles;
    private PicturesAdapter mAdapter;
    private long startTime = -1, endTime = -1;
    private boolean isStartTime;
    private List<StateUser> mUsersList;
    private StateUser indexUser;
    private int pageStyle = 0;//0申请  1修改
    private ProcessEntity mProcessEntity;
    private boolean isSetout;
    private boolean isTravel = false;//是否是差旅
    private ArrayList<AreaEntity> mProvinces = new ArrayList<>();
    private ArrayList<ArrayList<String>> mCities = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<>();

    @Override
    public int getContentLayout() {
        //让布局向上移来显示软键盘
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        pageStyle = getIntent().getIntExtra("pageStyle", 0);
        return R.layout.activity_reimbursement;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        processAreaData();
        indexUser = SpManager.getInstance().getUserInfo();//默認自己
        initGridView();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        String title = "报销申请";
        if (pageStyle == 1) {
            title = "修改申请";
        }
        titleBar.setTitleMainText(title);
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
        if (pageStyle == 0) {
            mEtPersonnel.setText(indexUser.getUser().getRealname());
            mEtPosition.setText(SpManager.getInstance().mPositionManager.get(indexUser.getPosition()));
        } else {
            mProcessEntity = new Gson().fromJson(getIntent().getStringExtra("processEntity"), ProcessEntity.class);
            mEtPersonnel.setText(mProcessEntity.getCreatorName());
            mEtPosition.setText(SpManager.getInstance().mPositionManager.get(mProcessEntity.getPosition()));
            mEtAccount.setText(mProcessEntity.getAccount());
            mTvAccountType.setText(mProcessEntity.getProcessType());
            mEtReason.setText(mProcessEntity.getReason());
            if (mProcessEntity.getAmount() != null) {
                mEtAmount.setText(mProcessEntity.getAmount() + "");
            }

            if (!TextUtils.isEmpty(mProcessEntity.getSetout())) {
                mEtSetOUt.setText(mProcessEntity.getSetout());
            }

            if (!TextUtils.isEmpty(mProcessEntity.getDestination())) {
                mEtDestination.setText(mProcessEntity.getDestination());
            }

            if (!TextUtils.isEmpty(mProcessEntity.getVehicle())) {
                mEtVehicle.setText(mProcessEntity.getVehicle());
            }

            if (mProcessEntity.getStartTime() != null) {
                mTvStartTime.setText(TimeFormatUtil.formatTime(mProcessEntity.getStartTime(), FastConstant.TIME_FORMAT_TYPE));
            }

            if (mProcessEntity.getEndTime() != null) {
                mTvEndTime.setText(TimeFormatUtil.formatTime(mProcessEntity.getEndTime(), FastConstant.TIME_FORMAT_TYPE));
            }

            mTimeline.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mTimeline.setHasFixedSize(true);
            mTimeline.setNestedScrollingEnabled(false);
            TimeLineAdapter adapter = new TimeLineAdapter(mProcessEntity.getPointList(), false, mProcessEntity.getPoint(), mProcessEntity
                    .getProcessType());
            mTimeline.setAdapter(adapter);
        }
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
        initUserParameters();
    }

    @Override
    @OnClick({R.id.reimbursement_personnel_search, R.id.reimbursement_set_out_search
            , R.id.reimbursement_destination_search, R.id.reimbursement_vehicle_search
            , R.id.reimbursement_account_search, R.id.reimbursement_submit
            , R.id.reimbursement_start_time, R.id.reimbursement_end_time
            , R.id.reimbursement_is_travel_on_business_img, R.id.reimbursement_vehicle_price_question})
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
            //出发地
            case R.id.reimbursement_set_out_search:
                isSetout = true;
                selectCity();
                break;
            //目的地
            case R.id.reimbursement_destination_search:
                isSetout = false;
                selectCity();
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
            //是否差旅
            case R.id.reimbursement_is_travel_on_business_img:
                isTravel = !isTravel;
                if (isTravel) {
                    mCheckImg.setImageResource(R.drawable.ic_selected);
                    findViewByViewId(R.id.reimbursement_travel_on_business_l).setVisibility(View.VISIBLE);
                } else {
                    mCheckImg.setImageResource(R.drawable.ic_unchecked);
                    findViewByViewId(R.id.reimbursement_travel_on_business_l).setVisibility(View.GONE);
                }
                break;
            //查看参考价
            case R.id.reimbursement_vehicle_price_question:
                if (TextUtils.isEmpty(mEtSetOUt.getText().toString())) {
                    ToastUtil.show("出发地不能为空");
                    return;
                }
                if (TextUtils.isEmpty(mEtDestination.getText().toString())) {
                    ToastUtil.show("目的地不能为空");
                    return;
                }
                if (TextUtils.isEmpty(mEtVehicle.getText().toString())) {
                    ToastUtil.show("交通工具不能为空");
                    return;
                }
                ToastUtil.show("查看参考价");
                Bundle bundle = new Bundle();
                bundle.putString("from", mEtSetOUt.getText().toString().split(" ")[1]);
                bundle.putString("to", mEtDestination.getText().toString().split(" ")[1]);
                String mode = "2";
                if (TextUtils.equals("飞机", mEtVehicle.getText().toString())) {
                    mode = "2";
                } else if (TextUtils.equals("火车", mEtVehicle.getText().toString())) {
                    mode = "4";
                }
                bundle.putString("mode", mode);
                FastUtil.startActivity(this, TravelWayActivity.class, bundle);
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
            ToastUtil.show("请填写收款账号，支付宝或微信");
            return;
        }
        String accountType = mTvAccountType.getText().toString().trim();
        if (TextUtils.isEmpty(accountType)) {
            ToastUtil.show("请选择收款账号类型");
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
            pe.setDepartmentId(user.getDepartment());
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


            boolean isPass = true;//判断是否差旅信息填写完整
            String str = "";
            String setout = mEtSetOUt.getText().toString();
            String destination = mEtDestination.getText().toString();
            String vehicle = mEtVehicle.getText().toString();
            if (isTravel) {
                if (TextUtils.isEmpty(setout)) {
                    isPass = false;
                    str = "差旅报销,请填写出发地";
                }

                if (isPass && TextUtils.isEmpty(destination)) {
                    isPass = false;
                    str = "差旅报销,请填写目的地";
                }

                if (isPass && TextUtils.isEmpty(vehicle)) {
                    isPass = false;
                    str = "差旅报销,请填写交通工具";
                }
                if (isPass && startTime == -1) {
                    isPass = false;
                    str = "差旅报销,请填写开始日期";
                }

                if (isPass && endTime == -1) {
                    isPass = false;
                    str = "差旅报销,请填写截止日期";
                }

            }

            if (!isPass) {
                ToastUtil.show(str);
                return;
            }

            if (isTravel) {
                pe.setSetout(setout);
                pe.setDestination(destination);
                pe.setVehicle(vehicle);
                pe.setStartTime(startTime);
                pe.setEndTime(endTime);
                pe.setTravel(isTravel);
//                pe.setVehiclePriceReference(mTvVehiclePriceReference.getText().toString());
            }

            if (!TextUtils.equals(indexUser.getUser().getObjectId(), user.getUser().getObjectId())) {
                ToastUtil.show("账号与申请人不一致，申请人必须是本人。");
                return;
            }

            Timber.i(new Gson().toJson(pe));
            if (upDataFiles != null && upDataFiles.size() > 0) {
                mController.upDataFiles(upDataFiles, pe);
                return;
            }
            mController.saveProcess(pe);
        }

    }

    /**
     * 选择地区
     */
    private void selectCity() {
        if (mProvinces.size() == 0) {
            return;
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                // 返回的分别是三个级别的选中位置
                String tx = mProvinces.get(options1).getPickerViewText() + " " +
                        mCities.get(options1).get(options2) + " " +
                        mDistricts.get(options1).get(options2).get(options3);
                String result = mProvinces.get(options1).getPickerViewText();
                if (mCities.get(options1) != null && mCities.get(options1).size() > 0 && mCities.get(options1).get(options2) != null) {
                    result += " " + mCities.get(options1).get(options2);
                    if (mDistricts.get(options1).get(options2) != null && mDistricts.get(options1).get(options2).size() > 0) {
//                        result += " " + mDistricts.get(options1).get(options2).get(options3);
                    } else {
                    }
                } else {
                }
                if (isSetout) {
                    initSetoutTxt(result);
                } else {
                    initDestinationTxt(result);
                }
            }
        })
                .setTitleText("选择城市")
                .setTitleColor(ContextCompat.getColor(this, R.color.colorWhite))
                .setSubmitColor(ContextCompat.getColor(this, R.color.colorWhite))
                .setCancelColor(ContextCompat.getColor(this, R.color.colorWhite))
                .setTitleBgColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setSubCalSize(17)// 确定、取消按钮大小设置
                .setLineSpacingMultiplier(2f)
                .setBgColor(Color.parseColor("#D8D8D8"))
                .setDividerColor(ContextCompat.getColor(this, R.color.colorDivider))
                .setTextColorCenter(ContextCompat.getColor(this, R.color.colorPrimaryTitle)) // 设置选中项文字颜色
                .setContentTextSize(16)
                .setOutSideCancelable(false)// default is true
                .build();
        pvOptions.setPicker(mProvinces, mCities);// 二级选择器
//        pvOptions.setPicker(mProvinces, mCities, mDistricts);// 三级选择器
        pvOptions.show();


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

    private void initAccountType(String content) {
        mTvAccountType.setText(content);
    }

    private void initSetoutTxt(String content) {
        mEtSetOUt.setText(content);
        initReferencePrice();
    }

    private void initDestinationTxt(String content) {
        mEtDestination.setText(content);
        initReferencePrice();
    }

    private void initVehicle(String content) {
        mEtVehicle.setText(content);
        initReferencePrice();
    }

    private void initReferencePrice() {
     /*   if (!TextUtils.isEmpty(mEtSetOUt.getText().toString())
                && !TextUtils.isEmpty(mEtDestination.getText().toString())
                && !TextUtils.isEmpty(mEtVehicle.getText().toString())) {
//            String price = "";
            //提供交通参考价
//            mTvVehiclePriceReference.setText(price);
        }*/
    }

    /**
     * 处理地区数据
     */
    private void processAreaData() {
        ThreadPoolManager.getInstance().execute(new Thread() {
            @Override
            public void run() {
                InputStream is;
                try {
                    StringBuffer sb = new StringBuffer();
                    is = getAssets().open("cities.json");
                    int length = is.available();
                    byte[] buffer = new byte[length];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        sb.append(new String(buffer, 0, len, Charset.forName("UTF-8")));
                    }
                    is.close();
                    ArrayList<AreaEntity> areaEntities = new Gson().fromJson(sb.toString(), new TypeToken<ArrayList<AreaEntity>>() {
                    }.getType());

                    mProvinces = areaEntities;

                    for (int i = 0; i < mProvinces.size(); i++) {//遍历省份
                        ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
                        ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

                        for (int c = 0; c < mProvinces.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                            String CityName = mProvinces.get(i).getCityList().get(c).getName();
                            CityList.add(CityName);//添加城市
                            ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                            //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                            if (mProvinces.get(i).getCityList().get(c).getArea() == null
                                    || mProvinces.get(i).getCityList().get(c).getArea().size() == 0) {
                                City_AreaList.add("");
                            } else {
                                City_AreaList.addAll(mProvinces.get(i).getCityList().get(c).getArea());
                            }
                            Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                        }

                        /**
                         * 添加城市数据
                         */
                        mCities.add(CityList);

                        /**
                         * 添加地区数据
                         */
                        mDistricts.add(Province_AreaList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
