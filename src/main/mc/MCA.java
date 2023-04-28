package main.mc;

import main.exception.NBTException;
import main.nbt.CompoundTag;
import main.nbt.ListTag;
import main.nbt.TagType;

import java.util.HashMap;

public class MCA {
    public MCPosInt Pos;//当前MCA所在位置
    public long[] chunkTimeStamp = new long[1024];//时间戳
    public CompoundTag[] chunksNBT = new CompoundTag[1024];//区块数据
    public boolean[] chunksFlag = new boolean[1024];//被读取或修改过的标记

    /**
     * 获取相对区块
     *
     * @param chunkIndex 区块索引
     * @return 区块对象
     */
    public MCChunk getChunk(int chunkIndex) {
        if (chunksNBT[chunkIndex] == null)
            throw new NBTException("区块为空:" + chunkIndex);
        chunksFlag[chunkIndex] = true;
        return new MCChunk(chunksNBT[chunkIndex]);
    }

    /**
     * 复制区块标签对象
     *
     * @param chunkIndex 区块索引
     * @return 新的区块标签对象
     */
    public CompoundTag cloneChunkCompoundTag(int chunkIndex) {
        if (chunksNBT[chunkIndex] == null)
            throw new NBTException("区块为空:" + chunkIndex);
        chunksFlag[chunkIndex] = true;
        return chunksNBT[chunkIndex].clone();
    }
}
