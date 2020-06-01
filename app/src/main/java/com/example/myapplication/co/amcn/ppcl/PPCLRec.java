package com.example.myapplication.co.amcn.ppcl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class PPCLRec {//encryption should be done selectively, each column need to have a buffer for encrypted value

    public Map<Column<?>, Object> row = new HashMap<>();
    public Map<String, Column<?>> row2 = new HashMap<>();

    
    
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

	static byte[] pub_byte;
	 
    private int NumColumn = 0;   
    private String RecInfo;

      
    public PPCLRec(int numColumn, String recInfo) {
		NumColumn = numColumn;
		RecInfo = recInfo;
	}

    public PPCLRec() {
	}
    
    
	public <T> void putColumn(Column<T> type, T instance, String name) {
		System.out.println("HERE IS THE TYPE" + type);
        row.put(type, type.cast(instance));
        row2.put(name, type);
        NumColumn++;
    }
    
	public <T> T getColumnValbyname(String name) {
		Column<T> type = (Column<T>) row2.get(name);
		if (row == null)
			return null;
		else
		    return type.cast(row.get(type));
    }

	
	public <T> T getColumnVal(Column<T> type) {
        return type.cast(row.get(type));
    }
    
    public <T> void SetencColumn(Column<T> type, byte [][] encVal) {
//    	String InputString = row.get(type).toString();
    	
//    	System.out.println("The encryption target: " + InputString);
    	
    	try {
			type.SetEncVal(encVal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    public <T> byte [][] GetencColumn(Column<T> type) {
//    	String InputString = row.get(type).toString();
    	
//    	System.out.println("The encryption target: " + InputString);
    	
    	byte [][] temp=null;
    	
    	try {
			temp = type.GetEncVal(row.get(type));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return temp;
    	
    }

    public <T> void SetencColumnforSEED(Column<T> type, byte [] encValforSEED) { //암호화된 값으로 set
//    	String InputString = row.get(type).toString();

//    	System.out.println("The encryption target: " + InputString);

        try {
            type.SetEncValforSEED(encValforSEED);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*public <T> byte [] GetencColumnforSEED(Column<T> type) { //column에 암호화된 상태로 저장되어있는 값 get--->복호화에 이용
//    	String InputString = row.get(type).toString();

//    	System.out.println("The encryption target: " + InputString);

        byte [] temp=null;

        try {
            temp = type.GetEncValforSEED(row.get(type));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return temp;

    }*/
    public <T> byte [] GetencColumnforSEEDbyname(String name) { //column에 암호화된 상태로 저장되어있는 값 get--->복호화에 이용
//    	String InputString = row.get(type).toString();

//    	System.out.println("The encryption target: " + InputString);
        Column<T> type = (Column<T>) row2.get(name);

        byte [] temp=null;

        try {
            temp = type.GetEncValforSEED(row.get(type));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return temp;

    }

      
    /*public <T> void encColumn(Column<T> type, String Policy) {
    	String InputString = row.get(type).toString();
    	
    	System.out.println("The encryption target: " + InputString);
    	
    	try {
			type.SetEncVal(test.enc4(pub_byte, Policy, InputString));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

	public <T> void encColumn(Column<T> type, String Policy, byte [] pub_byte2) {
		String InputString = row.get(type).toString();

		System.out.println("The encryption target: " + InputString);

		try {
			type.SetEncVal(test.enc4(pub_byte2, Policy, InputString));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public <T> void decColumn(Column<T> type, String Policy) {
    	String InputString = row.get(type).toString();
    	
    	System.out.println("The encryption target: " + InputString);
    	
    	try {
			type.SetEncVal(test.enc4(pub_byte, Policy, InputString));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }*/
    

    public <T> byte [][] getColumnEnc(Column<T> type) { // <T> means the data type of "type", byte [][] is the return type
        return type.GetEncVal(row.get(type));
    }

	public <T> byte []getColumnEncforSEED(Column<T> type) { // <T> means the data type of "type", byte [] is the return type
		return type.GetEncValforSEED(row.get(type));
	}
    
    
    @Override
 	public String toString() {
 		return "["+ row + "]";
 	}

 	public void RecordComposer(Integer Num, String strColInfo) {//it should be updated for supporting general composer
 		
        //Sequences of required data, Number of Column, Data Types for the columns
/* 		Map<String, Column> ColumnMap = new HashMap<String, Column>();
 		
 		Column<Integer> Object = new Column<>(Integer.class);
 		ColumnMap.put("SensorID", Object);

 		Column myObj = ColumnMap.get("SensorID");
 */		 		

 		//1. Create with the Num of Column with Array? is it possible? It is not easy because it is heterogeneous
 		//2. 
 		
// 		Column<Integer> SensorID = new Column<>(Integer.class);
//        Column<String> SensorName = new Column<>(String.class);
 //       Column<Double> SensingData = new Column<>(Double.class);
//        Column<String> Description = new Column<>(String.class);
        
        //setting values for each column, List of data for columns
        //How to set the values? 
        
        
 	}

 	
 	
/*    
    
    public static void main(String[] args) {

 		String student_attr = "objectClass:inetOrgPerson objectClass:organizationalPerson "
 				+ "sn:student2 cn:student2 uid:student2 userPassword:student2 "
 				+ "ou:idp o:computer mail:student2@sdu.edu.cn title:student";

 		String student_policy = "sn:student2 cn:student2 uid:student2 3of3";

 		
 		String dir = "./sensors";

 	    String pubfile = dir + "/pub_key";
 		String mskfile = dir + "/master_key";
 		String prvfile = dir + "/prv_key";

 		String inputfile = dir + "/input.txt";
 		String encfile = dir + "/input.pdf.cpabe";
 		String decfile = dir + "/input.pdf.new";

 		String[] attr = { "baf1", "fim1", "foo" };
 		String policy = "foo bar fim 2of3 baf 1of2";
		
 		
 		
 		try {
			pub_byte = Common.suckFile(pubfile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

                               
         
        String str = db.toString();
        
        System.out.println(str);

        String attr_str;
 		//attr = attr_kevin;
 		//attr = attr_sara;
 		//policy = policy_kevin_or_sara;
 		//attr_str = array2Str(attr);

 		attr_str = student_attr;
 		policy = student_policy;

 		
 		System.out.println("Working Directory = " +
 	              System.getProperty("user.dir"));		
 		
 		System.out.println(attr_str);
 		
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

 		System.out.println("//start to enc");
 		try {
 			test.enc2(pubfile, policy, str, encfile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		System.out.println("//end to enc");

 		System.out.println("//start to dec");

 		try {
			test.dec(pubfile, prvfile, encfile, decfile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		System.out.println("//end to dec");
 		
 		db.encColumn(SensingData, policy);
 		
        db.putColumn(SensorID, 1);
        db.putColumn(SensorName, "HeartBeat");        
        db.putColumn(SensingData, 78.99);
        db.putColumn(Description, "Normal Status");
	
 		
 				    
        System.out.println(SensorID.getClass() + " " + SensingData.getClass() +" " + Description.getClass());
        
        
        System.out.println(db.getColumnVal(SensorID) + " " + db.getColumnVal(SensorName)+ " " + db.getColumnVal(SensingData)+ " " + db.getColumnVal(Description));
        
        System.out.println(db.getColumnEnc(SensorID) + " " + db.getColumnEnc(SensorName)+ " " + db.getColumnEnc(SensingData)+ " " + db.getColumnEnc(Description));
    }
*/

}
