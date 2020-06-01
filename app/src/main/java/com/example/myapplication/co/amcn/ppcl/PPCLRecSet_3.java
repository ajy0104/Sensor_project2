package com.example.myapplication.co.amcn.ppcl;

import android.hardware.Sensor;
import android.text.TextUtils;

import java.io.IOException;

import static com.example.myapplication.highActivity.dec_string;
import static com.example.myapplication.highActivity.s_position;
import static com.example.myapplication.highActivity.sensor;
import static com.example.myapplication.highActivity.sensorReturn;
import static com.example.myapplication.co.amcn.ppcl.ppclSeed.PPCLSeed.decrypt;
import static com.example.myapplication.highActivity.a_enc;
import static com.example.myapplication.highActivity.b_enc;
import static com.example.myapplication.highActivity.c_enc;

public class PPCLRecSet_3 extends PPCLRecSet {

    static Column<Float> SensingData_x = null;
    static Column<Float> SensingData_y = null;
    static Column<Float> SensingData_z = null;

    static Column<String> Sensing_time = null; //sensing data를 얻은 시간

    public static void AddRecforSEED_3(String name, float x, float y, float z, byte[] a_enc, byte[] b_enc, byte[] c_enc, String time) { //센서값이 x,y,z일 경우

        PPCLRec rec = new PPCLRec();

        SensorName = new Column<>(String.class, "SensorName");
        SensingData_x = new Column<>(Float.class, "SensingData_x");
        SensingData_y = new Column<>(Float.class, "SensingData_y");
        SensingData_z = new Column<>(Float.class, "SensingData_z");
        Sensing_time = new Column<>(String.class, "Sensing_time");

        rec.putColumn(SensorName, name, "SensorName");
        rec.putColumn(SensingData_x, x, "SensingData_x");
        rec.putColumn(SensingData_y, y, "SensingData_y");
        rec.putColumn(SensingData_z, z, "SensingData_z");
        rec.putColumn(Sensing_time, time, "Sensing_time"); //column에 값 추가

        rec.SetencColumnforSEED(SensingData_x, a_enc);
        rec.SetencColumnforSEED(SensingData_y, b_enc);
        rec.SetencColumnforSEED(SensingData_z, c_enc); //암호화된 값 rec변수에 저장

        list.add(rec);//record set을 list에 추가

    }

    public static void AddRec_3(String name, float x, float y, float z, String time) { //센서값이 x,y,z일 경우

        PPCLRec rec = new PPCLRec();

        SensorName = new Column<>(String.class, "SensorName");
        SensingData_x = new Column<>(Float.class, "SensingData_x");
        SensingData_y = new Column<>(Float.class, "SensingData_y");
        SensingData_z = new Column<>(Float.class, "SensingData_z");
        Sensing_time = new Column<>(String.class, "Sensing_time");

        rec.putColumn(SensorName, name, "SensorName");
        rec.putColumn(SensingData_x, x, "SensingData_x");
        rec.putColumn(SensingData_y, y, "SensingData_y");
        rec.putColumn(SensingData_z, z, "SensingData_z");
        rec.putColumn(Sensing_time, time, "Sensing_time"); //column에 값 추가

        list.add(rec);//record set을 list에 추가

    }

    public static String printDec_3(String name){

        dec_string = ""; //문자열 초기화

        for(int i = 0; i<list.size(); i++) {

            PPCLRec rec = new PPCLRec();
            rec = list.get(i);

            if (rec.getColumnValbyname("SensorName").equals(name)){ //list의 센서이름과 event발생한 센서이름 같을 경우만 출력

                dec_string += "x = " + decrypt(rec.GetencColumnforSEEDbyname("SensingData_x")) + "\n" +
                        "y = " + decrypt(rec.GetencColumnforSEEDbyname("SensingData_y")) +"\n" +
                        "z = " + decrypt(rec.GetencColumnforSEEDbyname("SensingData_z")) + "\n\n";

            }

        }
        return dec_string;
    }

    public static String print_3(String name){

        dec_string = ""; //문자열 초기화

        for(int i = 0; i<list.size(); i++) {

            PPCLRec rec = new PPCLRec();
            rec = list.get(i);

            if (rec.getColumnValbyname("SensorName").equals(name)){ //list의 센서이름과 event발생한 센서이름 같을 경우만 출력

                dec_string += "x = " + rec.getColumnValbyname("SensingData_x") + "\n" +
                        "y = " + rec.getColumnValbyname("SensingData_y") +"\n" +
                        "z = " + rec.getColumnValbyname("SensingData_z") + "\n\n";

            }

        }
        return dec_string;
    }
}
