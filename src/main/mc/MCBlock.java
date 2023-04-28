package main.mc;

import main.nbt.CompoundTag;

public class MCBlock {
    public CompoundTag blockState;//方块状态
    public CompoundTag blockEntitie;//方块实体


    public static final String FACING_UP = "up";
    public static final String FACING_DOWN = "down";
    public static final String FACING_NORTH = "north";
    public static final String FACING_SOUTH = "south";
    public static final String FACING_EAST = "east";
    public static final String FACING_WEST = "west";

    /**
     * 通过标签创建方块
     *
     * @param blockState
     * @param blockEntitie
     */
    public MCBlock(CompoundTag blockState, CompoundTag blockEntitie) {
        this.blockState = blockState;
        this.blockEntitie = blockEntitie;
    }

    /**
     * 通过方块名创建方块
     *
     * @param blockName 方块名 如 "minecraft:air"
     */
    public MCBlock(String blockName) {
        blockState = new CompoundTag();
        blockState.setTag("Name", blockName);
    }

    /**
     * 获取方块名
     *
     * @return 方块名
     */
    public String getBlockName() {
        return (String) blockState.getTag("Name");
    }


    /**
     * 设置方块朝向
     *
     * @param blockFacing 方块朝向  请使用"FACING_xx"
     */
    public void setBlockFacing(String blockFacing) {
        blockState.setCompoundTag("Properties").setTag("facing", blockFacing);
    }

    /**
     * 复制一个新对象
     *
     * @return 新方块
     */
    public MCBlock clone() {
        if (blockEntitie == null)
            return new MCBlock(blockState.clone(), null);
        return new MCBlock(blockState.clone(), blockEntitie.clone());
    }


}
