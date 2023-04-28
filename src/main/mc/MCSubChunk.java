package main.mc;

import main.exception.NBTException;

public class MCSubChunk {
    private long[] longArray;//地图数据
    private int mapBit;//存储的位大小
    private long max;

    public MCSubChunk(long[] longArray, int longLength, int mapBit) {
        this.longArray = new long[longLength];
        System.arraycopy(longArray, 0, this.longArray, 0, longArray.length);
        this.mapBit = mapBit;
        max = (int) Math.pow(2, mapBit) - 1;
    }

    public MCSubChunk(long[] longArray, int mapBit) {
        this.longArray = longArray;
        this.mapBit = mapBit;
        max = (int) Math.pow(2, mapBit) - 1;
    }

    public MCSubChunk(int longLength, int mapBit) {
        this.longArray = new long[longLength];
        this.mapBit = mapBit;
        max = (int) Math.pow(2, mapBit) - 1;
    }

    /**
     * 获取Long数组
     *
     * @return 地图数据
     */
    public long[] getLongArray() {
        return longArray;
    }

    /**
     * 获取地图存储的位大小
     * @return 存储的位大小
     */
    public int getMapBit() {
        return mapBit;
    }

    /**
     * 设置值
     * @param index 索引
     * @param vel 值
     */
    public void set(int index, long vel) {
        if (vel < 0 || vel > max)
            throw new NBTException("设置的值不在范围:" + vel);
        int longIndex = index / (64 / mapBit);
        if (longIndex >= longArray.length)
            throw new NBTException("索引超出范围:" + index);
        int bitIndex = index % (64 / mapBit) * mapBit;
        longArray[longIndex] = longArray[longIndex] & ~(max << bitIndex);
        longArray[longIndex] = (vel << bitIndex) | longArray[longIndex];
    }

    /**
     * 获取值
     * @param index 索引
     * @return 值
     */
    public long get(int index) {
        int longIndex = index / (64 / mapBit);
        if (longIndex >= longArray.length)
            throw new NBTException("索引超出范围:" + index);
        int bitIndex = index % (64 / mapBit) * mapBit;
        long b = (longArray[longIndex] & (max << bitIndex)) >> bitIndex;
        if (b < 0)
            return 0;
        return b;
    }

}
