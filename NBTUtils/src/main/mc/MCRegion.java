package main.mc;

import main.exception.NBTException;
import main.io.MCUtil;
import main.nbt.CompoundTag;
import main.nbt.ListTag;

import java.io.File;
import java.io.IOException;


public class MCRegion {
    public static final short SAVEMODE_RewriteAll = 0;//0全部重写 稳定 需要退出存档
    public static final short SAVEMODE_RewritePart = 1;//1局部重写 速度快 只需区块不被加载就能写
    private int saveMode = SAVEMODE_RewritePart;//保存模式 默认为局部重写
    private File mcaFileDirectory;//MCA文件目录的路径
    private MCA[] mca;//当前打开的MCA
    private int mcaCache = 3;//用于缓存mca文件 加快反复横跳速度
    private int mcaCacheNumber = 0;//已存在的缓存数量
    private MCChunk generateChunk = null;//自动生成区块 适合超平坦使用

    /**
     * @param mcaFileDirectory mca文件夹路径
     * @throws IOException
     */
    public MCRegion(File mcaFileDirectory) throws IOException {
        if (!mcaFileDirectory.isDirectory()) {
            throw new NBTException("需要一个文件夹路径");
        }
        this.mcaFileDirectory = mcaFileDirectory;
    }

    /**
     * @param mcaFileDirectory mca文件夹路径
     * @param mcaCache         缓存数量 默认为3
     * @throws IOException
     */
    public MCRegion(File mcaFileDirectory, int mcaCache) throws IOException {
        if (!mcaFileDirectory.isDirectory()) {
            throw new NBTException("需要一个文件夹路径");
        }
        this.mcaFileDirectory = mcaFileDirectory;
        this.mcaCache = mcaCache;
    }

    /**
     * 设置保存模式
     *
     * @param saveMode 保存模式 默认为局部重写 请使用"SAVEMODE_xx"
     */
    public void setSaveMode(int saveMode) {
        this.saveMode = saveMode;
    }

    /**
     * 设置生成区块  遇到空区块时会自动创建区块 为空时开摆
     *
     * @param generateChunk 要自动生成的区块
     */
    public void setGenerateChunk(MCChunk generateChunk) {
        this.generateChunk = generateChunk;
    }


    /**
     * 保存所有MCA文件
     *
     * @throws IOException
     */
    public void saveMCA() throws IOException {
        for (int i = 0; i < mcaCacheNumber; i++) {
            saveMCA(i);
        }
    }

    /**
     * 保存MCA文件
     *
     * @param index 缓存索引
     * @throws IOException
     */
    private void saveMCA(int index) throws IOException {
        System.out.println("保存mca:" + mca[index].Pos.toStr());
        if (saveMode == SAVEMODE_RewriteAll) {
            MCUtil.writeMCAFile(mca[index], mcaFileDirectory);
        } else if (saveMode == SAVEMODE_RewritePart) {
            MCUtil.writeMCAFile2(mca[index], mcaFileDirectory);
        }
    }

    /**
     * 区块坐标查mca缓存 如果没有则读取mca
     *
     * @param xz 区块绝对坐标
     * @return 缓存索引
     * @throws IOException
     */
    private int checkMCA(MCPosInt xz) throws IOException {
        MCPosInt mcaPos = MCPosInt.chunk2regionPos(xz);//转换坐标
        int chunkIndex = MCPosInt.chunk2chunkIndex(xz);
        if (mca == null) {//初始化
            mca = new MCA[mcaCache];
            mca[0] = MCUtil.readMCAFile(mcaFileDirectory, mcaPos);
            mcaCacheNumber = 1;
            System.out.println("载入mca:" + mcaPos.toStr());
            mca[0].chunkTimeStamp[chunkIndex] = System.currentTimeMillis();//修改时间戳
            mca[0].chunksFlag[chunkIndex] = true;
            if (generateChunk != null)//自动生成区块
                if (mca[0].chunksNBT[chunkIndex] == null) {
                    mca[0].chunksNBT[chunkIndex] = generateChunk.cloneChunk().chunk;
                }
            return 0;
        }

        //查找
        for (int i = 0; i < mcaCacheNumber; i++) {
            if (mca[i].Pos.isEquals(mcaPos)) {
                mca[i].chunkTimeStamp[chunkIndex] = System.currentTimeMillis();//修改时间戳
                mca[i].chunksFlag[chunkIndex] = true;
                if (generateChunk != null)//自动生成区块
                    if (mca[i].chunksNBT[chunkIndex] == null)
                        mca[i].chunksNBT[chunkIndex] = generateChunk.cloneChunk().chunk;
                return i;
            }
        }

        //保存末尾
        if (mcaCacheNumber < mcaCache) {
            mcaCacheNumber++;
        } else {
            saveMCA(mcaCache - 1);
        }

        //移动
        System.arraycopy(mca, 0, mca, 1, mcaCache - 1);

        //载入
        mca[0] = MCUtil.readMCAFile(mcaFileDirectory, mcaPos);
        System.out.println("载入mca:" + mcaPos.toStr());

        mca[0].chunkTimeStamp[chunkIndex] = System.currentTimeMillis();//修改时间戳
        mca[0].chunksFlag[chunkIndex] = true;
        if (generateChunk != null)//自动生成区块
            if (mca[0].chunksNBT[chunkIndex] == null)
                mca[0].chunksNBT[chunkIndex] = generateChunk.cloneChunk().chunk;
        return 0;
    }


    /**
     * 获取方块状态
     *
     * @param xyz 方块绝对坐标
     * @return 新的Tag对象
     * @throws IOException
     */
    public CompoundTag getBlockState(MCPosInt xyz) throws IOException {
        int index = checkMCA(MCPosInt.pos2chunk(xyz));
        MCChunk chunk = mca[index].getChunk(MCPosInt.pos2chunkIndex(xyz));
        return chunk.getBlockState(MCPosInt.pos2blockIndex(xyz), MCPosInt.pos2subChunkY(xyz.y)).clone();
    }

    /**
     * 设置方块状态
     *
     * @param xyz        方块绝对坐标
     * @param blockState 方块状态
     * @throws IOException
     */
    public void setBlockState(MCPosInt xyz, CompoundTag blockState) throws IOException {
        int index = checkMCA(MCPosInt.pos2chunk(xyz));
        MCChunk chunk = mca[index].getChunk(MCPosInt.pos2chunkIndex(xyz));
        chunk.setBlockState(MCPosInt.pos2blockIndex(xyz), MCPosInt.pos2subChunkY(xyz.y), blockState);
    }

    /**
     * 获取方块实体
     *
     * @param xyz 方块绝对坐标
     * @return 方块实体标签
     * @throws IOException
     */
    public CompoundTag getBlockEntitie(MCPosInt xyz) throws IOException {
        int index = checkMCA(MCPosInt.pos2chunk(xyz));
        MCChunk chunk = mca[index].getChunk(MCPosInt.pos2chunkIndex(xyz));
        return chunk.getBlockEntitie(xyz);
    }

    /**
     * 设置方块实体 直接覆盖
     *
     * @param xyz          方块绝对坐标
     * @param blockEntitie 方块实体标签
     * @throws IOException
     */
    public void setBlockEntitie(MCPosInt xyz, CompoundTag blockEntitie) throws IOException {
        int index = checkMCA(MCPosInt.pos2chunk(xyz));
        MCChunk chunk = mca[index].getChunk(MCPosInt.pos2chunkIndex(xyz));
        chunk.setBlockEntitie((xyz), blockEntitie);
    }

    /**
     * 获取一个区块
     *
     * @param xz 区块绝对坐标
     * @return 区块对象
     * @throws IOException
     */
    public MCChunk getChunk(MCPosInt xz) throws IOException {
        int index = checkMCA(xz);
        return mca[index].getChunk(MCPosInt.chunk2chunkIndex(xz)).cloneChunk();
    }

    /**
     * 设置区块
     *
     * @param xz    区块绝对坐标
     * @param chunk 要设置的区块对象
     * @throws IOException
     */
    public void setChunk(MCPosInt xz, MCChunk chunk) throws IOException {
        int index = checkMCA(xz);
        chunk.chunk.getCompoundTag("").setTag("xPos", xz.x).setTag("zPos", xz.z);
        ListTag blockEntities = chunk.chunk.getCompoundTag("").getListTag("block_entities");
        if (blockEntities != null) {
            for (int i = 0; i < blockEntities.size(); i++) {
                CompoundTag tag = blockEntities.getCompoundTag(i);
                MCPosInt blockPos = new MCPosInt((int) tag.getTag("x"), (int) tag.getTag("z"));
                blockPos.x = blockPos.x % 16 + xz.x * 16;
                blockPos.z = blockPos.z % 16 + xz.z * 16;
                tag.setTag("x", blockPos.x);
                tag.setTag("z", blockPos.z);
            }
        }
        mca[index].chunksNBT[MCPosInt.chunk2chunkIndex(xz)] = chunk.chunk;
    }

    /**
     * 获取方块
     *
     * @param xyz 要获取的坐标
     * @return 获取到的方块
     * @throws IOException
     */
    public MCBlock getBlock(MCPosInt xyz) throws IOException {
        return new MCBlock(getBlockState(xyz), getBlockEntitie(xyz));
    }

    /**
     * 设置方块
     *
     * @param xyz   要设置的坐标
     * @param block 要设置的方块
     * @throws IOException
     */
    public void setBlock(MCPosInt xyz, MCBlock block) throws IOException {
        setBlockState(xyz, block.blockState);
        if (block.blockEntitie != null)
            setBlockEntitie(xyz, block.blockEntitie);
    }

    /**
     * 获取方块集
     *
     * @param Pos1 坐标1
     * @param Pos2 坐标2
     * @return 方块集
     * @throws IOException
     */
    public MCBlocksCollective getBlocksCollective(MCPosInt Pos1, MCPosInt Pos2) throws IOException {
        MCPosInt origin = new MCPosInt();
        MCPosInt lwh = new MCPosInt();
        MCPosInt.getOrigin(Pos1, Pos2, origin, lwh);
        lwh = MCPosInt.additive(lwh, new MCPosInt(1, 1, 1));
        MCBlocksCollective mcBlocksCollective = new MCBlocksCollective(lwh);

        MCPosInt.iteratePos(lwh, p -> {
            MCPosInt blockPos = MCPosInt.additive(origin, p);
            MCBlock mcBlock = null;
            try {
                mcBlock = getBlock(blockPos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mcBlocksCollective.setBlock(p, mcBlock);
        });


        return mcBlocksCollective;
    }

    /**
     * 设置方块集
     *
     * @param pos                原点坐标
     * @param mcBlocksCollective 方块集
     */
    public void setBlocksCollective(MCPosInt pos, MCBlocksCollective mcBlocksCollective) {
        MCPosInt.iteratePos(mcBlocksCollective.lwh, p -> {
            MCBlock block = mcBlocksCollective.getBlock(p);
            if (block != null)
                try {
                    setBlock(MCPosInt.additive(pos, p), block.clone());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        });
    }
}
