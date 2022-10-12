package main.mc;

import main.Utils.BytesUtils;

import java.awt.*;

/*
 * 资料:
 * https://minecraft.fandom.com/zh/wiki/%E5%9C%B0%E5%9B%BE%E7%89%A9%E5%93%81%E6%A0%BC%E5%BC%8F
 * */
public class MCMapColors {
    //MC地图颜色索引
    public static final Color[] mapColors = {
            new Color(0, 0, 0, 0),
            new Color(127, 178, 56),
            new Color(247, 233, 163),
            new Color(199, 199, 199),
            new Color(255, 0, 0),
            new Color(160, 160, 255),
            new Color(167, 167, 167),
            new Color(0, 124, 0),
            new Color(255, 255, 255),
            new Color(164, 168, 184),
            new Color(151, 109, 77),
            new Color(112, 112, 112),
            new Color(64, 64, 255),
            new Color(143, 119, 72),
            new Color(255, 252, 245),
            new Color(216, 127, 51),
            new Color(178, 76, 216),
            new Color(102, 153, 216),
            new Color(229, 229, 51),
            new Color(127, 204, 25),
            new Color(242, 127, 165),
            new Color(76, 76, 76),
            new Color(153, 153, 153),
            new Color(76, 127, 153),
            new Color(127, 63, 178),
            new Color(51, 76, 178),
            new Color(102, 76, 51),
            new Color(102, 127, 51),
            new Color(153, 51, 51),
            new Color(25, 25, 25),
            new Color(250, 238, 77),
            new Color(92, 219, 213),
            new Color(74, 128, 255),
            new Color(0, 217, 58),
            new Color(129, 86, 49),
            new Color(112, 2, 0),
            new Color(209, 177, 161),
            new Color(159, 82, 36),
            new Color(149, 87, 108),
            new Color(112, 108, 138),
            new Color(186, 133, 36),
            new Color(103, 117, 53),
            new Color(160, 77, 78),
            new Color(57, 41, 35),
            new Color(135, 107, 98),
            new Color(87, 92, 92),
            new Color(122, 73, 88),
            new Color(76, 62, 92),
            new Color(76, 50, 35),
            new Color(76, 82, 42),
            new Color(142, 60, 46),
            new Color(37, 22, 16),
            new Color(189, 48, 49),
            new Color(148, 63, 97),
            new Color(92, 25, 29),
            new Color(22, 126, 134),
            new Color(58, 142, 140),
            new Color(86, 44, 62),
            new Color(20, 180, 133),
            new Color(86, 86, 86),
            new Color(186, 150, 126)
    };

    //相关联的地图色 乘数
    public static final float[] relatedColor = {
            0.71f,
            0.86f,
            1f,
            0.53f
    };

    /**
     * 计算颜色差
     *
     * @param color  比对颜色1
     * @param color2 比对颜色2
     * @return 颜色差
     */
    public static int compareColor(Color color, Color color2) {
        return Math.abs(color2.getRed() - color.getRed()) + Math.abs(color2.getGreen() - color.getGreen()) + Math.abs(color2.getBlue() - color.getBlue());
    }

/*   HSB计算色差   （效果不咋滴  废弃）
    public static int compareColor(Color color, Color color2) {
        float[] HSB = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float[] HSB2 = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);

        HSB[0] = (Math.abs(HSB[0] - HSB2[0])) * 1000f;
        HSB[1] = (Math.abs(HSB[1] - HSB2[1])) * 100f;
        HSB[2] = (Math.abs(HSB[2] - HSB2[2])) * 100f;

        return (int) (HSB[0] + HSB[1] + HSB[2]);
    }
*/

    /**
     * 颜色乘
     *
     * @param color 欲被乘的颜色
     * @param n     被乘的小数
     * @return 乘后的颜色
     */
    public static Color multiplyColor(Color color, float n) {
        return new Color((int) (color.getRed() * n), (int) (color.getGreen() * n), (int) (color.getBlue() * n));
    }

    /**
     * 颜色到字节
     *
     * @param color 颜色
     * @return 颜色字节
     */
    public static byte color2byte(Color color) {
        byte iMin = 0;
        byte jMin = 0;
        int colorDeviation = 16581375;
        for (byte i = 0; i < 61; i++) {//所有色
            for (byte j = 0; j < 4; j++) {//关联色
                int Deviation = compareColor(color, multiplyColor(mapColors[i], relatedColor[j]));
                if (Deviation < colorDeviation) {
                    colorDeviation = Deviation;
                    iMin = i;
                    jMin = j;
                }
            }
        }
        return (byte) ((iMin * 4 + jMin));
    }

    /**
     * 字节到颜色
     *
     * @param color 颜色字节
     * @return 返回颜色
     */
    public static Color byte2color(byte color) {
        return multiplyColor(mapColors[(color & 0xFF) / 4], relatedColor[(color & 0xFF) % 4]);
    }


}
