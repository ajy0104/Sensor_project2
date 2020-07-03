package com.example.myapplication;

public class Sensor_info {

    public String Description;
    public String Scenario;
    public double Percentage;

    public Sensor_info() {

    }

    public Sensor_info(String Description, String Scenario, double Percentage) {
        this.Description = Description;
        this.Scenario = Scenario;
        this.Percentage = Percentage;
    }

    public String getDescription() {
        return Description;
    }

    public String getScenario() {
        return Scenario;
    }

    public double getPercentage() {
        return Percentage;
    }

    public void setPercentage(double percentage) {
        this.Percentage = Percentage;
    }
}
