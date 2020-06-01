package com.example.myapplication.co.amcn.ppcl.ppclSeed;

import android.util.Base64;

import com.example.myapplication.KISA_SEED_CBC;
import com.example.myapplication.co.amcn.ppcl.Column;
import com.example.myapplication.co.amcn.ppcl.PPCLRec;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.highActivity.bszIV;
import static com.example.myapplication.highActivity.charset;
import static com.example.myapplication.highActivity.pbUserKey;
import static com.example.myapplication.co.amcn.ppcl.PPCLRecSet.list;

public class PPCLSeed<SensorName> {

    //암호화(seed암호 사용)
    public static byte[] encrypt(String str){
        byte[] enc = null;

        try {
            //암호화 함수 호출
            enc = KISA_SEED_CBC.SEED_CBC_Encrypt(pbUserKey, bszIV, str.getBytes(charset), 0,
                    str.getBytes(charset).length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] encArray = Base64.encode(enc,0);

        try {
            System.out.println(new String(encArray, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encArray;
    }

    //복호화(seed암호화 복호)
    public static String decrypt(byte[] str) {

        byte[] enc = Base64.decode(str,0);

        String result = "";
        byte[] dec = null;

        try {
            //복호화 함수 호출
            dec = KISA_SEED_CBC.SEED_CBC_Decrypt(pbUserKey, bszIV, enc, 0, enc.length);
            result = new String(dec, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }
    //화면에 list를 통해서 출력하도록 코드 추가...

}
