package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet.list;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet_1.AddRec_1;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet_1.AddRecforSEED_1;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet_1.printDec_1;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet_1.print_1;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet_3.AddRec_3;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet_3.AddRecforSEED_3;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet_3.printDec_3;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet_3.print_3;
import static com.example.myapplication.co.amcn.ppcl.ppclSeed.PPCLSeed.decrypt;
import static com.example.myapplication.co.amcn.ppcl.ppclSeed.PPCLSeed.encrypt;

public class mediumActivity extends AppCompatActivity implements SensorEventListener {

    ListView sensorList;
    ArrayList<String> sensorData;
    private SensorManager sm;
    public static List<Sensor> sensor;
    public static TextView sensorName;
    public static TextView sensorReturn;

    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mLight;
    private Sensor mGravity;
    private Sensor mStepCounter;

    public static float ACC_var0, ACC_var1, ACC_var2;
    public static float GYRO_0, GYRO_1, GYRO_2;
    public static float Light_0;
    public static float Gv_0, Gv_1, Gv_2;
    public static float Step_0;

    private String sensing_date=null;


    private String str_default="일반적인 센서가 아닙니다.";

    public static MyDatabaseOpenHelper myHelper;
    public static SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medium);

        myHelper = new MyDatabaseOpenHelper(this, "SensorData", null, 1);

        sqlDB = myHelper.getWritableDatabase();
        myHelper.onUpgrade(sqlDB, 1, 2); //db초기화

        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getSensorList(Sensor.TYPE_ALL); //센서 장치목록 저장

        mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //1
        mGyroscope = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE); //4
        mLight = sm.getDefaultSensor(Sensor.TYPE_LIGHT); //5
        mGravity = sm.getDefaultSensor(Sensor.TYPE_GRAVITY); //9
        mStepCounter = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER); //19

        sensorData = new ArrayList<String>();

        for(int i = 0;i<sensor.size();i++){
            sensorData.add(sensor.get(i).getName());
        }

        sensorList = findViewById(R.id.sensorLIst);
        ButtonListAdapter buttonListAdapter = new ButtonListAdapter(this, sensorData);

        sensorList.setAdapter(buttonListAdapter);

        sensorName = findViewById(R.id.sensorName);
        sensorReturn = findViewById(R.id.sensorReturn);

        Intent i = this.getIntent();
        String level = i.getStringExtra("level");
        //levelTxt.setText("선택하신 강도 : " + level);

    }

    @Override
    protected void onResume() {
        super.onResume();

        sm.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        //리스너 등록
    }

    @Override
    protected void onStop() {
        super.onStop();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            ACC_var0 = event.values[0]; //x
            ACC_var1 = event.values[1]; //y
            ACC_var2 = event.values[2]; //z
        }
        else if (event.sensor.getType()==Sensor.TYPE_GYROSCOPE) {
            GYRO_0 = event.values[0]; //x
            GYRO_1 = event.values[1]; //y
            GYRO_2 = event.values[2]; //z

        }
        else if(event.sensor.getType()==Sensor.TYPE_LIGHT) {
            Light_0 = event.values[0]; //lux

        }
        else if(event.sensor.getType()==Sensor.TYPE_GRAVITY) {
            Gv_0 = event.values[0]; //x
            Gv_1 = event.values[1]; //y
            Gv_2 = event.values[2]; //z


        }
        else if(event.sensor.getType()==Sensor.TYPE_STEP_COUNTER) {
            Step_0 = event.values[0]; //step Counter

        }
        else{

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
