package test;

import main.mc.MCBlock;
import main.mc.MCBlocksCollective;
import main.mc.MCPosInt;
import main.mc.MCRegion;

import java.io.File;
import java.io.IOException;

public class Test_BlocksCollective {
    public static void main(String[] args) {
        try {
            //创建
            MCRegion mcRegion = new MCRegion(new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\测试地图\\region"));
            //mcRegion.setSaveMode(MCRegion.SAVEMODE_RewriteAll);

            //获取方块集
            MCBlocksCollective blocks = mcRegion.getBlocksCollective(new MCPosInt(9404, 60, 10171), new MCPosInt(9303, 97, 10098));

            //替换水为蓝色羊毛
            blocks.replace(b -> {
                return "minecraft:water".equals(b.getBlockName());
            }, new MCBlock("minecraft:blue_wool"));

            //替换空气为屏障
            blocks.replace(b -> {
                return "minecraft:air".equals(b.getBlockName());
            }, new MCBlock("minecraft:barrier"));

            //设置方块集 绕y轴顺时针旋转1次
            mcRegion.setBlocksCollective(new MCPosInt(9303, 100, 10098), blocks.rotation(1, 1));

            //设置方块集 y轴翻转
            mcRegion.setBlocksCollective(new MCPosInt(9303, 150, 10098), blocks.flip(false, true, false));

            //保存
            mcRegion.saveMCA();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
