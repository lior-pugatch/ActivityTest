package com.example.user1.activitytest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressLint("ClickableViewAccessibility")

public class ShowTimersActivity extends AppCompatActivity {

    TableLayout table;
    private ArrayList<Timer> timerList;
    private static String TAG = "ShowTimersActivity";

    private TextView createTableCell(String text) {
        TextView tv = new TextView(this);

        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tv.setBackground(getResources().getDrawable(R.drawable.cell_shape, null));
        tv.setPadding(30, 30, 30, 30);

        return tv;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_timers);
        Log.d(TAG, "In on create");

        table = (TableLayout) findViewById(R.id.timersTable);

        timerList = getIntent().getParcelableArrayListExtra("TimersList");

        Log.d(TAG, "Got timer's list, len: " + timerList.size());
        final Drawable d1 = getResources().getDrawable(R.drawable.gradient_bg_hover, null);

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[] {android.R.attr.state_pressed}, d1);
     //   states.addState(new int[] {-android.R.attr.state_focused},g);

        for (int i = 0; i < timerList.size(); i++){
            Log.d(TAG, "Starting adding data to row...");
            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER);
            row.setLongClickable(true);
            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    view.setBackground(new ColorDrawable(Color.RED));
                    Intent intent = getIntent();
                    intent.putExtra("TimerIdx", view.getId());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    return true;
                }
            });

           /* row.setOnClickListener(new View.OnClickListener (){
            @Override
                public void onClick(View view) {
                view.setBackground(d1);
            }*/

           row.setOnTouchListener(new View.OnTouchListener(){
               @Override
               public boolean onTouch(View view, MotionEvent motionEvent){
                   switch (motionEvent.getAction()) {
                       case MotionEvent.ACTION_DOWN:
                           view.setBackground(d1);
                           break;
                       case MotionEvent.ACTION_UP:
                           view.setBackground(new ColorDrawable(Color.WHITE));
                           break;
                   }

                   /* Return false here so the event is not consumed and click listner can handle it further*/
                   return false;
               }

           });

            row.setSelected(false);
            row.setId(i);
            //row.setBackground(states);
            Timer timer = timerList.get(i);
/*
            TextView tvControllerId = new TextView(this);
            tvControllerId.setText(Constants.tapsList[timer.getControllerId()]);
            tvControllerId.setGravity(Gravity.CENTER);
            tvControllerId.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            tvControllerId.setBackground(getResources().getDrawable(R.drawable.cell_shape, null));
            tvControllerId.setPadding(30, 30, 30, 30);

            TextView tvDay = new TextView(this);
            tvDay.setText(timer.getDayString());
            tvDay.setGravity(Gravity.CENTER);
            tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            tvDay.setBackground(getResources().getDrawable(R.drawable.cell_shape, null));
            tvDay.setPadding(30, 30, 30, 30);

            TextView tvTime = new TextView(this);
            tvTime.setText(timer.getTimeString());
            tvTime.setGravity(Gravity.CENTER);
            tvTime.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            tvTime.setBackground(getResources().getDrawable(R.drawable.cell_shape, null));
            tvTime.setPadding(30, 30, 30, 30);

            TextView tvDuration = new TextView(this);
            tvDuration.setText(timer.getDuration() + " Mins");
            tvDuration.setGravity(Gravity.CENTER);
            tvDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            tvDuration.setBackground(getResources().getDrawable(R.drawable.cell_shape, null));
            tvDuration.setPadding(30, 30, 30, 30);
*/
            row.addView(createTableCell(Constants.tapsList[timer.getControllerId()]));
            row.addView(createTableCell(timer.getDayString()));
            row.addView(createTableCell(timer.getTimeString()));
            row.addView(createTableCell(timer.getDuration() + " Mins"));


            Log.d(TAG, "Finished adding data to row, adding row to table...");
            table.addView(row);
        }
/*        TableRow emptyRow = new TableRow(this);
        emptyRow.setBackground(states);
        table.addView(emptyRow);*/
    }
}
