package main.Utils;

import main.Utils.BitsUtils;
import main.Utils.BytesUtils;
import main.io.MCUtil;
import main.mc.MCChunk;
import main.mc.MCPosInt;
import main.nbt.CompoundTag;
import main.nbt.ListTag;

import java.util.BitSet;

public class MCChunkUtils {
    //在地图数据里寻找区块 Y常为-4
    public static MCChunk findChunk(ListTag mapNBT, MCPosInt xyz) {
        for (int i = 0; i < mapNBT.size(); i++) {
            CompoundTag chunkNBT = mapNBT.getCompoundTag(i).getCompoundTag("");
            if ((int) chunkNBT.getTag("xPos") == xyz.x) {
                if ((int) chunkNBT.getTag("yPos") == xyz.y) {
                    if ((int) chunkNBT.getTag("zPos") == xyz.z) {
                        return new MCChunk(chunkNBT);
                    }
                }
            }
        }
        return null;
    }

    //在区块里寻找子区块
    public static CompoundTag findSubChunk(CompoundTag chunk, int Y) {
        ListTag subChunks = chunk.getListTag("sections");
        for (int i = 0; i < subChunks.size(); i++) {
            CompoundTag subChunk = subChunks.getCompoundTag(i);
            if ((byte) subChunk.getTag("Y") == (byte) Y) {
                return subChunk;
            }
        }
        return null;
    }


    //在子区块获取一个方块状态   相对子区块坐标
    public static CompoundTag subChunkFindBlockStates(CompoundTag subChunk, MCPosInt xyz) {
        //findSubChunk(Chunk, (int) Math.floor(xyz.y / 16.0));
        CompoundTag sunChunkBlocks = subChunk.getCompoundTag("block_states");


        //读取区块方块索引
        ListTag blocks = sunChunkBlocks.getListTag("palette");
        //for (int j = 0; j < blocks.size(); j++) {
        //System.out.println("方块索引:" + BitsUtils.bits2fStr(BytesUtils.bytes2bits(new byte[]{(byte) j})) + " ID:" + blocks.getCompoundTag(j).getTagData("Name"));
        //}

        //(int)Math.ceil(4096f/(64/5))  //计算long数组长度

        int mapBit = MCUtil.getMapBitSize(blocks.size());
        int blockIndex = xyz.x + xyz.z * 16 + xyz.y * 256;
        int longIndex = blockIndex / (64 / mapBit);
        int longBlockIndex = blockIndex % (64 / mapBit);

        BitSet blockData = BitSet.valueOf(new long[]{((long[]) sunChunkBlocks.getTag("data"))[longIndex]});
        return blocks.getCompoundTag(BytesUtils.bytes2int(blockData.get(longBlockIndex * mapBit, (longBlockIndex + 1) * mapBit).toByteArray()));

        //for (int j = 0; j < ((long[]) sunChunkBlocks.getTagData("data")).length; j++) {
        //    BitSet blockData = BitSet.valueOf(new long[]{((long[]) sunChunkBlocks.getTagData("data"))[j]});





            /*for (int k = 0; k < 64 / mapBit; k++) {//方块数


                System.out.println("X:" + String.valueOf(xPos) + " Y:" + String.valueOf(yPos) + " Z:" + String.valueOf(zPos) + " Block:" + blocks.getCompoundTag(BytesUtils.bytes2int(blockData.get(k * mapBit, (k + 1) * mapBit).toByteArray())).getTagData("Name") + " Properties:" + blocks.getCompoundTag(BytesUtils.bytes2int(blockData.get(k * mapBit, (k + 1) * mapBit).toByteArray())).getCompoundTag("Properties"));


                xPos++;
                if (xPos > 15) {
                    zPos++;
                    xPos = 0;
                    if (zPos > 15) {
                        yPos++;
                        zPos = 0;
                        if (yPos > 15) {
                            break;
                        }
                    }
                }
            }*/
        // }
        //return null;
    }

    //在区块里获取一个方块实体   绝对世界坐标
    public static CompoundTag chunkFindBlockEntities(CompoundTag chunk, MCPosInt xyz) {
        ListTag chunkBlockEntities = chunk.getListTag("block_entities");
        return getCompoundTag(xyz, chunkBlockEntities);
    }

    public static CompoundTag getCompoundTag(MCPosInt xyz, ListTag chunkBlockEntities) {
        for (int i = 0; i < chunkBlockEntities.size(); i++) {
            CompoundTag blockEntitie = chunkBlockEntities.getCompoundTag(i);
            if ((int) blockEntitie.getTag("x") == xyz.x)
                if ((int) blockEntitie.getTag("y") == xyz.y)
                    if ((int) blockEntitie.getTag("z") == xyz.z)
                        return blockEntitie;
        }
        return null;
    }


}
