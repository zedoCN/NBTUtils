package main.mc;

import main.Utils.BytesUtils;
import main.exception.NBTException;
import main.io.MCUtil;
import main.nbt.CompoundTag;
import main.nbt.ListTag;
import main.nbt.TagType;

import java.util.BitSet;

public class MCChunk {
    public CompoundTag chunk;//区块对象

    /**
     * 通过区块标签创建区块对象
     *
     * @param chunk
     */
    public MCChunk(CompoundTag chunk) {
        if (chunk == null)
            throw new NBTException("区块为空");
        this.chunk = chunk;
    }

    /**
     * 复制区块对象
     *
     * @return 新的区块对象
     */
    public MCChunk cloneChunk() {
        return new MCChunk(chunk.clone());
    }

    /**
     * 获取区块坐标 Y可以忽略
     *
     * @return 区块坐标
     */
    public MCPosInt getChunkPos() {
        return new MCPosInt((int) chunk.getTag("xPos"), (int) chunk.getTag("yPos"), (int) chunk.getTag("zPos"));
    }

    /**
     * 在区块里寻找子区块
     *
     * @param Y 区块Y索引
     * @return 子区块标签对象
     */
    public CompoundTag getSubChunk(int Y) {
        ListTag subChunks = chunk.getCompoundTag("").getListTag("sections");
        for (int i = 0; i < subChunks.size(); i++) {
            CompoundTag subChunk = subChunks.getCompoundTag(i);
            if ((byte) subChunk.getTag("Y") == (byte) Y) {
                return subChunk;
            }
        }
        return null;
    }


    /**
     * 获取方块实体对象
     *
     * @param pos 世界绝对坐标
     * @return 方块实体标签
     */
    public CompoundTag getBlockEntitie(MCPosInt pos) {
        ListTag chunkBlockEntities = chunk.getCompoundTag("").getListTag("block_entities");
        if (chunkBlockEntities == null)
            return null;
        for (int i = 0; i < chunkBlockEntities.size(); i++) {
            CompoundTag blockEntitie = chunkBlockEntities.getCompoundTag(i);
            if ((int) blockEntitie.getTag("x") == pos.x)
                if ((int) blockEntitie.getTag("y") == pos.y)
                    if ((int) blockEntitie.getTag("z") == pos.z)
                        return blockEntitie;
        }
        return null;
    }

    /**
     * 设置方块实体对象
     *
     * @param pos          世界绝对坐标
     * @param blockEntitie 方块实体标签
     */
    public void setBlockEntitie(MCPosInt pos, CompoundTag blockEntitie) {

        ListTag chunkBlockEntities = chunk.getCompoundTag("").getListTag("block_entities");
        if (chunkBlockEntities.type == TagType.TAG_End) {
            chunkBlockEntities = new ListTag(TagType.TAG_Compound);
            chunk.getCompoundTag("").setListTag("block_entities", chunkBlockEntities);
        }

        CompoundTag tag = blockEntitie.clone();

        tag.setTag("x", pos.x);
        tag.setTag("y", pos.y);
        tag.setTag("z", pos.z);
        for (int i = 0; i < chunkBlockEntities.size(); i++) {
            if (chunkBlockEntities.getCompoundTag(i).getTag("x") != null)
                if ((int) chunkBlockEntities.getCompoundTag(i).getTag("x") == pos.x)
                    if ((int) chunkBlockEntities.getCompoundTag(i).getTag("y") == pos.y)
                        if ((int) chunkBlockEntities.getCompoundTag(i).getTag("z") == pos.z) {
                            chunkBlockEntities.set(i, tag);
                            return;
                        }

        }
        chunkBlockEntities.addTag(tag);
        return;
    }

    /**
     * 获取一个方块状态
     *
     * @param blockIndex 方块索引
     * @param subChunkY  子区块Y索引
     * @return 方块状态标签
     */
    public CompoundTag getBlockState(int blockIndex, int subChunkY) {
        CompoundTag sunChunkBlocks = getSubChunk(subChunkY).getCompoundTag("block_states");

        //读取区块方块索引
        ListTag blocks = sunChunkBlocks.getListTag("palette");


        int mapBit = MCUtil.getMapBitSize(blocks.size());
        int longIndex = blockIndex / (64 / mapBit);
        int longBlockIndex = blockIndex % (64 / mapBit);
        //System.out.println(sunChunkBlocks);

        if (sunChunkBlocks.getTag("data") == null)
            return blocks.getCompoundTag(0);
        BitSet blockData = BitSet.valueOf(new long[]{((long[]) sunChunkBlocks.getTag("data"))[longIndex]});

        //System.out.println(BytesUtils.bytes2int(blockData.get(longBlockIndex * mapBit, (longBlockIndex + 1) * mapBit).toByteArray()));

        return blocks.getCompoundTag(BytesUtils.bytes2int(blockData.get(longBlockIndex * mapBit, (longBlockIndex + 1) * mapBit).toByteArray()));

    }

    /**
     * 设置方块状态
     *
     * @param blockIndex 方块索引
     * @param subChunkY  子区块Y索引
     * @param blockState 方块状态标签
     */
    public void setBlockState(int blockIndex, int subChunkY, CompoundTag blockState) {
        CompoundTag subChunk = getSubChunk(subChunkY);
        if (subChunk == null) {
            throw new NBTException("Y越界:" + subChunkY);
        }
        CompoundTag sunChunkBlocks = subChunk.getCompoundTag("block_states");
        //读取区块方块索引
        ListTag blocks = sunChunkBlocks.getListTag("palette");
        int mapBitBefore = MCUtil.getMapBitSize(blocks.size());

        //防止为空
        if (sunChunkBlocks.getTag("data") == null)
            sunChunkBlocks.setTag("data", new long[1]);


        int foundBlockIndex = -1;
        //查找方块列表是否存在要添加的方块
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.getCompoundTag(i).equals(blockState)) {
                foundBlockIndex = i;
                break;
            }
        }

        //未找到 添加方块列表
        if (foundBlockIndex == -1) {
            blocks.addTag(blockState);
            foundBlockIndex = blocks.size() - 1;
        }

        int mapBitNow = MCUtil.getMapBitSize(blocks.size());
        MCSubChunk mcSubChunkBefore = new MCSubChunk((long[]) sunChunkBlocks.getTag("data"), (int) Math.ceil(4096.0 / (double) (64 / mapBitBefore)), mapBitBefore);
        if (mapBitNow != mapBitBefore) {//存储位数不一样
            MCSubChunk mcSubChunkNow = new MCSubChunk((int) Math.ceil(4096.0 / (double) (64 / mapBitNow)), mapBitNow);
            for (int i = 0; i < 4096; i++) {
                if (blockIndex == i) {//找到欲替换方块
                    mcSubChunkNow.set(i, foundBlockIndex);
                } else {//转移数据
                    mcSubChunkNow.set(i, mcSubChunkBefore.get(i));
                }

            }
            sunChunkBlocks.setTag("data", mcSubChunkNow.getLongArray());
        } else {
            mcSubChunkBefore.set(blockIndex, foundBlockIndex);
            sunChunkBlocks.setTag("data", mcSubChunkBefore.getLongArray());
        }


    }


}
