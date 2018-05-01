package com.example.xiaoqiang.baoxiao.common.ui.info;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;

public class FillActivity extends MyBaseActivity {

    EditText editText;

    @Override
    public Integer getViewId() {
        return R.layout.activity_fill;
    }

    @Override
    public void init() {
        editText = findViewById(R.id.fill_edit);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(FillActivity.this, "尚未天填写内容！", Toast.LENGTH_SHORT).show();

        } else {
            Intent intent = new Intent();
            intent.putExtra("text", editText.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
