package main.io;

import main.Utils.BytesUtils;
import main.Utils.CompressUtils;
import main.exception.NBTException;
import main.mc.MCA;
import main.mc.MCPosInt;
import main.nbt.CompoundTag;
import main.nbt.ListTag;
import main.nbt.TagType;

import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

public class MCUtil {
    /**
     * 解析NBT
     *
     * @param nbt NBT数据
     * @return 复合标签
     * @throws IOException
     */
    public static CompoundTag parseNBT(byte[] nbt) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(nbt);
        CompoundTag compoundTag = parseCompoundTag(byteArrayInputStream);
        byteArrayInputStream.close();
        return compoundTag;
    }


    /**
     * 计算方块列表大小所占存储的位大小
     *
     * @param blockListSize 方块列表大小
     * @return 存储的位大小
     */
    public static int getMapBitSize(int blockListSize) {
        blockListSize--;
        int count = 0;
        while (blockListSize != 0) {
            count++;
            blockListSize >>= 1;
        }
        return (count < 4 ? 4 : count);
    }

    /**
     * 解析复合标签
     *
     * @param in 输入流
     * @return 复合标签
     * @throws IOException
     */
    private static CompoundTag parseCompoundTag(InputStream in) throws IOException {
        CompoundTag nbt = new CompoundTag();


        while (true) {
            //读取标签类型
            short tagType = BytesUtils.bytes2short(in.readNBytes(1));
            //判断是否结束
            if (tagType == TagType.TAG_End) {
                return nbt;
            }

            //读取标签名
            String tagName = new String(in.readNBytes(BytesUtils.bytes2shortA(in.readNBytes(2))));

            if (tagType == TagType.TAG_Byte) {
                nbt.setTag(tagName, in.readNBytes(1)[0]);
            } else if (tagType == TagType.TAG_Short) {
                nbt.setTag(tagName, BytesUtils.bytes2shortA(in.readNBytes(2)));
            } else if (tagType == TagType.TAG_Int) {
                nbt.setTag(tagName, BytesUtils.bytes2intA(in.readNBytes(4)));
            } else if (tagType == TagType.TAG_Long) {
                nbt.setTag(tagName, BytesUtils.bytes2longA(in.readNBytes(8)));
            } else if (tagType == TagType.TAG_Float) {
                nbt.setTag(tagName, BytesUtils.bytes2floatA(in.readNBytes(4)));
            } else if (tagType == TagType.TAG_Double) {
                nbt.setTag(tagName, BytesUtils.bytes2doubleA(in.readNBytes(8)));
            } else if (tagType == TagType.TAG_Byte_Array) {
                nbt.setTag(tagName, in.readNBytes(BytesUtils.bytes2intA(in.readNBytes(4))));
            } else if (tagType == TagType.TAG_String) {
                nbt.setTag(tagName, new String(in.readNBytes(BytesUtils.bytes2shortA(in.readNBytes(2)))));
            } else if (tagType == TagType.TAG_List) {
                nbt.setListTag(tagName, parseListTag(in));
            } else if (tagType == TagType.TAG_Compound) {
                nbt.setCompoundTag(tagName, parseCompoundTag(in));
            } else if (tagType == TagType.TAG_Int_Array) {
                int arraySize = BytesUtils.bytes2intA(in.readNBytes(4));//读取数组个数
                int[] intdata = new int[arraySize];
                for (int j = 0; j < arraySize; j++) {
                    intdata[j] = BytesUtils.bytes2intA(in.readNBytes(4));
                }
                nbt.setTag(tagName, intdata);
            } else if (tagType == TagType.TAG_Long_Array) {
                int arraySize = BytesUtils.bytes2intA(in.readNBytes(4));//读取数组个数
                long[] intdata = new long[arraySize];
                for (int j = 0; j < arraySize; j++) {
                    intdata[j] = BytesUtils.bytes2longA(in.readNBytes(8));
                }
                nbt.setTag(tagName, intdata);
            } else if (tagType == TagType.TAG_End) {
                return nbt;
            }
        }
    }

    /**
     * 解析列表标签
     *
     * @param in 输入流
     * @return 列表标签
     * @throws IOException
     */
    private static ListTag parseListTag(InputStream in) throws IOException {

        //读取列表类型
        short ListTagType = BytesUtils.bytes2short(in.readNBytes(1));
        //创建新列表
        ListTag nbtList = new ListTag(ListTagType);
        //获取列表成员数
        int listSize = BytesUtils.bytes2intA(in.readNBytes(4));
        //遍历所有成员
        for (int i = 0; i < listSize; i++) {
            if (ListTagType == TagType.TAG_Byte) {
                nbtList.addTag(in.readNBytes(1)[0]);
            } else if (ListTagType == TagType.TAG_Short) {
                nbtList.addTag(BytesUtils.bytes2shortA(in.readNBytes(2)));
            } else if (ListTagType == TagType.TAG_Int) {
                nbtList.addTag(BytesUtils.bytes2intA(in.readNBytes(4)));
            } else if (ListTagType == TagType.TAG_Long) {
                nbtList.addTag(BytesUtils.bytes2longA(in.readNBytes(8)));
            } else if (ListTagType == TagType.TAG_Float) {
                nbtList.addTag(BytesUtils.bytes2floatA(in.readNBytes(4)));
            } else if (ListTagType == TagType.TAG_Double) {
                nbtList.addTag(BytesUtils.bytes2doubleA(in.readNBytes(8)));
            } else if (ListTagType == TagType.TAG_Byte_Array) {
                nbtList.addTag(in.readNBytes(BytesUtils.bytes2intA(in.readNBytes(4))));
            } else if (ListTagType == TagType.TAG_String) {
                nbtList.addTag(new String(in.readNBytes(BytesUtils.bytes2shortA(in.readNBytes(2)))));
            } else if (ListTagType == TagType.TAG_List) {
                nbtList.addTag(parseListTag(in));
            } else if (ListTagType == TagType.TAG_Compound) {
                nbtList.addTag(parseCompoundTag(in));
            } else if (ListTagType == TagType.TAG_Int_Array) {
                int arraySize = BytesUtils.bytes2intA(in.readNBytes(4));//读取数组个数
                int[] intdata = new int[arraySize];
                for (int j = 0; j < arraySize; j++) {
                    intdata[j] = BytesUtils.bytes2intA(in.readNBytes(4));
                }
                nbtList.addTag(intdata);
            } else if (ListTagType == TagType.TAG_Long_Array) {
                int arraySize = BytesUtils.bytes2intA(in.readNBytes(4));//读取数组个数
                long[] longdata = new long[arraySize];
                for (int j = 0; j < arraySize; j++) {
                    longdata[j] = BytesUtils.bytes2longA(in.readNBytes(8));
                }
                nbtList.addTag(longdata);
            } else {
                throw new NBTException("ListTag处理异常");
            }
        }

        return nbtList;
    }

    /**
     * 写标签
     *
     * @param nbt 标签
     * @param out 输出流
     * @throws IOException
     */
    public static void writeNBT(CompoundTag nbt, OutputStream out) throws IOException {
        writeCompoundTag(nbt, out);
    }

    /**
     * 写复合标签
     *
     * @param nbt 复合标签
     * @param out 输出流
     * @throws IOException
     */
    private static void writeCompoundTag(CompoundTag nbt, OutputStream out) throws IOException {


        Iterator<Map.Entry<String, Object>> integer = nbt.entrySet().iterator();
        while (integer.hasNext()) {
            Map.Entry<String, Object> tag = integer.next();
            short tagType = TagType.Object2TagType(tag.getValue());
            //判断标签类型
            if (tagType == -1) {
                throw new NBTException("错误的标签类型");
            }

            out.write(tagType);//写标签类型
            out.write(BytesUtils.short2bytes((short) tag.getKey().getBytes().length));//标签名长度
            out.write(tag.getKey().getBytes());


            writeTag(tagType, tag.getValue(), out);

        }

        //out.write(TagType.TAG_End);//复合标签尾


    }

    /**
     * 写出标签
     *
     * @param tagType 标签类型
     * @param object  标签对象
     * @param out     输出流
     * @throws IOException
     */
    private static void writeTag(short tagType, Object object, OutputStream out) throws IOException {
        if (tagType == TagType.TAG_Byte) {
            out.write((byte) object);
        } else if (tagType == TagType.TAG_Short) {
            out.write(BytesUtils.short2bytes((short) object));
        } else if (tagType == TagType.TAG_Int) {
            out.write(BytesUtils.int2bytes((int) object));
        } else if (tagType == TagType.TAG_Long) {
            out.write(BytesUtils.long2bytes((long) object));
        } else if (tagType == TagType.TAG_Float) {
            out.write(BytesUtils.float2bytes((float) object));
        } else if (tagType == TagType.TAG_Double) {
            out.write(BytesUtils.double2bytes((double) object));
        } else if (tagType == TagType.TAG_Byte_Array) {
            out.write(BytesUtils.int2bytes(((byte[]) object).length));
            out.write((byte[]) object);
        } else if (tagType == TagType.TAG_String) {
            out.write(BytesUtils.short2bytes((short) ((String) object).getBytes().length));
            out.write(((String) object).getBytes());
        } else if (tagType == TagType.TAG_List) {
            writeListTag((ListTag) object, out);
        } else if (tagType == TagType.TAG_Compound) {
            writeCompoundTag((CompoundTag) object, out);
            out.write(0);
        } else if (tagType == TagType.TAG_Int_Array) {
            out.write(BytesUtils.int2bytes(((int[]) object).length));
            for (int ints : (int[]) object) {
                out.write(BytesUtils.int2bytes(ints));
            }
        } else if (tagType == TagType.TAG_Long_Array) {
            out.write(BytesUtils.int2bytes(((long[]) object).length));
            for (long longs : (long[]) object) {
                out.write(BytesUtils.long2bytes(longs));
            }
        }
    }

    /**
     * 写出列表标签
     *
     * @param nbt 列表标签
     * @param out 输出流
     * @throws IOException
     */
    private static void writeListTag(ListTag nbt, OutputStream out) throws IOException {
        short ListTagType = nbt.type;

        /*out.write(TagType.TAG_List);//写标签类型
        out.write(BytesUtils.short2bytes((short) nbt.Name.getBytes().length));//标签名长度
        out.write(nbt.Name.getBytes());*/


        out.write(ListTagType);//写列表类型
        out.write(BytesUtils.int2bytes(nbt.size()));//写列表大小
        for (int i = 0; i < nbt.size(); i++) {
            Object object = nbt.get(i);
            writeTag(ListTagType, object, out);

        }


    }

    /**
     * 读取Dat文件
     *
     * @param filePath 文件路径
     * @return 读取的复合标签
     * @throws IOException
     */
    public static CompoundTag readDATFile(File filePath) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(filePath);
        CompoundTag tag = parseNBT(CompressUtils.gzipDecompress(fileInputStream.readAllBytes()));
        fileInputStream.close();
        return tag;

    }

    /**
     * 写出Dat文件
     *
     * @param filePath    文件路径
     * @param compoundTag 复合标签
     * @throws IOException
     */
    public static void writeDATFile(File filePath, CompoundTag compoundTag) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeNBT(compoundTag, byteArrayOutputStream);
        fileOutputStream.write(CompressUtils.gzipCompress(byteArrayOutputStream.toByteArray()));
        byteArrayOutputStream.close();
        fileOutputStream.close();
    }

    /**
     * 读取MCA文件   mca坐标
     *
     * @param path   mca文件夹路径
     * @param mcaPos mca文件位置
     * @return mca对象
     * @throws IOException
     */
    public static MCA readMCAFile(File path, MCPosInt mcaPos) throws IOException {
        MCA mca = new MCA();
        mca.Pos = mcaPos.clone();
        File mcaFile = new File(path.getPath() + "\\r." + mcaPos.x + "." + mcaPos.z + ".mca");
        if (!mcaFile.isFile()) {
            mcaFile.createNewFile();
        }

        RandomAccessFile randomAccessFile = new RandomAccessFile(mcaFile, "r");
        for (int i = 0; i < 1024; i++) {
            //顺序读取区块索引表
            randomAccessFile.seek(i * 4);
            //读取区块偏移
            int chunkPosOffset = BytesUtils.bytes2intA(BytesUtils.bytesSplicing(new byte[1], RandomAccessFileReadBytes(randomAccessFile, 3)));
            if (chunkPosOffset == 0) {
                //区块还未生成
                continue;
            }

            //区块数据段大小
            byte chunkSectionSize = randomAccessFile.readByte();


            //跳转区块数据区
            randomAccessFile.seek(chunkPosOffset * 4096);

            int chunkSize = randomAccessFile.readInt();
            byte compressType = randomAccessFile.readByte();
            byte[] chunkData = new byte[chunkSize];
            randomAccessFile.read(chunkData);
            if (compressType == 1) {//GZip压缩
                mca.chunksNBT[i] = MCUtil.parseNBT(CompressUtils.gzipDecompress(chunkData));
            } else if (compressType == 2) {//ZLib压缩
                try {
                    mca.chunksNBT[i] = (MCUtil.parseNBT(CompressUtils.zlibDecompress(chunkData)));
                } catch (DataFormatException e) {
                    throw new NBTException("ZLIB解压失败");
                }
            } else if (compressType == 3) {//未压缩
                mca.chunksNBT[i] = (MCUtil.parseNBT(chunkData));
            } else {
                System.out.println("未知压缩类型:" + String.valueOf(compressType));
                //throw new NBTException("未知压缩类型:" + String.valueOf(compressType));
            }
        }
        randomAccessFile.close();

        return mca;
    }

    /**
     * 写出MCA文件 全部修改
     *
     * @param mca  mca对象
     * @param path mca文件夹路径
     * @throws IOException
     */
    public static void writeMCAFile(MCA mca, File path) throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile(
                new File(path.getPath() + "\\r." + mca.Pos.x + "." + mca.Pos.z + ".mca"), "rw");


        randomAccessFile.setLength(0);//清除所有


        long time = System.currentTimeMillis();//生成新时间戳

        //数据区写区块
        int chunksCount = 2;
        for (int i = 0; i < 1024; i++) {
            if (mca.chunksNBT[i] != null) {
                //写区块数据
                randomAccessFile.seek(chunksCount * 4096);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                MCUtil.writeNBT(mca.chunksNBT[i], byteArrayOutputStream);
                byte[] NBTData = CompressUtils.zlibCompress(byteArrayOutputStream.toByteArray());
                byteArrayOutputStream.close();
                randomAccessFile.writeInt(NBTData.length);
                randomAccessFile.write(2);
                randomAccessFile.write(NBTData);


                //写区块时间戳
                randomAccessFile.seek(i * 4 + 4096);
                randomAccessFile.writeInt((int) time);

                //写区块位置
                randomAccessFile.seek(i * 4);
                randomAccessFile.write(BytesUtils.bytesCut(BytesUtils.int2bytes(chunksCount), 1, 0, 3));
                randomAccessFile.write((int) Math.ceil(NBTData.length / 4096.0));

                chunksCount += (int) Math.ceil(NBTData.length / 4096.0);

                //mca.chunkLocation.put(chunksCount, (byte) Math.ceil(NBTData.length / 4096.0));
            }
        }


        randomAccessFile.seek(8191 + chunksCount * 4096);
        randomAccessFile.write(0);
        randomAccessFile.close();

    }

    /**
     * 写出MCA文件 局部修改
     *
     * @param mca  mca对象
     * @param path mca文件夹路径
     * @throws IOException
     */
    public static void writeMCAFile2(MCA mca, File path) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(
                new File(path.getPath() + "\\r." + mca.Pos.x + "." + mca.Pos.z + ".mca"), "rw");
        if (randomAccessFile.length() == 0)
            randomAccessFile.setLength(8192);

        long time = System.currentTimeMillis();//生成新时间戳

        for (int i = 0; i < 1024; i++) {
            if (mca.chunksFlag[i]) {//如果被标记 重写新数据
                //读取区块信息表
                randomAccessFile.seek(i * 4);
                int chunkPosOffset;
                chunkPosOffset = BytesUtils.bytes2intA(BytesUtils.bytesSplicing(new byte[1], RandomAccessFileReadBytes(randomAccessFile, 3)));
                //区块数据段大小
                byte chunkSectionSize = randomAccessFile.readByte();

                /*if (chunkPosOffset == 0) {//未找到区块
                    //MCUtil.writeMCAFile(mca,path);
                    //throw new NBTException("保存时 未找到区块");
                    chunkSectionSize = -1;
                }*/

                //生成新区块数据
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                MCUtil.writeNBT(mca.chunksNBT[i], byteArrayOutputStream);
                byte[] newChunkData = CompressUtils.zlibCompress(byteArrayOutputStream.toByteArray());
                byteArrayOutputStream.close();

                //计算新区块数据所占段数
                byte newChunkSectionSize = (byte) Math.ceil(newChunkData.length / 4096.0);

                //判断大小
                if (chunkSectionSize < newChunkSectionSize) {
                    //System.out.println("遇到增容");
                    //比原先大 查找最大偏移
                    int maxChunkOffset = 0;
                    byte maxChunkSectionSize = 0;
                    int maxChunkIndex = 0;
                    for (int j = 0; j < 1024; j++) {//找到最后面位置
                        //读取区块信息表
                        randomAccessFile.seek(j * 4);
                        int maxChunkPosOffset = BytesUtils.bytes2intA(BytesUtils.bytesSplicing(new byte[1], RandomAccessFileReadBytes(randomAccessFile, 3)));
                        if (maxChunkPosOffset == 0) {
                            continue;
                        }
                        if (maxChunkOffset < maxChunkPosOffset) {
                            maxChunkOffset = maxChunkPosOffset;
                            maxChunkIndex = j;
                            //区块数据段大小
                            maxChunkSectionSize = randomAccessFile.readByte();
                        }

                    }
                    chunkPosOffset = maxChunkOffset + maxChunkSectionSize;//设置最后面的偏移
                }

                if (chunkPosOffset == 0) {
                    chunkPosOffset = 2;
                }

                //写区块数据
                randomAccessFile.seek(chunkPosOffset * 4096);

                randomAccessFile.writeInt(newChunkData.length);
                randomAccessFile.write(2);
                randomAccessFile.write(newChunkData);


                //写区块时间戳
                randomAccessFile.seek(i * 4 + 4096);
                randomAccessFile.writeInt((int) time);

                //写区块信息
                randomAccessFile.seek(i * 4);
                randomAccessFile.write(BytesUtils.bytesCut(BytesUtils.int2bytes(chunkPosOffset), 1, 0, 3));
                randomAccessFile.write(newChunkSectionSize);
            }
        }

    }

    /**
     * RandomAccessFile 读取N个字节
     *
     * @param randomAccessFile RandomAccessFile对象
     * @param lenth            欲读取的字节长度
     * @return 读取的字节数组
     * @throws IOException
     */
    private static byte[] RandomAccessFileReadBytes(RandomAccessFile randomAccessFile, int lenth) throws IOException {
        byte[] data = new byte[lenth];
        randomAccessFile.read(data);
        return data;
    }
}
