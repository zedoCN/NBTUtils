package test;
/**
 * 测试-对mc地图的操作，是能拿在手上的地图！
 */

import main.mc.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Test_Img2Map {
    public static void main(String[] args) {


        try {

            BufferedImage Image = ImageIO.read(new File("E:\\工程文件\\java\\NBTUtils\\src\\text\\白猫RGB.png"));


            BufferedImage bufferedImage = new BufferedImage(250, 250, BufferedImage.TYPE_4BYTE_ABGR);
            for (int y = 0; y < 250; y++) {
                for (int x = 0; x < 250; x++) {
                    bufferedImage.setRGB(x, y, MCMapColors.byte2color(MCMapColors.color2byte(new Color(Image.getRGB(x*4, y*4)))).getRGB());
                }
            }
            ImageIO.write(bufferedImage, "png", new File("E:\\工程文件\\java\\NBTUtils\\src\\text\\白猫RGB预览.png"));






            /*{
                MCMap mcMap = new MCMap(new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\地图画测试\\data\\map_0.dat"));
                mcMap.setImg(new File("E:\\工程文件\\java\\MCNBT\\src\\text\\RGB.png"));
                mcMap.saveFile();
            }
            {
                for (int i = 0; i < 4; i++) {
                    MCMap mcMap = new MCMap(new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\地图画测试\\data\\map_" + (i + 1) + ".dat"));
                    mcMap.setImg(new File("E:\\工程文件\\java\\MCNBT\\src\\text\\IMG" + (i + 1) + ".png"));
                    mcMap.saveFile();
                }
            }*/
           /* {
                MCMap mcMap = new MCMap(new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\data\\map_17.dat"));
                mcMap.save2img(new File("E:\\工程文件\\java\\MCNBT\\src\\text\\保存的地图.png"));
            }*/

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /*try {
            MCRegion mcRegion = new MCRegion(new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\BadApple\\region"));
            CompoundTag block = mcRegion.getBlockState(new MCPosInt(2 ,-60, -1));
            System.out.println(block);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        //octave=mid low high  {Properties={note=0, octave=low, powered=false, instrument=harp_l}, Name=minecraft:note_block}

        //System.out.println(MCPosInt.pos2subChunkIndex(new MCPosInt(100,-10,100)).toStr());
        //System.out.println(MCPosInt.pos2chunk2(new MCPosInt(10000,100,100)).toStr());


        //System.out.println(1024>> 5);
        //System.out.println(9999999>> 5);
        //System.out.println(MCPosInt.blockToChunk(-114514));
        /*System.out.println(MCPosInt.chunk2relativelyChunk(new MCPosInt(0,0)).toStr());

        try {
            MCRegion mcRegion = new MCRegion(new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\测试地图\\region"));

            CompoundTag c = mcRegion.getBlockEntitie(new MCPosInt(4, -60, 0));
            System.out.println(c);

            mcRegion.setBlockEntitie(new MCPosInt(10, -60, -21), c);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/


        //System.out.println(MCUtil.getMapBitSize(33));
        //MCSubChunk mcSubChunk = new MCSubChunk(1, 4);
        //mcSubChunk.set(12, 5);

        //System.out.println(mcSubChunk.get(12));
        //mcSubChunk.set(17, 0xf);
        //System.out.println(Long.toBinaryString(mcSubChunk.getLongArray()[0]));
        //System.out.println(BitsUtils.bits2fStr(BitSet.valueOf(mcSubChunk.getLongArray())));
       /* long a = 55;
        a = a << 10;
        System.out.println(Long.toBinaryString(a));
        System.out.println(Long.toBinaryString(a & 0xF));
        System.out.println(a & 0xF);*/
        //System.out.println(BitsUtils.bits2fStr(BitSet.valueOf(a.toLongArray())));
        //System.out.println(MCUtil.getMapBitSize(16 * 16));
            /*MCRegion mcRegion = new MCRegion(new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\测试地图\\region"));
            mcRegion.setBlockState(new MCPosInt(-30, -60, 9), new CompoundTag().setTag("Name", "minecraft:red_wool"));
            mcRegion.setBlockState(new MCPosInt(-32, -60, 0), new CompoundTag().setTag("Name", "minecraft:red_wool"));
            mcRegion.setBlockState(new MCPosInt(-512, -60, 0), new CompoundTag().setTag("Name", "minecraft:red_wool"));
            mcRegion.setBlockState(new MCPosInt(-513, -60, 0), new CompoundTag().setTag("Name", "minecraft:red_wool"));
            mcRegion.setBlockState(new MCPosInt(32, -60, 0), new CompoundTag().setTag("Name", "minecraft:red_wool"));
            mcRegion.setBlockState(new MCPosInt(-511, -60, 0), new CompoundTag().setTag("Name", "minecraft:red_wool"));
            mcRegion.setBlockState(new MCPosInt(-512, -60, 0), new CompoundTag().setTag("Name", "minecraft:red_wool"));
            mcRegion.setBlockState(new MCPosInt(-513, -60, 0), new CompoundTag().setTag("Name", "minecraft:red_wool"));
            mcRegion.upDate();*/
        //MCA mca = MCUtil.readMCAFile(new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界 (1)\\region\\r.19.19.mca"));
           /* MCChunk chunk = mca.getChunk(new MCPosInt(17, 17));
            System.out.println(chunk.getBlockState(new MCPosInt(0, 100, 0)));

            int count = 0;
            for (int j = 0; j < 50; j++) {
                count++;
                for (int i = 0; i < 16; i++) {
                    chunk.setBlockState(new CompoundTag().setTag("Name", "minecraft:diamond_block"), new MCPosInt(i, 80 + count, 0));
                }

                count++;
                for (int i = 0; i < 16; i++) {
                    chunk.setBlockState(new CompoundTag().setTag("Name", "minecraft:gold_block"), new MCPosInt(i, 80 + count, 15));
                }
                count++;
                for (int i = 0; i < 16; i++) {
                    chunk.setBlockState(new CompoundTag().setTag("Name", "minecraft:glass"), new MCPosInt(15, 80 + count, i));
                }

                count++;
                for (int i = 0; i < 16; i++) {
                    chunk.setBlockState(new CompoundTag().setTag("Name", "minecraft:iron_block"), new MCPosInt(0, 80 + count, i));
                }

            }


            MCUtil.writeMCAFile(mca, new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界aaa\\region\\r.19.19.mca"));*/

        //MCRegion mcRegion=new MCRegion(new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\region"));



/*
        try {
            MCPosInt localPos = MCChunkUtils.chunk2local(MCChunkUtils.pos2chunk(new MCPosInt(10000, 10000)));
            MCA mca = MCUtil.readMCAFile(new FileInputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\region\\r." + localPos.x + "." + localPos.z + ".mca"));
            CompoundTag chunk = MCChunkUtils.findChunk(mca.chunksNBT, new MCPosInt(10000 / 16, -4, 10000 / 16));


            //CompoundTag az = new CompoundTag();
            //az.setCompoundTag("",new CompoundTag().setListTag("az", mca.chunksNBT));

            // MCUtil.writeNBT(az, new FileOutputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\aaaa.dat"));

            //System.out.println(mca.chunksNBT);
            System.out.println(MCChunkUtils.subChunkFindBlockStates(MCChunkUtils.findSubChunk(chunk, -4), new MCPosInt(0, 6, 0)).getTag("Name"));
            System.out.println(MCChunkUtils.subChunkFindBlockStates(MCChunkUtils.findSubChunk(chunk, -4), new MCPosInt(0, 6, 0)));

            System.out.println("修改前---");
            System.out.println(MCChunkUtils.chunkFindBlockEntities(chunk, new MCPosInt(10000, -59, 10000)));
            CompoundTag block = MCChunkUtils.chunkFindBlockEntities(chunk, new MCPosInt(10000, -59, 10000)).getListTag("Items").getCompoundTag(0);
            block.setTag("id", "minecraft:diamond");
            block.setCompoundTag("tag").setCompoundTag("display").setTag("Name","{\"text\":\"ZEDO\",\"color\":\"green\"}");
            System.out.println("修改后---");
            System.out.println(MCChunkUtils.chunkFindBlockEntities(chunk, new MCPosInt(10000, -59, 10000)));

            MCUtil.writeMCAFile(mca, new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\region\\r." + localPos.x + "." + localPos.z + ".mca"));

        } catch (IOException e) {
            throw new RuntimeException(e);

        }
*/


        /*try {

            System.out.println(MCUtil.readDATFile(new FileInputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\level.dat")));
            MCUtil.writeNBT(MCUtil.readDATFile(new FileInputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\level.dat")),new FileOutputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\level.dat.dat"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        //System.out.println(BytesUtils.bytes2longA(new byte[]{ (byte) 0x64,(byte) 0x00,(byte) 0x00 ,(byte) 0xFF,(byte) 0x00 , (byte) 0x00, (byte) 0x00,(byte) 0xAA}));
        //System.out.println(BytesUtils.bytes2longA((new byte[]{ (byte) 0xAA,(byte) 0x00,(byte) 0x00 ,(byte) 0x00 ,(byte) 0xFF,(byte) 0x00, (byte) 0x00,(byte) 0x64})));
        //System.out.println(BytesUtils.bytes2fStr(BytesUtils.long2bytes(-6196953082983612316l)));
        //System.out.println("bit:" + BitsUtils.bits2fStr(BytesUtils.bytes2bitsA(new byte[]{(byte) 0x23,(byte) 0xa3,(byte) 0x89})));
        //System.out.println("bit:" + BitsUtils.bits2fStr(BytesUtils.bytes2bitsA(BytesUtils.bytes2bytesA(new byte[]{(byte) 0x23,(byte) 0xa3,(byte) 0x89}))));


        //ByteArrayOutputStream out =new ByteArrayOutputStream();
        //{={fml=}}
/*        CompoundTag nbt = new CompoundTag("");

        CompoundTag nbt2 = new CompoundTag("复合标签");
        nbt2.addTag(new Tag("short", (short) 2233));
        nbt2.addTag(new Tag("int", -114514));
        nbt2.addTag(new Tag("long", 114514L));
        nbt2.addTag(new Tag("float", 2.33f));
        nbt2.addTag(new Tag("double", 223344.5566));
        nbt2.addTag(new Tag("bytes", new byte[]{0, 1, 2, -5}));
        nbt2.addTag(new Tag("ints", new int[]{11, 22, 33, 44}));
        nbt2.addTag(new Tag("longs", new long[]{11, 22, 33, 44}));
        nbt2.addTag(new Tag("string", "字符串测试"));


        ListTag listTag = new ListTag("longlist", TagType.TAG_Long);
        listTag.addTag(8848L);
        listTag.addTag(-6666L);


        ListTag listTag2 = new ListTag("复合list", TagType.TAG_Compound);
        listTag2.addTag(new CompoundTag(null).addTag(new Tag("tag1","字符串")));
        listTag2.addTag(new CompoundTag(null).addTag(new Tag("tag2","好耶")));

        nbt.addCompoundTag(nbt2);
        nbt.addCompoundTag(new CompoundTag("套娃测试1").addCompoundTag(new CompoundTag("套娃测试2").addCompoundTag(new CompoundTag("套娃测试3"))));
        nbt.addListTag(listTag);
        nbt.addListTag(listTag2);

        System.out.println(nbt);
        try {
            MCUtil.writeNBT(nbt, new FileOutputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\test.dat"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        //System.out.println(BytesUtils.bytes2fStr(out.toByteArray()));


        //ByteArrayInputStream in = new ByteArrayInputStream();


    /*    try {
            inflaterOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        /*try {
            System.out.println(BytesUtils.bytes2fStr(ZLibUtils.decompress(new FileInputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\map.zlib"))));
        new FileOutputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\map.zlib.dat").write(ZLibUtils.decompress(new FileInputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\map.zlib")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/


        /*int x, z;
        x = 1;
        z = 1;
        System.out.println(4 * ((x & 31) + (z & 31) * 32));*/
        /*try {
            FileInputStream fileInputStream = new FileInputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\level.dat");
            GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);

            byte[] data = ((InputStream)gzipInputStream).readAllBytes();
            System.out.println(data.length);

            FileOutputStream fileOutputStream =new FileOutputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\level.dat.gzip");
            fileOutputStream.write(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/


/*        CompoundTag compoundTag;
        try {
            compoundTag = MCUtil.readDATFile(new FileInputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\level.dat"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(compoundTag.getNBT("a").getNBT("aa"));
        System.out.println(compoundTag);
        //compoundTag.getCompoundTag("").getCompoundTag("Data").addTag(new Tag("LevelName","芜湖"));
        System.out.println("世界名:" + compoundTag.getCompoundTag("").getCompoundTag("Data").getTag("LevelName").getData());
        System.out.println("玩家坐标:" + compoundTag.getCompoundTag("").getCompoundTag("Data").getCompoundTag("Player").getListTag("Pos"));

        try {
            MCUtil.writeNBT(compoundTag,new FileOutputStream("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\新的世界\\level.dat.dat"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        /*
        ListTag listTag = compoundTag.getCompoundTag("").getCompoundTag("Data").getCompoundTag("Player").getListTag("Inventory");
        for (int i = 0; i < listTag.size(); i++) {
            System.out.println(listTag.getCompoundTag(i).getTagData("Slot"));
        }*/

        //System.out.println(((int[]) compoundTag.getCompoundTag("").getCompoundTag("Data").getCompoundTag("Player").getTag("UUID").getTag())[0]);
        //CompoundTag compoundTag = new CompoundTag("az");
        //compoundTag.addCompoundTag(new CompoundTag("ddd"));

        //System.out.println();


    }
}
