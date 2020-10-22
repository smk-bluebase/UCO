package com.example.mypc.uco;

public class CommonUtils {
//    static String IP = "https://www.ucosocietychennai.in";
static String IP = "http://192.168.201.165";

    public String StringToRound(String str_value){
        double new_value;
        new_value=Double.parseDouble(str_value);
        return Long.toString(Math.round(new_value));
    }
}