package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet.list;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet_1.AddRecforSEED_1;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet_1.printDec_1;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet_3.AddRecforSEED_3;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet_3.printDec_3;
import static com.example.myapplication.co.amcn.ppcl.ppclSeed.PPCLSeed.decrypt;
import static com.example.myapplication.co.amcn.ppcl.ppclSeed.PPCLSeed.encrypt;

public class highActivity extends AppCompatActivity implements SensorEventListener{

    public static String charset = "utf-8";

    //비밀키(암호화 대칭키)
   public static byte pbUserKey[] = { (byte) 0x2c, (byte) 0x11, (byte) 0x19, (byte) 0x1d, (byte) 0x1f, (byte) 0x16, (byte) 0x12,
            (byte) 0x12, (byte) 0x11, (byte) 0x19, (byte) 0x1d, (byte) 0x1f, (byte) 0x10, (byte) 0x14, (byte) 0x1b,
            (byte) 0x16 };

   //CBC 대칭키 - 사용자가 지정하는 초기화 벡터
    public static byte bszIV[] = { (byte) 0x27, (byte) 0x28, (byte) 0x27, (byte) 0x6d, (byte) 0x2d, (byte) 0xd5, (byte) 0x4e,
            (byte) 0x29, (byte) 0x2c, (byte) 0x56, (byte) 0xf4, (byte)0x2a, (byte) 0x65, (byte) 0x2a, (byte) 0xae,
            (byte) 0x08 };

    ArrayList<String> arrayList;
    public static List<Sensor> sensor;
    private SensorManager sm;

    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mLight;
    private Sensor mGravity;
    private Sensor mStepCounter;

    private TextView sensorName;
    public static TextView sensorReturn;

    private float ACC_var0, ACC_var1, ACC_var2;
    private float GYRO_0, GYRO_1, GYRO_2;
    private float Light_0;
    private float Gv_0, Gv_1, Gv_2;
    private float Step_0;
    private String sensing_date=null;
    public static byte [] a_enc = null;
    public static byte [] b_enc = null;
    public static byte [] c_enc = null;

    public static String dec_string = "";


    private String str_default="일반적인 센서가 아닙니다.";

    public static int s_position=0;

    SQLiteDatabase sqlDB;
    MyDatabaseOpenHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high);


        sensorName = (TextView)findViewById(R.id.sensorName);
        sensorReturn = (TextView)findViewById(R.id.sensorReturn);
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

        ArrayList<String> arrayList = new ArrayList<String>();
        for(int i = 0; i < sensor.size(); i++){
            Sensor s = sensor.get(i); //i번째 sensor정보 가져오기
            arrayList.add(s.getName());
        }

        ArrayAdapter<String> Adapter;
        Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        ListView listView = (ListView)findViewById(R.id.sensorLIst);
        listView.setAdapter(Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                s_position=position;

                sensorName.setText(sensor.get(position).getName()); //센서 클릭했을 때 textview에 이름 출력

                try {
                    catchReturn(position);

                    //DBSearch("SensorData", sensor.get(position).getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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

    public void catchReturn(int position) throws Exception {

        //Sensor date(시간정보)
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String sensing_date = sdfNow.format(date);

        if(sensor.get(position).getType()==1){
            long initTime = System.currentTimeMillis();

            saveFile(sensor.get(position).getName(), ACC_var0, ACC_var1, ACC_var2, sensing_date);
            saveDB(sensor.get(position).getName(), ACC_var0, ACC_var1, ACC_var2, sensing_date);
            long endTime = System.currentTimeMillis();
            Log.i("시간", String.valueOf((endTime-initTime)/1000.0));

            DBSearch("SensorData", sensor.get(position).getName());
        }
        else if(sensor.get(position).getType()==4){
            saveFile(sensor.get(position).getName(), GYRO_0, GYRO_1, GYRO_2, sensing_date);
            saveDB(sensor.get(position).getName(), GYRO_0, GYRO_1, GYRO_2, sensing_date);
            DBSearch("SensorData", sensor.get(position).getName());
        }
        else if(sensor.get(position).getType()==5){
            saveFile(sensor.get(position).getName(), Light_0, sensing_date);
            saveDB(sensor.get(position).getName(), Light_0, sensing_date);
            DBSearch("SensorData", sensor.get(position).getName());
        }
        else if(sensor.get(position).getType()==9){
            saveFile(sensor.get(position).getName(), Gv_0, Gv_1, Gv_2, sensing_date);
            saveDB(sensor.get(position).getName(), Gv_0, Gv_1, Gv_2, sensing_date);
            DBSearch("SensorData", sensor.get(position).getName());
        }
        else if(sensor.get(position).getType()==19){
            saveFile(sensor.get(position).getName(), Step_0, sensing_date);
            saveDB(sensor.get(position).getName(), Step_0, sensing_date);
            DBSearch_1("SensorData", sensor.get(position).getName());
        }
        else{
            sensorReturn.setText(str_default);
            //levelTxt.setText("");
        }
    } //센서 이름으로 파일 생성

    public void saveFile(String filename, float a, float b, float c, String sensing_date) throws Exception { //센서 return값이 x,y,z일 때 파일 저장함수

        String str = "x = " + a + "\ny = "+b +"\nz = "+ c + "\n";


        a_enc = encrypt(Float.toString(a));
        b_enc = encrypt(Float.toString(b));
        c_enc = encrypt(Float.toString(c)); //sensing하는 순간 암호화...


        AddRecforSEED_3(sensor.get(s_position).getName(), a, b, c, a_enc, b_enc, c_enc, sensing_date);

        //sensorReturn.setText(printDec_3(sensor.get(s_position).getName()));

        String save = "";

        for(int i = 0; i< list.size(); i++) {

            if(filename.equals(list.get(i).getColumnValbyname("SensorName"))) {
                save += "Sensor Name : " + list.get(i).getColumnValbyname("SensorName") + "\n"
                        + "x = " + decrypt(list.get(i).GetencColumnforSEEDbyname("SensingData_x")) + "\n" +
                        "y = " + decrypt(list.get(i).GetencColumnforSEEDbyname("SensingData_y")) + "\n" +
                        "z = " + decrypt(list.get(i).GetencColumnforSEEDbyname("SensingData_z")) + "\n"
                        + "Time : " + list.get(i).getColumnValbyname("Sensing_time") + "\n\n";
            }

        }


            String filePath = "";

            filePath = Environment.getExternalStorageDirectory() + "/";

            File saveFile = new File(filePath);
            if (!saveFile.exists()) {
                saveFile.mkdir();
            }

            try {
                String fileName = filename;

                BufferedWriter bw = new BufferedWriter(new FileWriter(filePath + fileName + ".txt", false)); //true로 하면 원본에서 이어서 쓰기
                bw.write(save);
                bw.close();

                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
    }


    public void saveFile(String filename, float x, String sensing_date) throws Exception{ //return값이 하나일 경우 파일에 저장하는 함수

        String x_str = Float.toString(x);

        //암호화
        String plainText = x_str; //평문

        String x_enc = encrypt(x_str).toString() + "\n";

        String enc = x_enc;

        a_enc = encrypt(x_str);

        AddRecforSEED_1(sensor.get(s_position).getName(), x, a_enc, sensing_date); //암호화된 센서값과 그에 해당하는 센서 이름을 record로 추가
        //sensorReturn.setText(printDec_1(sensor.get(s_position).getName()));

        String save = "";

        for(int i = 0; i< list.size(); i++) {

            if(filename.equals(list.get(i).getColumnValbyname("SensorName"))) {
                save += "Sensor Name : " + list.get(i).getColumnValbyname("SensorName") + "\n"
                        + "x = " + decrypt(list.get(i).GetencColumnforSEEDbyname("SensingData_x")) + "\n"
                        + "Time : " + list.get(i).getColumnValbyname("Sensing_time") + "\n\n";
            }

        }


        String filePath = "";

        filePath = Environment.getExternalStorageDirectory() + "/";

        File saveFile = new File(filePath);
        if (!saveFile.exists()) {
            saveFile.mkdir();
        }

        try {
            String fileName = filename;

            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath + fileName + ".txt", false)); //true로 하면 원본에서 이어서 쓰기
            bw.write(save);
            bw.close();

            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveDB(String filename, float a, float b, float c, String sensing_date){
        sqlDB = myHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("NAME", filename);
        contentValues.put("X", "");
        contentValues.put("Y", "");
        contentValues.put("Z", "");
        contentValues.put("time", sensing_date);
        contentValues.put("X_enc", a_enc);
        contentValues.put("Y_enc", b_enc);
        contentValues.put("Z_enc", c_enc);

        long id = sqlDB.insert("SensorData", null, contentValues);

        sqlDB.close();

    }

    public void saveDB(String filename, float x, String sensing_date){
        sqlDB = myHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("NAME", filename);
        contentValues.put("X", "");
        contentValues.put("Y", "");
        contentValues.put("Z", "");
        contentValues.put("time", sensing_date);
        contentValues.put("X_enc", a_enc);
        contentValues.put("Y_enc", "");
        contentValues.put("Z_enc", "");

        long id = sqlDB.insert("SensorData", null, contentValues);

        sqlDB.close();
    }

    public void DBSearch(String tablename, String sensorName){
        String data = "";

        sqlDB = myHelper.getWritableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM SensorData;", null);

        while (cursor.moveToNext()){

            String name = cursor.getString(cursor.getColumnIndex("Name"));
            byte[] x_enc = cursor.getBlob(cursor.getColumnIndex("X_enc"));
            String x = decrypt(x_enc);

            byte[] y_enc = cursor.getBlob(cursor.getColumnIndex("Y_enc"));
            String y = decrypt(y_enc);

            byte[] z_enc = cursor.getBlob(cursor.getColumnIndex("Z_enc"));
            String z = decrypt(z_enc);

            //String time = cursor.getString(cursor.getColumnIndex("time"));

            if(name.equals(sensorName)) { //센서이름이 같은 경우만 출력
                data += "x = " + x + "\ny = " + y + "\nz = " + z + "\n\n";
            }

        }
        //levelTxt.setText(data);
        sensorReturn.setText(data);

        cursor.close();
        sqlDB.close();

        }

    public void DBSearch_1(String tablename, String sensorName){
        String data = "";

        sqlDB = myHelper.getWritableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM SensorData;", null);

        while (cursor.moveToNext()){

            String name = cursor.getString(cursor.getColumnIndex("Name"));
            byte[] x_enc = cursor.getBlob(cursor.getColumnIndex("X_enc"));
            String x = decrypt(x_enc);

            //String time = cursor.getString(cursor.getColumnIndex("time"));

            if(name.equals(sensorName)) { //센서이름이 같은 경우만 출력
                data += "x = " + x + "\n";
            }

        }
        //levelTxt.setText(data);
        sensorReturn.setText(data);

        cursor.close();
        sqlDB.close();

    }

}
