package text;

import main.mc.MCChunk;
import main.mc.MCPosInt;
import main.mc.MCRegion;
import main.nbt.CompoundTag;
import org.w3c.dom.css.RGBColor;
import text.lib.MyImageFilter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class mapPrint {
    static ArrayList<Block> blocks = new ArrayList<>();

    static class Block {
        String name;
        Color color;

        public Block(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public int compare(Color color2) {
            return Math.abs(color2.getRed() - color.getRed()) + Math.abs(color2.getGreen() - color.getGreen()) + Math.abs(color2.getBlue() - color.getBlue());
        }
    }

    static Block colorFindBlock(Color color) {
        int minIndex = 0;
        int min = 255 * 255 * 255;
        for (int i = 0; i < blocks.size(); i++) {
            int c = blocks.get(i).compare(color);
            if (min > c) {
                min = c;
                minIndex = i;

            }

        }
        return blocks.get(minIndex);
    }

    public static void main(String[] args) {
        ArrayList<File> FileList = new ArrayList<>();
        File[] files = new File("E:\\工程文件\\java\\MCNBT\\src\\text\\mc").listFiles();
        for (File f : files) {
            if (f.isFile()) {
                FileList.add(f);
            }
        }


        try {
            for (File file : FileList) {
                MyImageFilter img = new MyImageFilter(file.getPath());
                img.Zoom(new Dimension(1, 1));
                blocks.add(new Block(file.getName().substring(0, file.getName().length() - 4), new Color(img.bufferedImage.getRGB(0, 0))));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        MyImageFilter img = null;
        try {
            img = new MyImageFilter("E:\\工程文件\\java\\MCNBT\\src\\text\\白猫RGB.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //img.Zoom(new Dimension(128, 128));
        System.out.println("开始生成");
        try {
            MCRegion mcRegion = new MCRegion(new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\测试地图\\region"));


            MCChunk chunk = mcRegion.getChunk(new MCPosInt(0, 0));
            mcRegion.setGenerateChunk(chunk);
            //mcRegion.setChunk(new MCPosInt(0, 6400-10), chunk);
            //mcRegion.setBlockState(new MCPosInt(114514, 1, 114514), new CompoundTag().setTag("Name", "minecraft:diamond_block"));
            //CompoundTag tag = mcRegion.getBlockEntitie(new MCPosInt(114514, 1, 130));
            //tag.getListTag("Items").getCompoundTag(0).getCompoundTag("tag").getListTag("Enchantments").getCompoundTag(0).setTag("lvl", 114514);
            //tag.setTag("CustomName","{\"text\":\"我焯!!!\"}");
            //tag.setTag("Command","say 芜湖起飞~");
            //System.out.println(tag);
            //tag.getCompoundTag("Properties").setTag("facing", "down");
            //mcRegion.setBlockState(new MCPosInt(1138, 1, 130),tag);

            for (int x = 0; x < img.bufferedImage.getWidth(); x++) {
                for (int y = 0; y < img.bufferedImage.getHeight(); y++) {

                    CompoundTag block;
                    //block = new CompoundTag().setTag("Name", "minecraft:air");
                    block = new CompoundTag().setTag("Name", "minecraft:" + colorFindBlock(new Color(img.bufferedImage.getRGB(x, y))).name);
                    mcRegion.setBlockState(new MCPosInt(x + 114514, 0, y + 114514), block);

                }
            }

            /*for (int x = 0; x < img.bufferedImage.getWidth(); x++) {
                for (int y = 0; y < img.bufferedImage.getHeight(); y++) {
                    mcRegion.setBlockState(new MCPosInt(x, 80, y + 10240), new CompoundTag().setTag("Name", "minecraft:" + colorFindBlock(new Color(img.bufferedImage.getRGB(x, y))).name));

                }
            }*/

            mcRegion.saveMCA();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("结束生成");

    }


}
