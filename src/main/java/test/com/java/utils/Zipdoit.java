package test.com.java.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.nio.channels.WritableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 * 压缩20M文件从30秒到1秒的优化过程
 * https://blog.csdn.net/xx123698/article/details/99683808
 * @author beabody
 *
 */
public class Zipdoit {

	public static String ZIP_FILE ="";
	public static void zipFileNoBuffer() {
    File zipFile = new File(ZIP_FILE);
    try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
        //开始时间
        long beginTime = System.currentTimeMillis();
 
        for (int i = 0; i < 10; i++) {
            try (InputStream input = new FileInputStream("")) {
                zipOut.putNextEntry(new ZipEntry("" + i));
                int temp = 0;
                while ((temp = input.read()) != -1) {
                    zipOut.write(temp);
                }
            }
        }
        System.out.println(beginTime);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
	
	
	
	public static void zipFileBuffer() {
	    File zipFile = new File(ZIP_FILE);
	    try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
	            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zipOut)) {
	        //开始时间
	        long beginTime = System.currentTimeMillis();
	        for (int i = 0; i < 10; i++) {
	            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(""))) {
	                zipOut.putNextEntry(new ZipEntry("" + i));
	                int temp = 0;
	                while ((temp = bufferedInputStream.read()) != -1) {
	                    bufferedOutputStream.write(temp);
	                }
	            }
	        }
	        System.out.println(beginTime);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

public static void zipFileChannel() {
    //开始时间
    long beginTime = System.currentTimeMillis();
    File zipFile = new File(ZIP_FILE);
    try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
        for (int i = 0; i < 10; i++) {
            try (FileChannel fileChannel = new FileInputStream("").getChannel()) {
                zipOut.putNextEntry(new ZipEntry(i + ""));
                fileChannel.transferTo(0, 1, writableByteChannel);
            }
        }
        System.out.println(beginTime);
    } catch (Exception e) {
        e.printStackTrace();
    }

}


public static void zipFileMap() {
    //开始时间
    long beginTime = System.currentTimeMillis();
    File zipFile = new File(ZIP_FILE);
    try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
        for (int i = 0; i < 10; i++) {
 
            zipOut.putNextEntry(new ZipEntry(i + ""));
 
            //内存中的映射文件
            MappedByteBuffer mappedByteBuffer = new RandomAccessFile("", "r").getChannel()
                    .map(FileChannel.MapMode.READ_ONLY, 0, 1);
 
            writableByteChannel.write(mappedByteBuffer);
        }
        System.out.println(beginTime);
    } catch (Exception e) {
        e.printStackTrace();
    }
}


public static void zipFilePip() {
	 //1.8
//    long beginTime = System.currentTimeMillis();
//    try(WritableByteChannel out = Channels.newChannel(new FileOutputStream(ZIP_FILE))) {
//        Pipe pipe = Pipe.open();
//        //异步任务
//        CompletableFuture.runAsync(()->runTask(pipe));
// 
//        //获取读通道
//        ReadableByteChannel readableByteChannel = pipe.source();
//        ByteBuffer buffer = ByteBuffer.allocate(((int) FILE_SIZE)*10);
//        while (readableByteChannel.read(buffer)>= 0) {
//            buffer.flip();
//            out.write(buffer);
//            buffer.clear();
//        }
//    }catch (Exception e){
//        e.printStackTrace();
//    }
//    printInfo(beginTime);
 
}
 
//异步任务
public static void runTask(Pipe pipe) {
 
    try(ZipOutputStream zos = new ZipOutputStream(Channels.newOutputStream(pipe.sink()));
            WritableByteChannel out = Channels.newChannel(zos)) {
        System.out.println("Begin");
        for (int i = 0; i < 10; i++) {
            zos.putNextEntry(new ZipEntry(i+""));
 
            FileChannel jpgChannel = new FileInputStream(new File("")).getChannel();
 
            jpgChannel.transferTo(0, 1, out);
 
            jpgChannel.close();
        }
    }catch (Exception e){
        e.printStackTrace();
    }
}
	
}
