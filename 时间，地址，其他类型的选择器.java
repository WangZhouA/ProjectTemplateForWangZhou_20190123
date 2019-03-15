package fule.com.wheelpickerdemo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fule.com.mydatapicker.DataPickerDialog;
import fule.com.mydatapicker.DatePickerDialog;
import fule.com.mydatapicker.DateUtil;
import fule.com.mydatapicker.TimePickerDialog;
import fule.com.mywheelview.bean.AddressDetailsEntity;
import fule.com.mywheelview.bean.AddressModel;
import fule.com.mywheelview.weight.wheel.ChooseAddressWheel;
import fule.com.mywheelview.weight.wheel.OnAddressChangeListener;
import fule.com.wheelpickerdemo.util.JsonUtil;
import fule.com.wheelpickerdemo.util.Utils;

@SuppressLint("DefaultLocale")
/**
 * 作者： njb
 * 时间： 2018/8/29 0029-下午 4:51
 * 描述：  日期、时间、地址选择器
 * 来源：
 */
public class MainActivity extends AppCompatActivity {
    private Dialog dateDialog, timeDialog, chooseDialog;
    private TextView mTextView;
    private List<String> list = new ArrayList<>();
    private ChooseAddressWheel chooseAddressWheel;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initWheel();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mTextView = findViewById(R.id.textView);
        String[] data = getResources().getStringArray(R.array.list);
        list.addAll(Arrays.asList(data));
    }

    /**
     * 初始化地址
     */
    private void initWheel() {
        chooseAddressWheel = new ChooseAddressWheel(this);
        chooseAddressWheel.setOnAddressChangeListener(new OnAddressChangeListener() {
            @Override
            public void onAddressChange(String province, String city, String district) {
                address = (province + " " + city + " " + district);
                mTextView.setText(address);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String address = Utils.readAssert(this, "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDetailsEntity data = model.Result;
            if (data == null) return;
            if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                chooseAddressWheel.setProvince(data.ProvinceItems.Province);
                chooseAddressWheel.defaultValue(data.Province, data.City, data.Area);
            }
        }
    }

    /**
     * 选择其他
     * @param view
     */
    public void btnOther(View view) {
        showChooseDialog(list);
    }

    /**
     * 选择时间
     * @param view
     */
    public void btnTime(View view) {
        showTimePick();
    }

    /**
     * 选择日期
     * @param view
     */
    public void btnDate(View view) {
        showDateDialog(DateUtil.getDateForString("2000-02-14"));
    }

    /**
     * 选择地址
     * @param view
     */
    public void btnAddress(View view) {
        chooseAddressWheel.show(view);
    }

    /**
     * 显示其他列表
     */
    private void showChooseDialog(List<String> mlist) {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        chooseDialog = builder.setData(mlist).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue, int position) {
                        mTextView.setText(itemValue);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).create();
        chooseDialog.show();
    }


    /**
     * 显示日期
     * @param date
     */
    private void showDateDialog(List<Integer> date) {
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(this);
        builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {
                mTextView.setText(String.format("%d-%s-%s", dates[0], dates[1] > 9 ? dates[1] : ("0" + dates[1]), dates[2] > 9 ? dates[2] : ("0" + dates[2])));
            }

            @Override
            public void onCancel() {

            }
        })
                .setSelectYear(date.get(0) - 1)
                .setSelectMonth(date.get(1) - 1)
                .setSelectDay(date.get(2) - 1);
        builder.setMaxYear(DateUtil.getYear());
        builder.setMaxMonth(DateUtil.getDateForString(DateUtil.getToday()).get(1));
        builder.setMaxDay(DateUtil.getDateForString(DateUtil.getToday()).get(2));
        dateDialog = builder.create();
        dateDialog.show();
    }

    /**
     * 显示时间
     */
    private void showTimePick() {
        if (timeDialog == null) {
            TimePickerDialog.Builder builder = new TimePickerDialog.Builder(this);
            timeDialog = builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
                @Override
                public void onTimeSelected(int[] times) {
                    mTextView.setText(String.format("%d:%d", times[0], times[1]));
                }
            }).create();
        }
        timeDialog.show();
    }
}
