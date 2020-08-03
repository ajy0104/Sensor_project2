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
import android.provider.ContactsContract;
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
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;

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
import static com.example.myapplication.mediumActivity.bool;
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
    private View bodyView;
    private boolean array_switch[];

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public static byte [] a_enc2 = null;
    public static byte [] b_enc2 = null;
    public static byte [] c_enc2 = null;

    public String data2;

    public int num_acc=0;
    public int num_gra=0;
    public int num_gyro=0;
    public int num_light=0;
    public int num_step=0;


    public String sensor_func = "";
    public String scenario="";

    public static int s_position=0;

    public double per_acc=12.1;//firebase에 있는 percentage
    public double per_gra=23.4;//firebase에 있는 percentage
    public double per_gyro=15.9;//firebase에 있는 percentage
    public double per_light=18.7;//firebase에 있는 percentage
    public double per_step=10.3;//firebase에 있는 percentage

    private int survey_cnt=167; //기존 설문 응답자 수
    private String sensorType = "";

    private String get_desc = "";
    private double get_percentage;
    private String get_scenario="";

    ArrayList<Data> acc = new ArrayList<Data>();
    ArrayList<Data> gra = new ArrayList<Data>();
    ArrayList<Data> gyro = new ArrayList<Data>();
    ArrayList<Data> light = new ArrayList<Data>();
    ArrayList<Data> step = new ArrayList<Data>();


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


        bodyView = view.findViewById(R.id.body);
        switchView = view.findViewById(R.id.exSwitch);

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            //여기에 snapshot찍고 데이터 가져오면 되지 않을까?!
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {

                if(isChecked){
                    //check 되었을 때

                    int sensorType_num = sensor.get(position).getType(); //센서 타입 번호

                    if(sensorType_num==1){
                        // 영어로 설명 바꿈
                        sensorType = "TYPE_ACCELEROMETER";
                        databaseReference.child("UX_Sensor_List").child(sensorType).child("Percentage").setValue(per_acc);

                    }
                    else if(sensorType_num==4) {
                        sensorType = "TYPE_GYROSCOPE";
                        databaseReference.child("UX_Sensor_List").child(sensorType).child("Percentage").setValue(per_gyro);
                    }
                    else if(sensorType_num==5){
                        sensorType = "TYPE_LIGHT";
                        databaseReference.child("UX_Sensor_List").child(sensorType).child("Percentage").setValue(per_light);

                    }
                    else if(sensorType_num==9){
                        sensorType = "TYPE_GRAVITY";
                        databaseReference.child("UX_Sensor_List").child(sensorType).child("Percentage").setValue(per_gra);

                    }
                    else if(sensorType_num==19){
                        sensorType = "TYPE_STEP_COUNTER";
                        databaseReference.child("UX_Sensor_List").child(sensorType).child("Percentage").setValue(per_step);
                    }
                    else{
                        sensorType = "특수한 센서";
                        sensor_func = "";
                    }

                    databaseReference.child("UX_Sensor_List").child(sensorType).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            get_desc = dataSnapshot.child("Description").getValue().toString();
                            get_percentage = (double)(dataSnapshot.child("Percentage").getValue());
                            get_scenario = dataSnapshot.child("Scenario").getValue().toString();

                            AlertDialog.Builder builder = new AlertDialog.Builder(buttonView.getContext());
                            builder.setTitle("Sensor Encryption Settings").setIcon(R.drawable.shield);

                            builder.setMessage("Sensor_Type : " + sensorType + "\n" + "\nDescription : "+ get_desc +"\n"+ "\n\nPrivacy Risk : "+ get_scenario +
                                    "\n\nWould you like to set encryption for this sensor?\n\n" + "Statistics of encryption settings for the sensor : " + String.format("%.2f",get_percentage) + "%");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(sensorType.equals("TYPE_ACCELEROMETER")){
                                        // 영어로 설명 바꿈
                                        //기존의 퍼센트에 cnt곱하기 -> 카운트++ -> 카운트++한 값으로 나누기 -> 새로운 percentage.setvalue()
                                        //쌉가능
                                        survey_cnt++;
                                        per_acc = ((per_acc * (survey_cnt-1))/100+1)/(survey_cnt)*100;
                                    }
                                    else if(sensorType.equals("TYPE_GYROSCOPE")){
                                        survey_cnt++;
                                        per_gyro = ((per_acc * (survey_cnt-1))/100+1)/(survey_cnt)*100;

                                    }
                                    else if(sensorType.equals("TYPE_LIGHT")){
                                        survey_cnt++;
                                        per_light = ((per_acc * (survey_cnt-1))/100+1)/(survey_cnt)*100;

                                    }
                                    else if(sensorType.equals("TYPE_GRAVITY")){
                                        survey_cnt++;
                                        per_gra = ((per_acc * (survey_cnt-1))/100+1)/(survey_cnt)*100;

                                    }
                                    else if(sensorType.equals("TYPE_STEP_COUNTER")){
                                        survey_cnt++;
                                        per_step = ((per_acc * (survey_cnt-1))/100+1)/(survey_cnt)*100;

                                    }
                                    else{
                                        sensorType = "특수한 센서";
                                        sensor_func = "";
                                    }
                                    bool[position] = true;
                                    //퍼센트 계산해서 변수에 넣어주기
                                }
                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //스위치 off
                                    //buttonView.setChecked(!isChecked);
                                    buttonView.setChecked(false);
                                }
                            });
                            builder.show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    //check 되어있지 않을 때
                }
            }

        });

        bodyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //s_position=position;

                sensorName.setText(sensor.get(position).getName()); //센서 클릭했을 때 textview에 이름 출력



                try {
                    //catchReturn(position, switchView.isChecked());
                    catchReturn(position, bool[position]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }


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

        Data d = new Data();

        if(sensor.get(position).getType()==1){
            if(isChecked) {
                saveFile_enc(sensor.get(position).getName(), ACC_var0, ACC_var1, ACC_var2, sensing_date);
                d.setX(ACC_var0);
                d.setY(ACC_var1);
                d.setZ(ACC_var2);
                acc.add(d);
                saveDB_enc("TYPE_ACCELEROMETER", ACC_var0, ACC_var1, ACC_var2, sensing_date, num_acc);
            }
            else{
                saveFile(sensor.get(position).getName(), ACC_var0, ACC_var1, ACC_var2, sensing_date);
                saveDB("TYPE_ACCELEROMETER", ACC_var0, ACC_var1, ACC_var2, sensing_date, num_acc);
                DBSearch("SensorData", sensor.get(position).getName());
            }
            num_acc++;
        }
        else if(sensor.get(position).getType()==4){
            if(isChecked) {
                saveFile_enc(sensor.get(position).getName(), GYRO_0, GYRO_1, GYRO_2, sensing_date);
                d.setX(GYRO_0);
                d.setY(GYRO_1);
                d.setZ(GYRO_2);
                gyro.add(d);
                saveDB_enc("TYPE_GYROSCOPE", GYRO_0, GYRO_1, GYRO_2, sensing_date, num_gyro);
            }
            else{
                saveFile(sensor.get(position).getName(), GYRO_0, GYRO_1, GYRO_2, sensing_date);
                saveDB("TYPE_GYROSCOPE", GYRO_0, GYRO_1, GYRO_2, sensing_date, num_gyro);
                DBSearch("SensorData", sensor.get(position).getName());
            }
            num_gyro++;
        }
        else if(sensor.get(position).getType()==5){
            if(isChecked) {
                saveFile_enc(sensor.get(position).getName(), Light_0, sensing_date);
                d.setX(Light_0);
                acc.add(d);
                saveDB_enc("TYPE_LIGHT", Light_0, sensing_date, num_light);
            }
            else{
                saveFile(sensor.get(position).getName(), Light_0, sensing_date);
                saveDB("TYPE_LIGHT", Light_0, sensing_date, num_light);
                DBSearch("SensorData", sensor.get(position).getName());
            }
            num_light++;
        }
        else if(sensor.get(position).getType()==9){
            if(isChecked) {
                saveFile_enc(sensor.get(position).getName(), Gv_0, Gv_1, Gv_2, sensing_date);
                d.setX(Gv_0);
                d.setY(Gv_1);
                d.setZ(Gv_2);
                gra.add(d);
                saveDB_enc("TYPE_GRAVITY", Gv_0, Gv_1, Gv_2, sensing_date, num_gra);
            }
            else{
                saveFile(sensor.get(position).getName(), Gv_0, Gv_1, Gv_2, sensing_date);
                saveDB("TYPE_GRAVITY", Gv_0, Gv_1, Gv_2, sensing_date, num_gra);
                DBSearch("SensorData", sensor.get(position).getName());
            }
            num_gra++;
        }
        else if(sensor.get(position).getType()==19){
            if(isChecked) {
                saveFile_enc(sensor.get(position).getName(), Step_0, sensing_date);
                d.setX(Step_0);
                step.add(d);
                saveDB_enc("TYPE_STEP_COUNTER", Step_0, sensing_date, num_step);
            }
            else{
                saveFile(sensor.get(position).getName(), Step_0, sensing_date);
                saveDB("TYPE_STEP_COUNTER", Step_0, sensing_date, num_step);
                DBSearch_1("SensorData", sensor.get(position).getName());
            }
            num_step++;
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

    public void saveDB_enc(String filename, float a, float b, float c, String sensing_date, int num){

        data2 = "";

        String s = filename;

        if(s.equals("TYPE_ACCELEROMETER")){
            for(int i = 0; i<acc.size(); i++){
                data2 += "x : " + acc.get(i).getX() + " y : " + acc.get(i).getY()+" z : " +acc.get(i).getZ() + " time : "+sensing_date+"\n";
            }
        }
        if(s.equals("TYPE_GYROSCOPE")){
            for(int i = 0; i<gyro.size(); i++){
                data2 += "x : " + gyro.get(i).getX() + " y : " + gyro.get(i).getY()+" z : " +gyro.get(i).getZ() + " time : "+sensing_date+"\n";
            }
        }

        if(s.equals("TYPE_GRAVITY")){
            for(int i = 0; i<gra.size(); i++){
                data2 += "x : " + gra.get(i).getX() + " y : " + gra.get(i).getY()+" z : " +gra.get(i).getZ() + " time : "+sensing_date+"\n";
            }
        }

        sensorReturn.setText(data2);


        databaseReference.child("UX_Sensor_Data").child(filename).child(String.valueOf(num)).child("X").setValue(a_enc2.toString());
        databaseReference.child("UX_Sensor_Data").child(filename).child(String.valueOf(num)).child("Y").setValue(b_enc2.toString());
        databaseReference.child("UX_Sensor_Data").child(filename).child(String.valueOf(num)).child("Z").setValue(c_enc2.toString());
        databaseReference.child("UX_Sensor_Data").child(filename).child(String.valueOf(num)).child("time").setValue(sensing_date);



        databaseReference.child("UX_Sensor_Data").child(filename).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void saveDB_enc(String filename, float x, String sensing_date, int num){

        data2 = "";
        String s = filename;

        if(s.equals("TYPE_LIGHT")){
            for(int i = 0; i<light.size(); i++){
                data2 += "x : " + light.get(i).getX() + " time : "+sensing_date+"\n";
            }
        }

        if(s.equals("TYPE_STEP_COUNTER")){
            for(int i = 0; i<step.size(); i++){
                data2 += "x : " + step.get(i).getX() + " time : "+sensing_date+"\n";
            }
        }

        sensorReturn.setText(data2);

        databaseReference.child("UX_Sensor_Data").child(filename).child(String.valueOf(num)).child("X").setValue(a_enc2.toString());
        databaseReference.child("UX_Sensor_Data").child(filename).child(String.valueOf(num)).child("time").setValue(sensing_date);

    }

    public void saveDB(String filename, float a, float b, float c, String sensing_date, int num){

        data2 = "";

        //int i = 0;
        databaseReference.child("UX_Sensor_Data").child(filename).child(String.valueOf(num)).child("X").setValue(a);
        databaseReference.child("UX_Sensor_Data").child(filename).child(String.valueOf(num)).child("Y").setValue(b);
        databaseReference.child("UX_Sensor_Data").child(filename).child(String.valueOf(num)).child("Z").setValue(c);
        databaseReference.child("UX_Sensor_Data").child(filename).child(String.valueOf(num)).child("time").setValue(sensing_date);
        //i++;

        //int j = 0;
        databaseReference.child("UX_Sensor_Data").child(filename).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    data2 = dataSnapshot1.getValue().toString();

                }*/
                data2 = dataSnapshot.getValue().toString();
                sensorReturn.setText(data2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void saveDB(String filename, float x, String sensing_date, int num){

        data2 = "";

        //int i = 0;
        databaseReference.child("UX_Sensor_Data").child(filename).child(String.valueOf(num)).child("X").setValue(x);
        databaseReference.child("UX_Sensor_Data").child(filename).child(String.valueOf(num)).child("time").setValue(sensing_date);

        databaseReference.child("UX_Sensor_Data").child(filename).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    data2 = dataSnapshot1.getValue().toString();

                }*/
                data2 = dataSnapshot.getValue().toString();
                sensorReturn.setText(data2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
