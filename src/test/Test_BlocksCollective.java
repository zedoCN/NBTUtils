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
            MCRegion mcRegion = new MCRegion(new File("D:\\MineCraft\\MinecraftAll\\.minecraft\\versions\\1.19.4-OptiFine_I4\\saves\\新的世界\\region"));
            //mcRegion.setSaveMode(MCRegion.SAVEMODE_RewriteAll);

            //获取方块集
            MCBlocksCollective blocks = mcRegion.getBlocksCollective(new MCPosInt(20473, 0 ,12), new MCPosInt(20464 ,8, 2));

            //替换水为蓝色羊毛
            blocks.replace(b -> {
                return "minecraft:water".equals(b.getBlockName());
            }, new MCBlock("minecraft:blue_wool"));

            //替换空气为屏障
            blocks.replace(b -> {
                return "minecraft:air".equals(b.getBlockName());
            }, new MCBlock("minecraft:barrier"));

            //设置方块集 绕y轴顺时针旋转1次
            mcRegion.setBlocksCollective(new MCPosInt(20464, 16 ,2), blocks.clone().rotation(1, 1));

            //设置方块集 绕x轴顺时针旋转1次
            mcRegion.setBlocksCollective(new MCPosInt(20464, 24 ,2), blocks.clone().rotation(0, 1));

            //设置方块集 y轴翻转
            mcRegion.setBlocksCollective(new MCPosInt(20464, 32 ,2), blocks.clone().flip(false, true, false));

            //设置方块集 x轴翻转
            mcRegion.setBlocksCollective(new MCPosInt(20464, 40 ,2), blocks.clone().flip(true, false, false));

            //保存
            mcRegion.saveMCA();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
