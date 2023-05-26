package test;
/**
 * 测试-对mc地图的操作，是能拿在手上的地图！
 */

import main.mc.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test_Img2Map {
    public static void main(String[] args) {


        try {
            //图片生成预览图
            BufferedImage Image = ImageIO.read(new File("img\\WhiteCat.png"));
            BufferedImage bufferedImage = new BufferedImage(256, 256, BufferedImage.TYPE_4BYTE_ABGR);
            for (int y = 0; y < 256; y++) {
                for (int x = 0; x < 256; x++) {
                    bufferedImage.setRGB(x, y, MCMapColors.byte2color(MCMapColors.color2byte(new Color(Image.getRGB(x * 4, y * 4)))).getRGB());
                }
            }
            ImageIO.write(bufferedImage, "png", new File("img\\WhiteCatPreview.png"));


            {//对编号0的地图更改数据
                MCMap mcMap = new MCMap(new File("D:\\MineCraft\\MinecraftAll\\.minecraft\\versions\\1.19.4-OptiFine_I4\\saves\\新的世界\\data\\map_0.dat"));
                mcMap.setLocked(true);
                mcMap.setScale(4);
                mcMap.setXZCenter(new MCPosInt(100000000, 100000000));
                mcMap.setTrackingPosition(false);
                mcMap.setUnlimitedTracking(false);
                mcMap.setImg(new FileInputStream("img\\RGB.png"));
                mcMap.saveFile();
            }
            {//对编号1到4的地图更改数据
                for (int i = 0; i < 4; i++) {
                    MCMap mcMap = new MCMap(new File("D:\\MineCraft\\MinecraftAll\\.minecraft\\versions\\1.19.4-OptiFine_I4\\saves\\新的世界\\data\\map_" + (i + 1) + ".dat"));
                    mcMap.setLocked(true);
                    mcMap.setScale(4);
                    mcMap.setXZCenter(new MCPosInt(100000000 * i, 100000000));
                    mcMap.setTrackingPosition(false);
                    mcMap.setUnlimitedTracking(false);
                    mcMap.setImg(new FileInputStream("img\\IMG" + (i + 1) + ".png"));
                    mcMap.saveFile();
                }
            }

            {//将编号1的地图保存为图片
                MCMap mcMap = new MCMap(new File("D:\\MineCraft\\MinecraftAll\\.minecraft\\versions\\1.19.4-OptiFine_I4\\saves\\新的世界\\data\\map_1.dat"));
                mcMap.save2img(new FileOutputStream("img\\SavedMap.png"));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
