package com.example.myapplication.co.amcn.ppcl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

//import co.junwei.cpabe.Common;


public class PPCLRecSet {
	
	public static ArrayList<PPCLRec> list = new ArrayList<PPCLRec>();
	
	//static PPCLABE test;

	/*static {
		try {
			test = PPCLABE.get();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}*/

	static Column<Integer> SensorID = null;
	static Column<String> SensorName = null;
	static Column<Double> SensingData = null;
	static Column<String> Description = null;

	static byte [] pub_byte;
	static byte [] prv_byte;
	
	PPCLRecSet(){
		
	}

	public void SetRecData() {
			
	}
	
	public static void AddRec(Integer id, String name, Double data, String desc, String policy) throws IOException {

		PPCLRec rec = new PPCLRec();
		
 		SensorID = new Column<>(Integer.class, "SensorID"); // the key is instance id.
        SensorName = new Column<>(String.class, "SensorName");
        SensingData = new Column<>(Double.class, "SensingData");
        Description = new Column<>(String.class, "Description");
		
		rec.putColumn(SensorID, id, "SensorID");
		rec.putColumn(SensorName, name, "SensorName");        
		rec.putColumn(SensingData, data, "SensingData");
		rec.putColumn(Description, desc, "Description");
		
		
		list.add(rec);

		//encColumn(rec ,policy);
		
		//decColumn(rec); // when we decrypt

        System.out.println(rec.getColumnValbyname("SensorID") + " " + rec.getColumnValbyname("SensorName")+ " " + rec.getColumnValbyname("SensingData")+ " " + rec.getColumnValbyname("Description"));

        printRecords();

	}
	
    /*public static <T> void encColumn(PPCLRec rec, String Policy) {
    	String InputString = rec.row.get(SensingData).toString();

    	System.out.println("The encryption target: " + InputString);
    	
    	try {
    		rec.SetencColumn(SensingData,test.enc4(pub_byte, Policy, InputString)); // When we encrypt, the policy and public key are needed
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    public static <T> void decColumn(PPCLRec rec) throws IOException {
    	
    	byte[] temp = null;

    	try {
    		temp = test.dec3(pub_byte, prv_byte, rec.GetencColumn(SensingData)); // When we decrypt, the public and private keys are needed
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

		String str = new String(temp);

    	System.out.println("The decrypted target: " + str);
    }*/


    //    public static <T> void printRecords(Column<Integer> sensorID, Column<String> sensorName, Column<Double> sensingData, Column<String> description) {
    	
    public static <T> void printRecords() {
    	
    	int i = 0;
    	
        while(list.size()>i) {

        	System.out.println(list.get(i).row.keySet());
        	System.out.println(list.get(i).row.values());
        	System.out.println(list.get(i).row.entrySet());
        	
        	//System.out.println(list.get(i).row.);
        	
//        	System.out.println(list.get(i).getColumnVal(sensorID) + " " + list.get(i).getColumnVal(sensorName)+ " " + list.get(i).getColumnVal(sensingData)+ " " + list.get(i).getColumnVal(description));  
//        	System.out.println(list.get(i).getColumnEnc(sensorID) + " " + list.get(i).getColumnEnc(sensorName)+ " " + list.get(i).getColumnEnc(sensingData)+ " " + list.get(i).getColumnEnc(description));
        
        	i++;
        }
    }
    

    /*public static void main(String[] args) throws IOException {


 		String student_attr = "objectClass:inetOrgPerson objectClass:organizationalPerson "
 				+ "sn:student2 cn:student2 uid:student2 userPassword:student2 "
 				+ "ou:idp o:computer mail:student2@sdu.edu.cn title:student";

 		String student_policy = "sn:student2 cn:student2 uid:student2 3of3";
 		String[] attr = { "baf1", "fim1", "foo" };

		String attr_str = student_attr;
 		String policy = student_policy;
    	
    	
    	String dir = "./sensors";

 	    String pubfile = dir + "/pub_key";
 		String mskfile = dir + "/master_key";
 		String prvfile = dir + "/prv_key";

 		

                                		
 		System.out.println("Working Directory = " +
 	              System.getProperty("user.dir"));

 		System.out.println("//start to setup");
 		try {
			test.setup(pubfile, mskfile);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		System.out.println("//end to setup");

 		System.out.println("//start to keygen");
 		try {
			test.keygen(pubfile, prvfile, mskfile, attr_str);
		} catch (NoSuchAlgorithmException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		System.out.println("//end to keygen");

 		
 		try {
			pub_byte = Common.suckFile(pubfile);
			prv_byte = Common.suckFile(prvfile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 		
 	    AddRec(1, "HeartBeat", 83.23, "Normal", policy);
 	    AddRec(1, "HeartBeat", 90.2, "Normal", policy);
 	    AddRec(1, "HeartBeat", 81.33, "Normal", policy);
 		
 				    
    }*/
}
