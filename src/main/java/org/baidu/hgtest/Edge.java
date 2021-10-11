package org.baidu.hgtest;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Edge {
    public String from;
    public String to;
    public String EDGE_ID;
    public String Flow_Type;
    public double tx_amt;
    public double tx_cnt;
    public double In_Rank_Id_Amt;
    public double Out_Rank_Id_Amt;
    public String s_flag;
    public String date_dt;
    public String date;
    static AtomicLong _id = new AtomicLong(0);
    static Random random = new Random(System.currentTimeMillis());
    public static Edge makeOne(int vNum){
        Edge e = new Edge();
        int from = random.nextInt(vNum);
        int to = random.nextInt(vNum);
        while( from == to )
            to = random.nextInt(vNum);
        e.from = String.format("%010d", from);
        e.to = String.format("%010d",  to);
        e.EDGE_ID = e.from + "_" + String.valueOf(_id.getAndIncrement()) + "_" + e.to;
        e.Flow_Type = "0";
        e.tx_amt = random.nextDouble();
        e.tx_cnt = e.tx_amt;
        e.In_Rank_Id_Amt = e.tx_amt;
        e.Out_Rank_Id_Amt = e.tx_amt;
        e.s_flag = "0";
        e.date = String.format("20%02d%02d", random.nextInt(20), random.nextInt(12));
        e.date_dt = String.format("%s%02d", e.date, random.nextInt(30));
        return e;
    }
    public static String printHead(){
        return  "from,to,EDGE_ID,Flow_Type,tx_amt,tx_cnt,In_Rank_Id_Amt,Out_Rank_Id_Amt,s_flag,date,date_dt";
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.from).append(",");
        sb.append(this.to).append(",");
        sb.append(this.EDGE_ID).append(",");
        sb.append(this.Flow_Type).append(",");
        sb.append(this.tx_amt).append(",");
        sb.append(this.tx_cnt).append(",");
        sb.append(this.In_Rank_Id_Amt).append(",");
        sb.append(this.Out_Rank_Id_Amt).append(",");
        sb.append(this.s_flag).append(",");
        sb.append(this.date).append(",");
        sb.append(this.date_dt);
        return sb.toString();
    }

}
