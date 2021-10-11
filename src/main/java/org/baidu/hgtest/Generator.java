package org.baidu.hgtest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 参考工商银行的数据结构，模拟产生测试数据
 */
public class Generator {
    final int THREAD_COUNT = 20;

    public void makeVertex(int vNum, String filePath) throws IOException {
        new File(filePath).getParentFile().mkdirs();
        BufferedWriter vbw = new BufferedWriter(new FileWriter(filePath));
        vbw.write(Vertex.printHead());
        for(int i = 0; i < vNum; i++){
            vbw.write('\n');
            vbw.write(Vertex.makeOne().toString());
        }
        vbw.close();
        System.out.println(filePath);
    }

    public void makeEdge(int vNum, long eNum, String filePath) throws IOException {
        new File(filePath).getParentFile().mkdirs();
        BufferedWriter ebw = new BufferedWriter(new FileWriter(filePath));
        ebw.write(Edge.printHead());
        for(long l = 0; l < eNum; l++){
            ebw.write('\n');
            ebw.write(Edge.makeOne(vNum).toString());
        }
        ebw.close();
        System.out.println(filePath);
    }

    public void makeVertex(int vNum, int fNum, String path) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        for(int f = 0; f < fNum; f++){
            final int fno = f;
            executor.execute(()->{
                try {
                    makeVertex(vNum/fNum, String.format("%s/v/%03d.txt", path, fno));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(32, TimeUnit.DAYS);
    }

    public void makeEdge(int vNum, long eNum, int fNum, String path) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        for(int f = 0; f < fNum; f++){
            final int fno = f;
            executor.execute(()->{
                try {
                    makeEdge(vNum, eNum / fNum, String.format("%s/e/%03d.txt", path, fno));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(32, TimeUnit.DAYS);
    }
    /**
     * 存储路径 点数 边数 文件数
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        if ( args.length < 3) {
            System.out.println("存储目录 点数量 边数量 文件数");
            return;
        }
        int vNum = 500000000;
        long eNum = 10000000000L;
        String path = "/home/test-data/";
        int fNum = 200;
        if (args.length > 0)
            path = args[0];
        if (args.length > 2) {
            vNum = Integer.parseInt(args[1]);
            eNum = Long.parseLong(args[2]);
        }
        if (args.length > 3)
            fNum = Integer.parseInt(args[3]);

        System.out.println(String.format("Make example data, %s, vNum=%d, eNum=%d, fNum=%d", path, vNum, eNum, fNum));
        Generator generator = new Generator();
        generator.makeVertex(vNum, fNum, path);
        generator.makeEdge(vNum, eNum, fNum, path);

        System.out.println("OK");
    }
}
