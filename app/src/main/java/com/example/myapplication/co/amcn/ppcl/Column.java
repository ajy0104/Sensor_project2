package com.example.myapplication.co.amcn.ppcl;


public class  Column<T> {

    private final Class<T> valClass;
    private String name;
    private byte [][] encVal;
    private byte [] encValforSEED;
    private int enc_flag = 0;
    
    public Column(Class<T> valClass, String name) {
        this.valClass = valClass;
        this.name = name;
    }
    
    public void SetEncVal(byte [][] tmp) {
    	this.encVal = tmp;    	
    }

    public void SetEncValforSEED(byte [] tmp) {
        this.encValforSEED = tmp;
    }

    public byte [] GetEncValforSEED(Object obj) {
        return obj == null ? null : this.encValforSEED;
    }

    public byte [][] GetEncVal(Object obj) {
    	return obj == null ? null : this.encVal;    	
    }
   
    public T cast(Object obj) {
        return obj == null ? null : valClass.cast(obj);
    }
 

}