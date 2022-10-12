package main.mc;

public class MCPosInt {
    public int x, y, z;

    public MCPosInt() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * 通过xyz创建坐标对象
     *
     * @param x
     * @param y
     * @param z
     */
    public MCPosInt(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 通过xz创建坐标对象
     *
     * @param x
     * @param z
     */
    public MCPosInt(int x, int z) {
        this.x = x;
        this.y = 0;
        this.z = z;
    }

    /**
     * 坐标到字符串
     *
     * @return 格式化后的坐标文本
     */
    public String toStr() {
        return "x:" + x + " y:" + y + " z:" + z;
    }

    /**
     * 世界坐标取区块位置
     *
     * @param xyz 世界绝对坐标
     * @return 区块绝对坐标
     */
    public static MCPosInt pos2chunk(MCPosInt xyz) {
        return new MCPosInt(xyz.x >> 4, xyz.y >> 4, xyz.z >> 4);
    }

    /**
     * 世界坐标取方块索引
     *
     * @param xyz 世界绝对坐标
     * @return 方块索引值
     */
    public static int pos2blockIndex(MCPosInt xyz) {
        return (xyz.x & 0xF) + (xyz.y & 0xF) * 256 + (xyz.z & 0xF) * 16;
    }

    /**
     * 世界坐标Y到子区块Y索引
     *
     * @param y 世界绝对y坐标
     * @return 子区块Y索引
     */
    public static int pos2subChunkY(int y) {
        return y >> 4;
    }

    /**
     * 区块坐标取区块索引
     *
     * @param xz 区块绝对坐标
     * @return 区块索引
     */
    public static int chunk2chunkIndex(MCPosInt xz) {
        return (xz.x & 0x1F) + (xz.z & 0x1F) * 32;
    }


    /**
     * 世界坐标取区块索引
     *
     * @param xz 世界绝对坐标
     * @return 区块索引
     */
    public static int pos2chunkIndex(MCPosInt xz) {
        MCPosInt rel = pos2chunk(xz);
        return chunk2chunkIndex(rel);
    }


    /**
     * 将世界坐标转成mca位置
     *
     * @param xz 世界绝对坐标
     * @return mca文件位置
     */
    public static MCPosInt pos2regionPos(MCPosInt xz) {
        return new MCPosInt(xz.x >> 9, xz.z >> 9);
    }

    /**
     * 将区块位置转成mca位置
     *
     * @param xz 区块位置
     * @return mca文件位置
     */
    public static MCPosInt chunk2regionPos(MCPosInt xz) {
        return new MCPosInt(xz.x >> 5, xz.z >> 5);
    }

    /**
     * 复制个全新的坐标对象
     *
     * @return 新的坐标对象
     */
    public MCPosInt clone() {
        return new MCPosInt(x, y, z);
    }

    /**
     * 两坐标取原点坐标 和 区域大小
     *
     * @param p1     坐标1
     * @param p2     坐标2
     * @param Origin 原点坐标
     * @param LWH    长宽高
     */
    public static void getOrigin(MCPosInt p1, MCPosInt p2, MCPosInt Origin, MCPosInt LWH) {
        LWH.x = Math.abs(p1.x - p2.x);
        LWH.y = Math.abs(p1.y - p2.y);
        LWH.z = Math.abs(p1.z - p2.z);
        Origin.x = Math.min(p1.x, p2.x);
        Origin.y = Math.min(p1.y, p2.y);
        Origin.z = Math.min(p1.z, p2.z);
    }

    /**
     * 将两坐标相加
     *
     * @param p1 坐标1
     * @param p2 坐标2
     * @return 相加后的坐标
     */
    public static MCPosInt additive(MCPosInt p1, MCPosInt p2) {
        return new MCPosInt(p1.x + p2.x, p1.y + p2.y, p1.z + p2.z);
    }


    /**
     * 和坐标比较
     *
     * @param xyz 要比较的坐标
     * @return 是否一样
     */
    public boolean isEquals(MCPosInt xyz) {
        return (xyz.x == x && xyz.y == y && xyz.z == z);
    }

    public static void iteratePos(MCPosInt lwh, callBack cb) {
        for (int y = 0; y < lwh.y; y++) {
            for (int z = 0; z < lwh.z; z++) {
                for (int x = 0; x < lwh.x; x++) {
                    cb.iterate(new MCPosInt(x, y, z));
                }
            }
        }

    }


    public static float getDistance(MCPosInt p1, MCPosInt p2) {
        return (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2) + Math.pow(p2.z - p1.z, 2));
    }


    /**
     * 枚举两坐标直线所有坐标
     *
     * @param p1 坐标1
     * @param p2 坐标2
     * @param s  细分
     * @param p  所有坐标
     */
    public static void enumLinePos(MCPosInt p1, MCPosInt p2, float s, callBack p) {
        enumLinePos(p1, p2, s, (p_, d) -> {
            p.iterate(p_);
        });
    }


    public static void enumLinePos(MCPosInt p1, MCPosInt p2, float s, callBack2 p) {
        float k = (MCPosInt.getDistance(p1, p2)) * s;
        for (float i = 0; i < k; i++) {
            MCPosInt pos = new MCPosInt(
                    Math.round((p1.x - (p1.x - p2.x) / k * i)),
                    Math.round((p1.y - (p1.y - p2.y) / k * i)),
                    Math.round((p1.z - (p1.z - p2.z) / k * i)));
            p.iterate(pos, 1f / k * i);
        }
        if (k == 0) {
            p.iterate(p1, 1);
        }
    }


    public interface callBack {
        void iterate(MCPosInt p);
    }

    public interface callBack2 {
        void iterate(MCPosInt p, float d);
    }

}
