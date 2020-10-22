package com.example.mypc.uco;

public class EmployeeDetailsObj {

    private String memberNo;
    private String memberName;
    private String branch_name;
    private String mobile_no;
    private String email_id;

    public EmployeeDetailsObj(String no,String name,String branch,String mobile,String email){
        this.memberNo=no;
        this.memberName=name;
        this.branch_name=branch;
        this.mobile_no=mobile;
        this.email_id=email;
    }

    public String getBranch_name(){
        return branch_name;
    }

    public String getMobile_no(){
        return mobile_no;
    }

    public String getEmail_id(){
        return email_id;
    }

    public String getmemberNo(){
        return memberNo;
    }

    public String getmemberName(){
        return memberName;
    }
}