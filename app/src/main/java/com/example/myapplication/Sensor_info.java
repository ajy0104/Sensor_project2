package com.example.myapplication;

public class Sensor_info {

    public String Description;
    public String Scenario;
    public int Percentage;
    public int ref;

    public Sensor_info(){

    }

    public Sensor_info(String Description, String Scenario, int Percentage, int ref){
        this.Description=Description;
        this.Scenario=Scenario;
        this.Percentage=Percentage;
        this.ref=ref;
    }

    public String getDescription(){
        return Description;
    }

    public String getScenario(){
        return Scenario;
    }

    public int getPercentage(){
        return Percentage;
    }

    public void setPercentage(String percentage){
        this.Percentage=Percentage;
        //todo 숫자로 변환 코드 수정
    }

    public void setRef(int ref){
        this.ref=ref;
    }

    public int getRef(){
        return ref;
    }
}
