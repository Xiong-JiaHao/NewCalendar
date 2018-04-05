package com.gin.xjh.newcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gin on 2018/4/3.
 */

public class NewCalendar extends LinearLayout {

    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;
    private int month;

    private Calendar curDate = Calendar.getInstance();

    private String displayFormat;

    public NewCalendarListener mlistener;

    public NewCalendar(Context context) {
        super(context);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context,attrs);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context,attrs);
    }

    private void initControl(Context context,AttributeSet attrs) {
        bindControl(context);
        bindControlEvent();

        TypedArray ta = getContext().obtainStyledAttributes(attrs,R.styleable.NewCalendar);
        try {
            String format = ta.getString(R.styleable.NewCalendar_dateFormat);
            displayFormat = format;
            if(displayFormat == null){
                displayFormat = "MMM yyyy";
            }
        }
        finally {
            ta.recycle();
        }

        renderCalendar();
    }

    private void bindControl(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_view,this);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        txtDate = findViewById(R.id.txtDate);
        grid = findViewById(R.id.calendar_grid);
    }

    private void bindControlEvent() {
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH,-1);
                renderCalendar();
            }
        });
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH,1);
                renderCalendar();
            }
        });
    }

    private void renderCalendar() {
        //格式设置
        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);
        txtDate.setText(sdf.format(curDate.getTime()));

        final ArrayList<Date> cells = new ArrayList<>();
        //不影响正常的数据
        Calendar calendar = (Calendar) curDate.clone();

        calendar.set(Calendar.DAY_OF_MONTH,1);
        int prevDays = calendar.get(Calendar.DAY_OF_WEEK)-1;
        calendar.add(Calendar.DAY_OF_MONTH,-prevDays);

        int maxCellCount = 4*7;
        while(cells.size()<maxCellCount){
            if(cells.size()==7){
                month=calendar.getTime().getMonth();
            }
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        if(calendar.getTime().getMonth() == month){
            for (int i = 0 ; i < 7 ; i++){
                cells.add(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH,1);
            }
        }
        if(calendar.getTime().getMonth() == month){
            for (int i = 0 ; i < 7 ; i++){
                cells.add(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH,1);
            }
        }
        grid.setAdapter(new CalendarAdapter(getContext(),cells));
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(mlistener == null){
                    return false;
                }
                else{
                    mlistener.onItemLongPress(cells.get(position));
                    return true;
                }
            }
        });
    }

    private class CalendarAdapter extends ArrayAdapter<Date>{

        public LayoutInflater inflater;

        public CalendarAdapter(@NonNull Context context, ArrayList<Date> days) {
            super(context, R.layout.calendar_text_day, days);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Date date = getItem(position);

            if(convertView == null){
                convertView = inflater.inflate(R.layout.calendar_text_day,parent,false);
            }

            int day = date.getDate();
            ((Calendar_day_textView)convertView).setText(String.valueOf(day));

            Date now = new Date();//没有初始化情况下就是当前的时间


            if(month == date.getMonth()){
                if(now.getDate() == date.getDate() && now.getMonth() == date.getMonth() && now.getYear() == date.getYear()){//是不是今天
                    ((Calendar_day_textView)convertView).setTextColor(Color.parseColor("#ff0000"));
                    ((Calendar_day_textView)convertView).isToday=true;
                }
                else{
                    ((Calendar_day_textView)convertView).setTextColor(Color.parseColor("#000000"));
                }
            }
            else{
                ((Calendar_day_textView)convertView).setTextColor(Color.parseColor("#666666"));
            }

            return convertView;
        }
    }
    public interface NewCalendarListener{
        void onItemLongPress(Date day);
    }
}
