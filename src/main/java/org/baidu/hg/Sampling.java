package org.baidu.hg;

import com.baidu.hugegraph.driver.GremlinManager;
import com.baidu.hugegraph.driver.HugeClient;
import com.baidu.hugegraph.structure.gremlin.Result;
import com.baidu.hugegraph.structure.gremlin.ResultSet;


import java.io.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Sampling {
    final int THREAD_COUNT = 50;
    HugeClient hugeClient = null;
    BufferedWriter[] writers = new BufferedWriter[7];
    AtomicInteger process = new AtomicInteger(0);
    public void init(String url, String graph, String filePath) throws IOException {
        hugeClient = HugeClient.builder(url, graph).configTimeout(3600).build();

        writers[0] = new BufferedWriter(new FileWriter(filePath + "/10.txt", true));
        writers[1] = new BufferedWriter(new FileWriter(filePath + "/100.txt", true));
        writers[2] = new BufferedWriter(new FileWriter(filePath + "/500.txt", true));
        writers[3] = new BufferedWriter(new FileWriter(filePath + "/1000.txt", true));
        writers[4] = new BufferedWriter(new FileWriter(filePath + "/2000.txt", true));
        writers[5] = new BufferedWriter(new FileWriter(filePath + "/5000.txt", true));
        writers[6] = new BufferedWriter(new FileWriter(filePath + "/10000.txt", true));
    }

    private Iterator<Result> execGremlin(String cmd) {
        GremlinManager gremlin = hugeClient.gremlin();
        ResultSet resultSet = gremlin.gremlin(cmd).execute();
        return resultSet.iterator();
    }

    // 记录到文件
    private void record(String id, int degree) throws IOException {
        int index = 0;
        if (degree < 50){
            index = 0;
        }else if ( degree < 200 ){
            index = 1;
        }else if (degree < 600 ){
            index = 2;
        }else if ( degree > 800 && degree < 1200 ){
            index = 3;
        }else if ( degree > 2000 && degree < 3000 ){
            index = 4;
        }else if ( degree > 5000 && degree < 8000 ){
            index = 5;
        }else if ( degree > 10000){
            index = 6;
        }
        synchronized (writers[index]){
            writers[index].write(id);
            writers[index].newLine();
        }

        int n = process.incrementAndGet();
        if ( n % 1000 == 0)
            System.out.println(n);
    }

    public void samples(int range, int count) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        for(int i = 0; i < count / 1000; i++){
            final int start = range / (count/1000) * i;
            executor.execute(()-> {
                try {
                    sample(start, 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(32, TimeUnit.DAYS);
    }
    public void sample(int start, int count) throws IOException {
        // 拉取点列表
        String cmd = String.format("g.V().range(%d,%d).id()", start, start + count);
        GremlinManager gremlin = hugeClient.gremlin();
        ResultSet resultSet = gremlin.gremlin(cmd).execute();
        Iterator<Result> results = resultSet.iterator();
        while (results.hasNext()) {
            String id = (String) results.next().getObject();
            // 拉取点的degree
            cmd = String.format("g.V(\"%s\").outE().count()", id);
            ResultSet resultSet2 = gremlin.gremlin(cmd).execute();
            int degree =  Integer.parseInt(resultSet2.iterator().next().getObject().toString());
            record(id, degree);
        }

    }

    public void sample2(String id) throws IOException {
        // 拉取点列表
        GremlinManager gremlin = hugeClient.gremlin();
        String cmd = String.format("g.V(\"%s\").outE().count()", id);
        ResultSet resultSet = gremlin.gremlin(cmd).execute();
        int degree = Integer.parseInt(resultSet.iterator().next().getObject().toString());
        record(id, degree);
    }

    public void samples2(String vertexFile) throws InterruptedException, IOException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        try(BufferedReader reader = new BufferedReader(new FileReader(vertexFile))){
            String id = "";
            while( (id = reader.readLine()) != null){
                if ( !id.isEmpty()){
                    final String finalId = id;
                    executor.execute(()-> {
                        try {
                            sample2(finalId);
                        } catch (Throwable e) {
                            System.out.println(finalId);
                            e.printStackTrace();
                        }
                    });
                }
            }
        }

        executor.shutdown();
        executor.awaitTermination(32, TimeUnit.DAYS);
    }

    public void close() throws IOException {
        if ( hugeClient != null )
            hugeClient.close();
        for(BufferedWriter writer : writers)
            if ( writer != null )
                writer.close();
    }

    public static void main1(String[] args) throws IOException, InterruptedException {

        if ( args.length < 4){
            System.out.println("参数错误，正确格式： url 图名称 存储路径 点个数");
            return;
        }
        String url = args[0];
        String graph = args[1];
        String path = args[2];
        int total = 800000;//Integer.parseInt(args[3]);
        total = 800000;
        int count = Integer.parseInt(args[3]);
        System.out.println("开始采样");
        Sampling sampling = new Sampling();
        try {
            sampling.init(url, graph, path);
            sampling.samples(total, count);
        } finally {
            sampling.close();
        }
        System.out.println("采样完成");
    }

    public static void main2(String[] args) throws IOException, InterruptedException{

        if ( args.length < 4){
            System.out.println("参数错误，正确格式： url 图名称 存储路径 点列表文件");
            return;
        }

        String url = args[0];
        String graph = args[1];
        String path = args[2];
        String vertexFile = args[3];

        System.out.println("开始采样");
        Sampling sampling = new Sampling();
        try {
            sampling.init(url, graph, path);
            sampling.samples2(vertexFile);
        }finally {
            sampling.close();
        }
        System.out.println("采样完成");
    }
    public static void main(String[] args) throws IOException, InterruptedException{
        main2(args);
    }
}
