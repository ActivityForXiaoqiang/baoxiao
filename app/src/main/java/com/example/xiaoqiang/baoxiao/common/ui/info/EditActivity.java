package com.example.xiaoqiang.baoxiao.common.ui.info;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.controller.UpdataController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;
import com.example.xiaoqiang.baoxiao.common.listener.OnItemClickListener;
import com.example.xiaoqiang.baoxiao.common.listener.RecyclerItemClickListener;
import com.example.xiaoqiang.baoxiao.common.view.UpdataView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.util.Date;

import cn.bmob.v3.BmobUser;

public class EditActivity extends MyBaseActivity implements UpdataView {
    public static final int NIKE_NAME = 0;
    public static final int SEX = 1;
    public static final int REAL_NAME = 2;
    public static final int CODE = 3;
    public static final int BIRTHDAY = 4;
    private String[][] items = {{"昵称", "1"}, {"性别", "1"}, {"真实姓名", "1"}, {"身份证", "1"}, {"出生日期", "1"}};

    private RecyclerView recyclerView;

    private eAdapter adapter;

    private UpdataController controller;

    MyUser user = BmobUser.getCurrentUser(MyUser.class);

    @Override
    public Integer getViewId() {
        return R.layout.activity_infoedit;
    }

    @Override
    public void init() {

        if (!TextUtils.isEmpty(user.getNickName())) {
            items[NIKE_NAME][1] = user.getNickName();
        }
        if (user.isSex()) {
            items[SEX][1] = "男";
        } else {
            items[SEX][1] = "女";
        }
        adapter = new eAdapter();
        controller = new UpdataController(this);
        recyclerView = findViewById(R.id.info_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case NIKE_NAME:
                        startActivityForResult(new Intent(EditActivity.this, FillActivity.class), NIKE_NAME);
                        break;
                    case SEX:
                        choseSex();
                        break;
                    case REAL_NAME:
                        startActivityForResult(new Intent(EditActivity.this, FillActivity.class), REAL_NAME);
                        break;
                    case CODE:
                        startActivityForResult(new Intent(EditActivity.this, FillActivity.class), CODE);
                        break;
                    case BIRTHDAY:
                        showTimePickerDialog();
                        break;
                }
            }
        }));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String text = data.getStringExtra("text");
            switch (requestCode) {
                case NIKE_NAME:

                    items[NIKE_NAME][1] = text;
                    adapter.notifyDataSetChanged();
                    break;
                case REAL_NAME:
                    items[REAL_NAME][1] = text;
                    adapter.notifyDataSetChanged();
                    break;
                case CODE:
                    items[CODE][1] = text;
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    AlertDialog dialog;

    void choseSex() {
        dialog = new AlertDialog.Builder(this, R.style.Theme_Transparenr).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_chose_sex);
        final RadioButton boy, girl;
        boy = window.findViewById(R.id.sex_boy);
        girl = window.findViewById(R.id.sex_girl);
        boy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                items[SEX][1] = boy.getText().toString();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        girl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                items[SEX][1] = girl.getText().toString();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


    }

    void updata() {
        if (items[NIKE_NAME][1].equals("1")) {
            Toast.makeText(this, "昵称未填写！", Toast.LENGTH_SHORT).show();
            return;
        }
        user.setNickName(items[NIKE_NAME][1]);
        if (!items[SEX][1].equals("1")) {
            if (items[SEX][1].equals("男")) {
                user.setSex(true);
            } else {
                user.setSex(false);
            }
        }
        if (!items[REAL_NAME][1].equals("1")) {
            user.setRealname(items[REAL_NAME][1]);
        }
        if (!items[CODE][1].equals("1")) {
            user.setCode(items[CODE][1]);
        }
        if (!items[BIRTHDAY][1].equals("1")) {
            user.setBirthday(items[BIRTHDAY][1]);
        }

        controller.updataUser(user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        updata();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdataUserSuccess() {
        finish();
    }

    @Override
    public void onUpdataStateUserSuccess() {

    }

    @Override
    public void showDialog() {
        loadingDialog.show();
    }

    @Override
    public void hideDialog() {
        loadingDialog.hide();
    }

    @Override
    public void showError(Throwable throwable) {

    }


    private void showTimePickerDialog() {
        Date minDate = TimeFormatUtil.stringToDate("1970-01-01 00:00", FastConstant.TIME_FORMAT_TYPE);
        TimePickerDialog startTimeDialog = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        items[BIRTHDAY][1] = TimeFormatUtil.formatTime(millseconds, FastConstant.TIME_FORMAT_TYPE);
                        adapter.notifyDataSetChanged();
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
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
//                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.txt_title_blue))
                .setWheelItemTextSize(12)
                .build();
        startTimeDialog.show(getSupportFragmentManager(), "all");
    }


    class eViewHodler extends RecyclerView.ViewHolder {

        TextView item, content;

        public eViewHodler(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.info_item_name);
            content = itemView.findViewById(R.id.info_item_content);
        }
    }


    class eAdapter extends RecyclerView.Adapter<eViewHodler> {

        @NonNull
        @Override
        public eViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(EditActivity.this).inflate(R.layout.item_info, parent, false);
            return new eViewHodler(view);
        }

        @Override
        public void onBindViewHolder(@NonNull eViewHodler holder, int position) {
            holder.item.setText(items[position][0]);
            if (!items[position][1].equals("1")) {
                holder.content.setText(items[position][1]);
            }
        }

        @Override
        public int getItemCount() {
            return items.length;
        }
    }


}
