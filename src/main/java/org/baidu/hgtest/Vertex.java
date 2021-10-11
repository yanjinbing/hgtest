package org.baidu.hgtest;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Vertex {
    public String id;
    public String Zoneno;          //  随机五位数
    public String ACCOUNT_BELONG;   // 等于zoneno
    public String Party_Name;       // 人名 + 7位随机数
    public String Int_Org_Num;      // zoneno + 4位随机数
    public String Org_Num_Level;    // 定制5
    public String Int_Org_Num_Nm;   // 某某支行 + 4位随机数
    public String Int_Org_Num_Probranch;    //随机	0120100001
    public String Int_Org_Num_Branch;   // 随机	0120302580
    public String Int_Org_Num_Subbranch;   // 随机	0120391111
    public String Brno;             //Zoneno+后五位随机	0250500656
    public String PC_Flag;          //0
    public String Icbc_Flag;        //1
    public double REGTYPE;          // 0
    public String REGNO;            //随机	588018198902169578
    public String ACCOUNT;          //随机	25055000053011732287
    public String MOBILE;           //手机号
    public String WORKPLACE;        //工作单位

    static Random random = new Random(System.currentTimeMillis());
    static AtomicLong _id = new AtomicLong(0);
    public static  Vertex makeOne(){
        Vertex v = new Vertex();
        v.id = String.format("%010d", _id.getAndIncrement());
        v.Zoneno = String.format("%05d",random.nextInt(9999));
        v.ACCOUNT_BELONG = v.Zoneno;
        v.Party_Name = String.format("人名%06d", random.nextInt(999999));
        v.Int_Org_Num = String.format("%s%04d", v.Zoneno, random.nextInt(9999));
        v.Org_Num_Level = "5";
        v.Int_Org_Num_Nm = String.format("支行%05d", random.nextInt(9999));
        v.Int_Org_Num_Probranch = String.format("%06d", random.nextInt(9999));
        v.Int_Org_Num_Branch = String.format("%06d", random.nextInt(9999));
        v.Int_Org_Num_Subbranch = String.format("%06d", random.nextInt(9999));
        v.Brno = String.format("%s%05d", v.Brno, random.nextInt(9999));
        v.PC_Flag = "1";
        v.Icbc_Flag = "1";
        v.REGTYPE = 0;
        v.REGNO = String.format("%08d%08d", random.nextInt(99999999), random.nextInt(99999999));
        v.ACCOUNT = v.REGNO;
        v.MOBILE = String.format("138%08d", random.nextInt(99999999));
        v.WORKPLACE = String.format("工作单位%05d", random.nextInt(9999));
        return v;
    }

    public static String printHead(){
        return  "id,Zoneno,ACCOUNT_BELONG,Party_Name,Int_Org_Num,Org_Num_Level,Int_Org_Num_Nm,Int_Org_Num_Probranch," +
                "Int_Org_Num_Branch,Int_Org_Num_Subbranch,Brno,PC_Flag,Icbc_Flag,REGTYPE,REGNO,ACCOUNT,MOBILE,WORKPLACE";        //工作单位
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.id).append(",");
        sb.append(this.Zoneno).append(",");
        sb.append(this.ACCOUNT_BELONG).append(",");
        sb.append(this.Party_Name).append(",");
        sb.append(this.Int_Org_Num).append(",");
        sb.append(this.Org_Num_Level).append(",");
        sb.append(this.Int_Org_Num_Nm).append(",");
        sb.append(this.Int_Org_Num_Probranch).append(",");
        sb.append(this.Int_Org_Num_Branch).append(",");
        sb.append(this.Int_Org_Num_Subbranch).append(",");
        sb.append(this.Brno).append(",");
        sb.append(this.PC_Flag).append(",");
        sb.append(this.Icbc_Flag).append(",");
        sb.append(this.REGTYPE).append(",");
        sb.append(this.REGNO).append(",");
        sb.append(this.ACCOUNT).append(",");
        sb.append(this.MOBILE).append(",");
        sb.append(this.WORKPLACE);
        return sb.toString();
    }

}
