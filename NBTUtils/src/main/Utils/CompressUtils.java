package main.Utils;

import java.io.*;
import java.util.zip.*;

public abstract class CompressUtils {
    //Zlib压缩
    public static byte[] zlibCompress(byte[] data) throws IOException {
        byte[] output = new byte[0];

        Deflater compresser = new Deflater();

        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        byte[] buf = new byte[1024];
        while (!compresser.finished()) {
            int i = compresser.deflate(buf);
            bos.write(buf, 0, i);
        }
        output = bos.toByteArray();
        bos.close();
        compresser.end();
        return output;
    }

    //Zlib解压
    public static byte[] zlibDecompress(byte[] data) throws IOException, DataFormatException {


        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);

        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);

        byte[] buf = new byte[1024];
        while (!decompresser.finished()) {
            int i = decompresser.inflate(buf);
            o.write(buf, 0, i);
        }
        byte[] output = o.toByteArray();
        o.close();
        decompresser.end();
        return output;
    }

    //Gzip压缩
    public static byte[] gzipCompress(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream =new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(data);
        gzipOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    //Gzip解压
    public static byte[] gzipDecompress(byte[] data) throws IOException {
        GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(data));
        byte[] output = gzipInputStream.readAllBytes();
        gzipInputStream.close();
        return output;
    }

}  