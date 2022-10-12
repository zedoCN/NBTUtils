package main.mc;

import main.exception.NBTException;

import java.util.ArrayList;

public class MCBlocksCollective {
    public MCPosInt lwh;//集合的宽高
    public ArrayList<MCBlock> bloks;//方块集合

    public MCBlocksCollective(MCPosInt lwh) {
        this.lwh = lwh;
        bloks = new ArrayList<MCBlock>(lwh.x * lwh.y * lwh.z);
        for (int i = 0; i < lwh.x * lwh.y * lwh.z; i++) {
            bloks.add(null);
        }
    }

    /**
     * 设置方块
     *
     * @param pos   要设置的坐标
     * @param block 方块
     */
    public void setBlock(MCPosInt pos, MCBlock block) {
        checkPos(pos);
        bloks.set(pos.x + pos.z * lwh.x + pos.y * lwh.x * lwh.z, block);
    }

    /**
     * 获取方块
     *
     * @param pos 要获取的坐标
     * @return 方块
     */
    public MCBlock getBlock(MCPosInt pos) {
        checkPos(pos);
        return bloks.get(pos.x + pos.z * lwh.x + pos.y * lwh.x * lwh.z);
    }

    /**
     * 检查坐标是否超集合
     *
     * @param pos 欲检查坐标
     */
    private void checkPos(MCPosInt pos) {
        if ((pos.x < 0 || pos.x >= lwh.x) || (pos.y < 0 || pos.y >= lwh.y) || (pos.z < 0 || pos.z >= lwh.z))
            throw new NBTException("超出范围:" + pos.toStr());
    }

    public MCBlocksCollective clone() {
        MCBlocksCollective mcBlocksCollective = new MCBlocksCollective(lwh.clone());
        mcBlocksCollective.bloks = (ArrayList<MCBlock>) bloks.clone();
        return mcBlocksCollective;
    }

    /**
     * 空间翻转
     *
     * @param xFlip x翻转
     * @param yFlip y翻转
     * @param zFlip z翻转
     * @return 自身
     */
    public MCBlocksCollective flip(boolean xFlip, boolean yFlip, boolean zFlip) {
        MCBlocksCollective BlocksCollective = clone();
        MCPosInt.iteratePos(lwh, p -> {
            MCPosInt blockPos = p.clone();
            if (xFlip)
                blockPos.x = lwh.x - p.x - 1;
            if (yFlip)
                blockPos.y = lwh.y - p.y - 1;
            if (zFlip)
                blockPos.z = lwh.z - p.z - 1;
            setBlock(p, BlocksCollective.getBlock(blockPos));
        });
        return this;
    }

    /**
     * 空间旋转
     *
     * @param rotation 旋转的方向 0绕x轴 1绕y轴 2绕z轴
     * @param times    旋转的次数 整数顺时针 负数逆时针
     * @return 自身
     */
    public MCBlocksCollective rotation(int rotation, int times) {
        while (times != 0) {
            MCBlocksCollective BlocksCollective = clone();

            {
                int temp;
                if (rotation == 0) {
                    temp = lwh.z;
                    lwh.z = lwh.y;
                    lwh.y = temp;
                } else if (rotation == 1) {
                    temp = lwh.z;
                    lwh.z = lwh.x;
                    lwh.x = temp;
                } else if (rotation == 2) {
                    temp = lwh.y;
                    lwh.y = lwh.x;
                    lwh.x = temp;
                }
            }

            MCPosInt.iteratePos(lwh, p -> {
                MCPosInt blockPos = p.clone();
                int temp;
                if (rotation == 0) {
                    temp = blockPos.z;
                    blockPos.z = blockPos.y;
                    blockPos.y = temp;
                } else if (rotation == 1) {
                    temp = blockPos.z;
                    blockPos.z = blockPos.x;
                    blockPos.x = temp;
                } else if (rotation == 2) {
                    temp = blockPos.y;
                    blockPos.y = blockPos.x;
                    blockPos.x = temp;
                }


                setBlock(p, BlocksCollective.getBlock(blockPos));
            });

            if (rotation == 0) {
                flip(false, times > 0, times < 0);
            } else if (rotation == 1) {
                flip(times < 0, false, times > 0);
            } else if (rotation == 2) {
                flip(times > 0, times < 0, false);
            }


            if (times > 0)
                times--;
            else if (times < 0)
                times++;

        }
        return this;
    }

    public MCBlocksCollective replace(replace r, MCBlock replaceBlock) {
        MCPosInt.iteratePos(lwh, p -> {
            if (r.isReplace(getBlock(p)))
                setBlock(p, replaceBlock);
        });
        return this;
    }

    public interface replace {
        boolean isReplace(MCBlock block);
    }
}