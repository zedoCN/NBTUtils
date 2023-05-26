package test;
/**
 * 测试-obj模型生成
 * 枚举obj模型的所有三角面，读取材质贴图颜色找到最接近的方块进行生成
 */

import de.javagl.obj.*;
import main.mc.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test_OBJ {
    public static void main(String[] args) {
        try {

            MCRegion mcRegion = new MCRegion(new File("D:\\MineCraft\\MinecraftAll\\.minecraft\\versions\\1.19.4-OptiFine_I4\\saves\\新的世界\\region"), 10);
            mcRegion.setGenerateChunk(mcRegion.getChunk(new MCPosInt(0, 0)));

            File objFile = new File("D:\\Projects\\Github\\OBJ模型\\星穹铁道-主控舱段_by_小海新不恋爱\\星穹铁道-主控舱段_by_小海新不恋爱_细节.obj");


            MCBlockColors mcBlockColors = new MCBlockColors(new File("D:\\Projects\\Github\\NBTUtils\\src\\test\\mc_res\\bloks"));


            /*//测试画板
            MyImageFilter myImageFilter = new MyImageFilter(new Dimension(200, 200));*/


            Obj originalObj = ObjReader.read(new FileReader(objFile));

            //MCBlocksCollective blocks = new MCBlocksCollective(new MCPosInt(1024, 370, 1024));

            //三角化
            Obj obj = ObjUtils.convertToRenderable(originalObj);
            //Obj obj = originalObj;
            //读取材质文件
            List<Mtl> allMtls =
                    MtlReader.read(new FileReader(
                            objFile.getParent() + "\\" + obj.getMtlFileNames().get(0).substring(2)
                    ));

            //按材质分割obj
            Map<String, Obj> materialGroups =
                    ObjSplitting.splitByMaterialGroups(obj);
            //System.out.println("getNumGroups:" + obj.getGroup(0).);
            for (Map.Entry<String, Obj> entry : materialGroups.entrySet()) {
                String materialName = entry.getKey();
                Obj materialGroup = entry.getValue();
                System.out.println("材质名:" + materialName);
                Mtl material = findMtlForName(allMtls, materialName);
                FloatTuple kdColor = material.getKd();
                String materialPath = material.getMapKd();
                materialPath = (materialPath == null ? "" : materialPath);
                File materialFile = new File(objFile.getParent() + "\\" + materialPath.replaceAll("volume://", ""));
                System.out.println("纹理图片:" + materialFile);
                System.out.println("面数:" + materialGroup.getNumFaces());

                BufferedImage materialImg = null;

                if (materialFile.isFile()) {
                    //读取纹理文件
                    materialImg = ImageIO.read(materialFile);
                } else {
                    System.out.println("未找到纹理文件");
                }
                //枚举所有面
                for (int i = 0; i < materialGroup.getNumFaces(); i++) {
                    //System.out.println("面索引:" + i);
                    ObjFace face = materialGroup.getFace(i);//获取面
                    FloatTuple[] faceVertex = new FloatTuple[3];
                    FloatTuple[] faceUV = new FloatTuple[3];

                    //枚举所有顶点
                    for (int j = 0; j < 3; j++) {
                        faceVertex[j] = materialGroup.getVertex(face.getVertexIndex(j));//获取顶点坐标
                        if (materialImg != null) {
                            faceUV[j] = materialGroup.getTexCoord(face.getTexCoordIndex(j));//获取uv坐标
                        }
                    }


                    //顶点边
                    List<MCPosInt> side1 = new ArrayList<>();//取边1
                    List<MCPosInt> side2 = new ArrayList<>();//取边2
                    MCPosInt.enumLinePos(f2m(faceVertex[0]), f2m(faceVertex[1]), 10f, p -> {
                        side1.add(p);
                    });
                    MCPosInt.enumLinePos(f2m(faceVertex[0]), f2m(faceVertex[2]), 10f, p -> {
                        side2.add(p);
                    });

                    if (side1.size() == 0 || side2.size() == 0) {
                        side1.add(f2m(faceVertex[0]));
                        side2.add(f2m(faceVertex[0]));
                    }


                    for (int j = 0; j < side1.size(); j++) {


                        int finalJ = j;
                        BufferedImage finalMaterialImg = materialImg;

                        MCPosInt.enumLinePos(side1.get(j),
                                side2.get((int) (((float) side2.size() / side1.size() * j))),
                                4f,
                                (p, d) -> {


                                    //myImageFilter.bufferedImage.setRGB(p.x, p.z, color.getRGB());
                                    Color color;
                                    if (finalMaterialImg != null) {
                                        FloatTuple UV1 = in2Pos((faceUV[0]), (faceUV[1]), 1f / side1.size() * finalJ);
                                        FloatTuple UV2 = in2Pos((faceUV[0]), (faceUV[2]), 1f / side1.size() * finalJ);

                                        FloatTuple UV3 = in2Pos(UV1, UV2, d);


                                        int UVx = Math.round(finalMaterialImg.getWidth() * UV3.getX());
                                        int UVy = Math.round(finalMaterialImg.getHeight() * (1f - UV3.getY()));
                                        if ((UVx < 0 || UVx >= finalMaterialImg.getWidth()) || (UVy < 0 || UVy >= finalMaterialImg.getHeight())) {

                                            //color = Color.CYAN;
                                            //System.out.println("纹理过界 原大小>W:" + finalMaterialImg.getWidth() + " H:" + finalMaterialImg.getHeight());
                                            //System.out.println("UVxy> x:" + UVx + " y:" + UVy);

                                            if (UVx < 0)
                                                UVx = UVx + finalMaterialImg.getWidth() * (-UVx / finalMaterialImg.getWidth() + 1);
                                            if (UVy < 0)
                                                UVy = UVy + finalMaterialImg.getHeight() * (-UVy / finalMaterialImg.getHeight() + 1);

                                            UVx = UVx % finalMaterialImg.getWidth();
                                            UVy = UVy % finalMaterialImg.getHeight();
                                            //System.out.println("w:" + finalMaterialImg.getWidth() + " h:" + finalMaterialImg.getHeight());
                                            //System.out.println("UVx:" + UVx + " UVy:" + UVy);


                                            color = new Color(finalMaterialImg.getRGB(
                                                    UVx,
                                                    UVy));


                                        } else {
                                            color = new Color(finalMaterialImg.getRGB(
                                                    UVx,
                                                    UVy));
                                        }

                                    } else {
                                        if (kdColor != null)//使用漫反射颜色
                                            color = new Color(kdColor.get(0), kdColor.get(1), kdColor.get(2));
                                        else//没有颜色 设置紫色
                                            color = Color.MAGENTA;
                                    }

                                    MCBlockColors.BlockColor blockColor = mcBlockColors.colorFindBlock(color);

                                    try {
                                        //System.out.println(MCPosInt.additive(p,new MCPosInt(10240,-50,0)).toStr());
                                        mcRegion.setBlock(MCPosInt.additive(p, new MCPosInt(81920, -59, 0)), new MCBlock("minecraft:" + blockColor.name));
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    }

                }
            }
            //mcRegion.setBlocksCollective(new MCPosInt(10434, -60, 10434), blocks);
            mcRegion.saveMCA();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 材质名找材质
     *
     * @param mtls 材质
     * @param name 材质名
     * @return 材质
     */
    private static Mtl findMtlForName(Iterable<? extends Mtl> mtls, String name) {
        for (Mtl mtl : mtls) {
            if (mtl.getName().equals(name)) {
                return mtl;
            }
        }
        return null;
    }

    /**
     * 取两坐标的中点
     *
     * @param p1 坐标A
     * @param p2 坐标B
     * @param i  0.-1.
     * @return 中点坐标
     */
    public static FloatTuple in2Pos(FloatTuple p1, FloatTuple p2, float i) {
        return FloatTuples.create(((p1.getX() - (p1.getX() - p2.getX()) * i)),
                ((p1.getY() - (p1.getY() - p2.getY()) * i)),
                ((p1.getZ() - (p1.getZ() - p2.getZ()) * i)));
    }

    /**
     * 坐标到mc坐标
     *
     * @param floatTuple obj坐标
     * @return mc坐标
     */
    static MCPosInt f2m(FloatTuple floatTuple) {
        return new MCPosInt(Math.round(floatTuple.getX()), Math.round(floatTuple.getY()), Math.round(floatTuple.getZ()));
    }


}
