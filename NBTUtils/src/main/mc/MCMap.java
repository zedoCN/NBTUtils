package main.mc;

import main.exception.NBTException;
import main.io.MCUtil;
import main.nbt.CompoundTag;
import text.lib.MyImageFilter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MCMap {
    private CompoundTag map;
    private File mapFile;

    /**
     * 创建地图对象
     *
     * @param mapFile 地图文件
     * @throws IOException
     */
    public MCMap(File mapFile) throws IOException {
        this.mapFile = mapFile;
        map = MCUtil.readDATFile(mapFile);
    }

    /**
     * 保存地图文件
     *
     * @throws IOException
     */
    public void saveFile() throws IOException {
        MCUtil.writeDATFile(mapFile, map);
    }

    /**
     * 设置地图被制图台锁定
     *
     * @param locked 是否锁定
     */
    public void setLocked(boolean locked) {
        map.getCompoundTag("").getCompoundTag("data").setTag("locked", (byte) (locked ? 1 : 0));
    }

    /**
     * 地图在游戏世界里的位置中心
     *
     * @param xz 位置中心坐标
     */
    public void setXZCenter(MCPosInt xz) {
        map.getCompoundTag("").getCompoundTag("data").setTag("xCenter", xz.x);
        map.getCompoundTag("").getCompoundTag("data").setTag("zCenter", xz.z);
    }

    /**
     * 设置位置箭头是否被显示
     *
     * @param trackingPosition 是否被显示
     */
    public void setTrackingPosition(boolean trackingPosition) {
        map.getCompoundTag("").getCompoundTag("data").setTag("trackingPosition", (byte) (trackingPosition ? 1 : 0));
    }

    /**
     * 设置玩家位置指示器是否显示
     *
     * @param unlimitedTracking 是否被显示
     */
    public void setUnlimitedTracking(boolean unlimitedTracking) {
        map.getCompoundTag("").getCompoundTag("data").setTag("unlimitedTracking", (byte) (unlimitedTracking ? 1 : 0));
    }

    /**
     * 设置地图缩放等级
     *
     * @param scale 缩放等级(最大为4)
     */
    public void setScale(int scale) {
        if (scale < 0)
            scale = 0;
        if (scale > 4)
            scale = 4;
        map.getCompoundTag("").getCompoundTag("data").setTag("scale", (byte) scale);
    }


    /**
     * 获取某像素颜色
     *
     * @param x 水平像素位置
     * @param y 垂直像素位置
     * @return 返回获取的颜色
     */
    public Color getColor(int x, int y) {
        return MCMapColors.byte2color(((byte[]) (map.getCompoundTag("").getCompoundTag("data").getTag("colors")))[x + y * 128]);
    }

    /**
     * 设置某像素颜色
     *
     * @param x     水平像素位置
     * @param y     垂直像素位置
     * @param color 要设置的颜色
     */
    public void setColor(int x, int y, Color color) {
        ((byte[]) (map.getCompoundTag("").getCompoundTag("data").getTag("colors")))[x + y * 128] = MCMapColors.color2byte(color);
    }

    /**
     * 设置图片 (128x128)
     *
     * @param inputStream 图片输入流
     * @throws IOException
     */
    public void setImg(InputStream inputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        for (int y = 0; y < 128; y++) {
            for (int x = 0; x < 128; x++) {
                setColor(x, y, new Color(bufferedImage.getRGB(x, y)));
            }
        }

    }

    /**
     * 输出png图片 (128x128)
     *
     * @param outputStream
     * @throws IOException
     */
    public void save2img(OutputStream outputStream) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(128, 128, BufferedImage.TYPE_4BYTE_ABGR);
        for (int y = 0; y < 128; y++) {
            for (int x = 0; x < 128; x++) {
                bufferedImage.setRGB(x, y, getColor(x, y).getRGB());
            }
        }
        ImageIO.write(bufferedImage, "png", outputStream);
    }

}
