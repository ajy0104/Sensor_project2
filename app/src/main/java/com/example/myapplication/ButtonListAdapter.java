package com.example.myapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import static com.example.myapplication.mediumActivity.ACC_var0;
import static com.example.myapplication.mediumActivity.ACC_var1;
import static com.example.myapplication.mediumActivity.ACC_var2;
import static com.example.myapplication.mediumActivity.GYRO_0;
import static com.example.myapplication.mediumActivity.GYRO_1;
import static com.example.myapplication.mediumActivity.GYRO_2;
import static com.example.myapplication.mediumActivity.Gv_0;
import static com.example.myapplication.mediumActivity.Gv_1;
import static com.example.myapplication.mediumActivity.Gv_2;
import static com.example.myapplication.mediumActivity.Light_0;
import static com.example.myapplication.mediumActivity.Step_0;
import static com.example.myapplication.mediumActivity.myHelper;
import static com.example.myapplication.mediumActivity.sensor;
import static com.example.myapplication.mediumActivity.sensorName;
import static com.example.myapplication.mediumActivity.sensorReturn;
import static com.example.myapplication.mediumActivity.sqlDB;

public class ButtonListAdapter extends BaseAdapter implements SensorEventListener {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> data;
    private String str_default="일반적인 센서가 아닙니다.";
    //public static String dec_string = "";
    private Switch switchView;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    //

    public static byte [] a_enc2 = null;
    public static byte [] b_enc2 = null;
    public static byte [] c_enc2 = null;

    public String sensor_func = "";
    public int percentage = 70;
    public String scenario="";

    public static int s_position=0;

    public ButtonListAdapter(Context context, ArrayList<String> data){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
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
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.layout_medium, null);

        TextView textView = view.findViewById(R.id.txtName);
        textView.setText(data.get(position));


        View bodyView = view.findViewById(R.id.body);
        switchView = view.findViewById(R.id.exSwitch);


        bodyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //s_position=position;

                sensorName.setText(sensor.get(position).getName()); //센서 클릭했을 때 textview에 이름 출력

                //Toast.makeText(context, position + "번째 click body" , Toast.LENGTH_SHORT).show();

                try {
                    catchReturn(position, switchView.isChecked());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {

                if(isChecked){
                    //check 되었을 때
                    AlertDialog.Builder builder = new AlertDialog.Builder(buttonView.getContext());
                    builder.setTitle("센서 암호화 설정");
                    int sensorType_num = sensor.get(position).getType(); //센서 타입 번호
                    String sensorType = "";

                    //int percentage = 20; //나중에 데이터베이스에서 값 가져와서 넣기...

                    if(sensorType_num==1){
                        sensorType = "TYPE_ACCELEROMETER";
                        sensor_func = "그리고 이동에 따른 가속도를 감지하고 이 센서를 통해 스마트폰의 움직임을 확인할 수 있고, " +
                                "만보계 앱, 나침반 앱 등을 구현할 수 있습니다.";
                        scenario = "당신의 움직임을 숨길 수 없습니다.";
                    }
                    else if(sensorType_num==4){
                        sensorType = "TYPE_GYROSCOPE";
                        sensor_func = "그리고 물체의 회전각을 감지해 " +
                                "레이싱 게임 앱에서 스마트폰을 기울여서 자동차의 방향을 바꿀 수 있습니다.";
                        scenario = "당신의 휴대폰 사용 여부를 숨길 수 없습니다.";
                    }
                    else if(sensorType_num==5){
                        sensorType = "TYPE_LIGHT";
                        sensor_func = "그리고 빛의 세기를 감지하는 센서입니다.";
                        scenario = "당신이 있는 공간의 밝기를 숨길 수 없습니다.";
                    }
                    else if(sensorType_num==9){
                        sensorType = "TYPE_GRAVITY";
                        sensor_func = "그리고 축의 방향 및 중력을 감지합니다.";
                        scenario = "당신의 휴대폰 사용 여부를 숨길 수 없습니다.";
                    }
                    else if(sensorType_num==19){
                        sensorType = "TYPE_STEP_COUNTER";
                        //readSenor(sensorType);
                        sensor_func = "그리고 사용자의 발걸음을 감지하고 카운팅하는 센서입니다.";
                        scenario = "당신의 이동 횟수를 숨길 수 없습니다.";
                    }
                    else{
                        sensorType = "특수한 센서";
                        sensor_func = "";
                    }

                    builder.setMessage("선택하신 센서의 타입은 " + sensorType + "입니다.\n" + sensor_func + "\n 침해 시나리오 : "+ scenario +
                            "\n해당 센서에 대해 암호화를 설정하시겠습니까?\n\n" + "해당 센서에 대한 암호화 설정 통계 수치 : " + percentage + "%");

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //데이터 베이스에 정보 넘겨주기...코드 추가

                            databaseReference.child("Sensor").push().setValue("50");
                        }
                    });

                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //스위치 off
                            buttonView.setChecked(!isChecked);
                        }
                    });
                    builder.show();
                }
                else{
                    //check 되어있지 않을 때
                }
            }
        });

        return view;
    }

    //추가한 함수부분
    /*private void readSenor(String type){

        int ref = 1;
        databaseReference.child("Sensor").child(type).setValue(ref); //event발생

        databaseReference.child("Sensor").child(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Sensor_info.class) != null){
                    Sensor_info post = dataSnapshot.getValue(Sensor_info.class);
                    //Log.w("FireBaseData", "getData" + post.toString());
                    sensor_func = post.getDescription();
                    percentage = post.getPercentage();
                    scenario = post.getScenario();
                } else {
                    //Toast.makeText(MainActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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

    public void catchReturn(int position, boolean isChecked) throws Exception {

        //Sensor date(시간정보)
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String sensing_date = sdfNow.format(date);

        s_position = position;

        if(sensor.get(position).getType()==1){
            if(isChecked) {
                saveFile_enc(sensor.get(position).getName(), ACC_var0, ACC_var1, ACC_var2, sensing_date);
                saveDB_enc(sensor.get(position).getName(), ACC_var0, ACC_var1, ACC_var2, sensing_date);
                DBSearch_enc("SensorData", sensor.get(position).getName());
            }
            else{
                saveFile(sensor.get(position).getName(), ACC_var0, ACC_var1, ACC_var2, sensing_date);
                saveDB(sensor.get(position).getName(), ACC_var0, ACC_var1, ACC_var2, sensing_date);
                DBSearch("SensorData", sensor.get(position).getName());
            }
        }
        else if(sensor.get(position).getType()==4){
            if(isChecked) {
                saveFile_enc(sensor.get(position).getName(), GYRO_0, GYRO_1, GYRO_2, sensing_date);
                saveDB_enc(sensor.get(position).getName(), GYRO_0, GYRO_1, GYRO_2, sensing_date);
                DBSearch_enc("SensorData", sensor.get(position).getName());
            }
            else{
                saveFile(sensor.get(position).getName(), GYRO_0, GYRO_1, GYRO_2, sensing_date);
                saveDB(sensor.get(position).getName(), GYRO_0, GYRO_1, GYRO_2, sensing_date);
                DBSearch("SensorData", sensor.get(position).getName());
            }
        }
        else if(sensor.get(position).getType()==5){
            if(isChecked) {
                saveFile_enc(sensor.get(position).getName(), Light_0, sensing_date);
                saveDB_enc(sensor.get(position).getName(), Light_0, sensing_date);
                DBSearch_enc("SensorData", sensor.get(position).getName());
            }
            else{
                saveFile(sensor.get(position).getName(), Light_0, sensing_date);
                saveDB(sensor.get(position).getName(), Light_0, sensing_date);
                DBSearch("SensorData", sensor.get(position).getName());
            }
        }
        else if(sensor.get(position).getType()==9){
            if(isChecked) {
                saveFile_enc(sensor.get(position).getName(), Gv_0, Gv_1, Gv_2, sensing_date);
                saveDB_enc(sensor.get(position).getName(), Gv_0, Gv_1, Gv_2, sensing_date);
                DBSearch_enc("SensorData", sensor.get(position).getName());
            }
            else{
                saveFile(sensor.get(position).getName(), Gv_0, Gv_1, Gv_2, sensing_date);
                saveDB(sensor.get(position).getName(), Gv_0, Gv_1, Gv_2, sensing_date);
                DBSearch("SensorData", sensor.get(position).getName());
            }
        }
        else if(sensor.get(position).getType()==19){
            if(isChecked) {
                saveFile_enc(sensor.get(position).getName(), Step_0, sensing_date);
                saveDB_enc(sensor.get(position).getName(), Step_0, sensing_date);
                DBSearch_enc_1("SensorData", sensor.get(position).getName());
            }
            else{
                saveFile(sensor.get(position).getName(), Step_0, sensing_date);
                saveDB(sensor.get(position).getName(), Step_0, sensing_date);
                DBSearch_1("SensorData", sensor.get(position).getName());
            }
        }
        else{
            sensorReturn.setText(str_default);
        }
    } //센서 이름으로 파일 생성

    public void saveFile_enc(String filename, float a, float b, float c, String sensing_date) throws Exception { //센서 return값이 x,y,z일 때 파일 저장함수

        String str = "x = " + a + "\ny = "+b +"\nz = "+ c + "\n";

        a_enc2 = encrypt(Float.toString(a));
        b_enc2 = encrypt(Float.toString(b));
        c_enc2 = encrypt(Float.toString(c)); //sensing하는 순간 암호화...


        AddRecforSEED_3(sensor.get(s_position).getName(), a, b, c, a_enc2, b_enc2, c_enc2, sensing_date);

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

            //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void saveFile_enc(String filename, float x, String sensing_date) throws Exception{ //return값이 하나일 경우 파일에 저장하는 함수

        String x_str = Float.toString(x);

        //암호화
        String plainText = x_str; //평문

        String x_enc = encrypt(x_str).toString() + "\n";

        String enc = x_enc;

        a_enc2 = encrypt(x_str);

        AddRecforSEED_1(sensor.get(s_position).getName(), x, a_enc2, sensing_date); //암호화된 센서값과 그에 해당하는 센서 이름을 record로 추가
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

            //Toast.makeText(mediumActivity., "success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveFile(String filename, float a, float b, float c, String sensing_date) throws Exception { //센서 return값이 x,y,z일 때 파일 저장함수

        AddRec_3(sensor.get(s_position).getName(), a, b, c, sensing_date);

        //sensorReturn.setText(print_3(sensor.get(s_position).getName()));

        String save = "";

        for(int i = 0; i< list.size(); i++) {

            if(filename.equals(list.get(i).getColumnValbyname("SensorName"))) {
                save += "Sensor Name : " + list.get(i).getColumnValbyname("SensorName") + "\n"
                        + "x = " + list.get(i).getColumnValbyname("SensingData_x") + "\n" +
                        "y = " + list.get(i).getColumnValbyname("SensingData_y") + "\n" +
                        "z = " + list.get(i).getColumnValbyname("SensingData_z") + "\n"
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

            //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void saveFile(String filename, float x, String sensing_date) throws Exception{ //return값이 하나일 경우 파일에 저장하는 함수

        AddRec_1(sensor.get(s_position).getName(), x, sensing_date); //암호화된 센서값과 그에 해당하는 센서 이름을 record로 추가
        //sensorReturn.setText(print_1(sensor.get(s_position).getName()));

        String save = "";

        for(int i = 0; i< list.size(); i++) {

            if(filename.equals(list.get(i).getColumnValbyname("SensorName"))) {
                save += "Sensor Name : " + list.get(i).getColumnValbyname("SensorName") + "\n"
                        + "x = " + list.get(i).getColumnValbyname("SensingData_x") + "\n"
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

            //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveDB_enc(String filename, float a, float b, float c, String sensing_date){
        sqlDB = myHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("NAME", filename);
        contentValues.put("X", "");
        contentValues.put("Y", "");
        contentValues.put("Z", "");
        contentValues.put("time", sensing_date);
        contentValues.put("X_enc", a_enc2);
        contentValues.put("Y_enc", b_enc2);
        contentValues.put("Z_enc", c_enc2);

        long id = sqlDB.insert("SensorData", null, contentValues);

        sqlDB.close();

    }

    public void saveDB_enc(String filename, float x, String sensing_date){
        sqlDB = myHelper.getWritableDatabase();

        a_enc2 = encrypt(Float.toString(x));

        ContentValues contentValues = new ContentValues();

        contentValues.put("NAME", filename);
        contentValues.put("X", "");
        contentValues.put("Y", "");
        contentValues.put("Z", "");
        contentValues.put("time", sensing_date);
        contentValues.put("X_enc", a_enc2);
        contentValues.put("Y_enc", "");
        contentValues.put("Z_enc", "");

        long id = sqlDB.insert("SensorData", null, contentValues);

        sqlDB.close();
    }

    public void saveDB(String filename, float a, float b, float c, String sensing_date){
        sqlDB = myHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("NAME", filename);
        contentValues.put("X", a);
        contentValues.put("Y", b);
        contentValues.put("Z", c);
        contentValues.put("time", sensing_date);
        contentValues.put("X_enc", "");
        contentValues.put("Y_enc", "");
        contentValues.put("Z_enc", "");

        long id = sqlDB.insert("SensorData", null, contentValues);

        sqlDB.close();

    }

    public void saveDB(String filename, float x, String sensing_date){
        sqlDB = myHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("NAME", filename);
        contentValues.put("X", x);
        contentValues.put("Y", "");
        contentValues.put("Z", "");
        contentValues.put("time", sensing_date);
        contentValues.put("X_enc", "");
        contentValues.put("Y_enc", "");
        contentValues.put("Z_enc", "");

        long id = sqlDB.insert("SensorData", null, contentValues);

        sqlDB.close();
    }

    public void DBSearch_enc(String tablename, String sensorName){
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
                data += "x = " + x + "\n" + "y = " + y + "\n" + "z = " + z + "\n\n";
            }

        }
        sensorReturn.setText(data);

        cursor.close();
        sqlDB.close();

    }

    public void DBSearch_enc_1(String tablename, String sensorName){
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
        sensorReturn.setText(data);

        cursor.close();
        sqlDB.close();

    }

    public void DBSearch(String tablename, String sensorName){
        String data = "";

        sqlDB = myHelper.getWritableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM SensorData;", null);

        while (cursor.moveToNext()){

            String name = cursor.getString(cursor.getColumnIndex("Name"));
            Float x = cursor.getFloat(cursor.getColumnIndex("X"));

            Float y = cursor.getFloat(cursor.getColumnIndex("Y"));

            Float z = cursor.getFloat(cursor.getColumnIndex("Z"));


            if(name.equals(sensorName)) { //센서이름이 같은 경우만 출력
                data += "x = " + x + "\n" + "y = " + y + "\n" + "z = " + z + "\n\n";
            }

        }
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
            Float x = cursor.getFloat(cursor.getColumnIndex("X"));

            //String time = cursor.getString(cursor.getColumnIndex("time"));

            if(name.equals(sensorName)) { //센서이름이 같은 경우만 출력
                data += "x = " + x + "\n";
            }

        }
        sensorReturn.setText(data);

        cursor.close();
        sqlDB.close();

    }
}
