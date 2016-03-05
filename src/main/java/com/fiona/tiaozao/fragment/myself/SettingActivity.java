package com.fiona.tiaozao.fragment.myself;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.fiona.tiaozao.R;

public class SettingActivity extends AppCompatActivity {

    Switch switchA;
    Switch switchB;
    Switch switchC;

    TextView textViewA;
    TextView textViewB;
    TextView textViewC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        textViewA = (TextView) findViewById(R.id.textView_a);
        textViewB = (TextView) findViewById(R.id.textView_b);
        textViewC = (TextView) findViewById(R.id.textView_c);

        switchA = (Switch) findViewById(R.id.switchA);
        switchA.setOnCheckedChangeListener(new SwitchListener(textViewA));

        switchB = (Switch) findViewById(R.id.switchB);
        switchB.setOnCheckedChangeListener(new SwitchListener(textViewB));

        switchC = (Switch) findViewById(R.id.switchC);
        switchC.setOnCheckedChangeListener(new SwitchListener(textViewC));
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

        SwitchListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                textView.setText("开启");
            } else {
                textView.setText("关闭");
            }
        }
    }
}
