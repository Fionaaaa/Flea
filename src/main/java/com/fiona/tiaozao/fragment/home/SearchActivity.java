package com.fiona.tiaozao.fragment.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.Goods;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SearchActivity extends AppCompatActivity implements ListViewAdapter.OnItemSelected {

    EditText editText;
    TextView textView;
    ListView listViewHis;

    ArrayList<Goods> list;  //所有出售物品
    ArrayList<Goods> data = new ArrayList<>();  //匹配的物品
    ArrayList<int[]> location = new ArrayList<>(); //匹配的位置
    Set<String> history;
    ListView listView;

    ListViewAdapter adapter;
    int start;
    int end;
    String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editText = (EditText) findViewById(R.id.editText_search);
        textView = (TextView) findViewById(R.id.textView_search);
        listView = (ListView) findViewById(R.id.listView_search);
        listViewHis = (ListView) findViewById(R.id.listView);

        history = getSharedPreferences("user", MODE_PRIVATE).getStringSet("history", null);
        if (history != null) {
            ArrayList<String> list = new ArrayList<>();
            for (String s : history) {
                list.add(s);
            }
            listViewHis.setAdapter(new Adapter(this,list));
        }

        list = (ArrayList<Goods>) Goods.find(Goods.class, "flag=?", "1");  //获得所有出售物品
        adapter = new ListViewAdapter(this, data, location);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);

        //输入监听
        editText.addTextChangedListener(new TextChangedListener());

        //点击键盘的搜索
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clickCancel(editText);
                }
                return false;
            }
        });
    }

    public void clickCancel(View view) {
        String input = editText.getText().toString();
        if (input.length() == 0) {
            finish();   //  取消搜索
        } else {
            //隐藏键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            editText.clearFocus();
            if (data.size() == 0) {
                Toast.makeText(SearchActivity.this, "竹篮打水一场空，你什么也没搜到", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickTaoZiHai(View view) {
        Toast.makeText(SearchActivity.this, "别点这儿", Toast.LENGTH_SHORT).show();
    }

    //保存历史记录
    @Override
    public void onItemSelected() {
        SharedPreferences pf = getSharedPreferences("user", MODE_PRIVATE);
        history = pf.getStringSet("history", null);
        if (history == null) {
            history = new HashSet<>();
        }
        history.add(input);
        pf.edit().putStringSet("history", history).commit();
    }

    /**
     * 输入框监听器
     */
    private class TextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            input = String.valueOf(s);
            if (s.length() > 0) {
                listView.setVisibility(View.VISIBLE);  //listView可见
                textView.setText("确定");
            } else {
                listView.setVisibility(View.INVISIBLE);
                textView.setText("取消");
            }
            if (s.length() > 0) {
                listView.setVisibility(View.VISIBLE);
                data = getData(String.valueOf(s));
                adapter.notifyDataSetChanged();         //数据集改变
                listViewHis.setVisibility(View.INVISIBLE);
            }else{
                listView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    //返回匹配的物品
    private ArrayList<Goods> getData(String input) {
        data.clear();       //每次都清空
        location.clear();
        for (Goods goods : list) {
            String title = goods.getTitle();
            if (title.contains(input)) {
                data.add(goods);
                start = title.indexOf(input);
                end = title.lastIndexOf(input);
                int[] loc = new int[]{start, end};
                location.add(loc);
            }
        }
        return data;
    }

    private class Adapter extends BaseAdapter {
        ArrayList<String> data;
        LayoutInflater inflater;

        public Adapter(Context context, ArrayList<String> data) {
            this.data = data;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.simple_list_item_1, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.textView3);
            textView.setText(data.get(position));
            return convertView;
        }
    }
}
