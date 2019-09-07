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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressLint("ClickableViewAccessibility")

public class ShowTimersActivity extends AppCompatActivity {

    TableLayout table;
    private ArrayList<Timer> timerList;
    ArrayList<TableRow> rows = new ArrayList<>();

    private static String TAG = "ShowTimersActivity";
    private static int timerIdxToEdit;
    private boolean isUpdateNeeded;

    private TextView createTableCell(String text) {
        TextView tv = new TextView(this);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,
                1.0f
        );


        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tv.setBackground(getResources().getDrawable(R.drawable.cell_shape, null));
        tv.setPadding(30, 30, 30, 30);
       // tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        tv.setLayoutParams(params);
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
        isUpdateNeeded = false;

        Log.d(TAG, "Got timer's list, len: " + timerList.size());



     //   states.addState(new int[] {-android.R.attr.state_focused},g);

        Log.d(TAG, "Starting adding data to row...");
        drawTimerTable();

    }

    private void drawTimerTable(){
        clearTimerTable();

        for (int i = 0; i < timerList.size(); i++){
            drawTimer(i);
        }
    }

    private void drawTimer(int idx){
        TableRow row = new TableRow(this);
        TableRow headRow = (TableRow) findViewById(R.id.tableRow1);
        row.setLayoutParams(headRow.getLayoutParams());
        final Drawable d1 = getResources().getDrawable(R.drawable.gradient_bg_hover, null);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[] {android.R.attr.state_pressed}, d1);

        row.setLongClickable(true);
        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.setBackground(new ColorDrawable(Color.RED));

                timerIdxToEdit = view.getId();
                Timer timerToEdit = timerList.get(timerIdxToEdit);
                Log.d(TAG, "Returned after long click from table, user requested to edit timer "+ timerIdxToEdit);
                Intent intent = new Intent(ShowTimersActivity.this, AlarmActivity.class);
                intent.putExtra("timer", timerToEdit);
                startActivityForResult(intent, Constants.INTENT_REQUEST_EDIT);

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
        row.setId(idx);

        Timer timer = timerList.get(idx);

        row.addView(createTableCell(Constants.tapsList[timer.getControllerId()]));
        row.addView(createTableCell(timer.getDayString()));
        row.addView(createTableCell(timer.getTimeString()));
        row.addView(createTableCell(timer.getDuration() + " Mins"));


        Log.d(TAG, "Finished adding data to row, adding row to table...");
        table.addView(row);
        rows.add(row);
    }

    private void clearTimerTable(){
        for (int i = 0; i < rows.size(); i++){
            table.removeView(rows.get(i));
        }
        rows.clear();
    }

    public void OnClick_btnTimerAdd(View view) {
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivityForResult(intent, Constants.INTENT_REQUEST_NEW);
    }
    public void OnClick_btnTimerDone(View view) {

        Intent intent = getIntent();
        intent.putExtra("UpdatedTimersList", timerList);
        if (isUpdateNeeded == true) {
            setResult(Constants.RETURN_VALUE_UPDATE_NEEDED, intent);
        }
        finish();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        Log.d(TAG,"in override of startActivityForResult, request code: " + requestCode);
        intent.putExtra("requestCode", requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.INTENT_REQUEST_NEW && resultCode == Activity.RESULT_OK){
            Timer timer = (Timer) data.getParcelableExtra("timer");
            timerList.add(timer);
            Log.d(TAG,"Added timer to array, day: " + timer.getDayString() + ", Time: "
                    + timer.getHour() + ":" + timer.getMinute() + ", ctrl id: " + timer.getControllerId()
                    + ", duration: " + timer.getDuration());
            //Do whatever you want with yourData
            isUpdateNeeded = true;
            drawTimerTable();
        }
        else if (requestCode == Constants.INTENT_REQUEST_EDIT && resultCode == Constants.RETURN_VALUE_UPDATE)
        {
            Timer timer = (Timer) data.getParcelableExtra("timer");
            Log.d(TAG,"Got timer After Edit, day: " + timer.getDayString() + ", Time: "
                    + timer.getHour() + ":" + timer.getMinute() + ", ctrl id: " + timer.getControllerId()
                    + ", duration: " + timer.getDuration());

            isUpdateNeeded = true;
            timerList.set(timerIdxToEdit, timer);
            drawTimerTable();
        }
        else if (requestCode == Constants.INTENT_REQUEST_EDIT && resultCode == Constants.RETURN_VALUE_DELETE)
        {
            Log.d(TAG,"Deleting timer" + timerIdxToEdit);
            isUpdateNeeded = true;
            timerList.remove(timerIdxToEdit);
            drawTimerTable();
        }
    }
}
