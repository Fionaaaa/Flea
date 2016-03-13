package com.fiona.tiaozao.fragment.myself;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.fiona.tiaozao.App;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.interactor.Interactor;
import com.fiona.tiaozao.net.NetQueryImpl;

public class SettingActivity extends AppCompatActivity {

    Switch switchA;
    Switch switchB;
    Switch switchC;

    TextView textViewA;
    TextView textViewB;
    TextView textViewC;

    SharedPreferences pf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        pf = getSharedPreferences("user", MODE_PRIVATE);

        textViewA = (TextView) findViewById(R.id.textView_a);
        textViewB = (TextView) findViewById(R.id.textView_b);
        textViewC = (TextView) findViewById(R.id.textView_c);

        switchA = (Switch) findViewById(R.id.switchA);
        switchA.setOnCheckedChangeListener(new SwitchListener(1, textViewA));

        switchB = (Switch) findViewById(R.id.switchB);
        switchB.setOnCheckedChangeListener(new SwitchListener(2, textViewB));

        switchC = (Switch) findViewById(R.id.switchC);
        switchC.setOnCheckedChangeListener(new SwitchListener(3, textViewC));

        initView();
    }

    private void initView() {
        switchA.setChecked(pf.getBoolean(App.SETTING_WIFI, false));
        switchB.setChecked(pf.getBoolean(App.SETTING_STALL, false));
        switchC.setChecked(pf.getBoolean(App.SETTING_GOODS, false));
    }

    /**
     * 点击返回
     *
     * @param view
     */
    public void clickBackSettingActivity(View view) {
        finish();
    }

    /**
     * Switch监听
     */
    class SwitchListener implements CompoundButton.OnCheckedChangeListener {
        TextView textView;
        int i;

        SwitchListener(int i, TextView textView) {
            this.i = i;
            this.textView = textView;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                textView.setText("开启");
            } else {
                textView.setText("关闭");
            }
            switch (i) {
                case 1:
                    if (isChecked) {
                        pf.edit().putBoolean(App.SETTING_WIFI, true).commit();
                    } else {
                        pf.edit().putBoolean(App.SETTING_WIFI, false).commit();
                    }
                    break;
                case 2:
                    if (isChecked) {
                        pf.edit().putBoolean(App.SETTING_STALL, true).commit();
                        Interactor.getNotify(NetQueryImpl.getInstance(SettingActivity.this),SettingActivity.this);
                    } else {
                        pf.edit().putBoolean(App.SETTING_STALL, false).commit();
                    }
                    break;
                case 3:
                    if (isChecked) {
                        pf.edit().putBoolean(App.SETTING_GOODS, true).commit();
                        Interactor.getNotify(NetQueryImpl.getInstance(SettingActivity.this), SettingActivity.this);
                    } else {
                        pf.edit().putBoolean(App.SETTING_GOODS, false).commit();
                    }
                    break;
            }
        }
    }
}
