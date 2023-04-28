package main.mc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MCBlockColors {
    private ArrayList<BlockColor> blocks = new ArrayList<>();

    public MCBlockColors(File blockImgPath) throws IOException {
        ArrayList<File> FileList = new ArrayList<>();
        File[] files = blockImgPath.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                FileList.add(f);
            }
        }

        for (File file : FileList) {
            BufferedImage img = ImageIO.read(file);
            BufferedImage pix = new BufferedImage(1, 1, img.getType());
            pix.createGraphics().drawImage(img, 0, 0, 1, 1, null);
            blocks.add(new BlockColor(file.getName().substring(0, file.getName().length() - 4), new Color(pix.getRGB(0, 0))));
        }

    }


    public static class BlockColor {
        public String name;
        public Color color;

        public BlockColor(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public int compare(Color color2) {
            return Math.abs(color2.getRed() - color.getRed()) + Math.abs(color2.getGreen() - color.getGreen()) + Math.abs(color2.getBlue() - color.getBlue());
        }
    }

    public BlockColor colorFindBlock(Color color) {
        if (color.getAlpha() == 0)
            return new BlockColor("air", new Color(0, 0, 0, 0));
        int minIndex = 0;
        int min = 255 * 255 * 255;
        for (int i = 0; i < blocks.size(); i++) {
            int c = blocks.get(i).compare(color);
            if (min > c) {
                min = c;
                minIndex = i;

            }

        }
        return blocks.get(minIndex);
    }


}
