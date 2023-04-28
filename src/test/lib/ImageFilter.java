package text.lib;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageFilter {
    public BufferedImage bufferedImage;

    public ImageFilter(Dimension size) {
        bufferedImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_4BYTE_ABGR);
    }

    public ImageFilter(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;

    }

    public ImageFilter(String imgPath) throws IOException {
        bufferedImage = ImageIO.read(new File(imgPath));
    }

    public ImageFilter(Image image) {
        bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        bufferedImage.createGraphics().drawImage(image, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
        //this.image = image;

    }

    //缩放图片
    public text.lib.ImageFilter Zoom(Dimension size) {
        BufferedImage bfimg = new BufferedImage(size.width, size.height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = bfimg.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(bufferedImage, 0, 0, size.width, size.height, null);
        bfimg.getGraphics().dispose();

        bufferedImage = bfimg;
        return this;
    }

    //修改画布
    public text.lib.ImageFilter ChangeCanvas(Point Pos, Dimension size) {
        BufferedImage bfimg = new BufferedImage(size.width, size.height, BufferedImage.TYPE_4BYTE_ABGR);
        bfimg.getGraphics().drawImage(bufferedImage, Pos.x, Pos.y, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
        bfimg.getGraphics().dispose();
        bufferedImage = bfimg;
        return this;
    }


    public text.lib.ImageFilter DrawIMG(text.lib.ImageFilter img, Point pos, Dimension size) {
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(img.bufferedImage, pos.x, pos.y,size.width,size.height, null);
        g2d.dispose();
        return this;
    }

    //改透明度
    public text.lib.ImageFilter ChangeAllAlpha(int Alpha) {
        BufferedImage bfimg = new BufferedImage(bufferedImage.getWidth(null), bufferedImage.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        bfimg.getGraphics().drawImage(bufferedImage, 0, 0, bfimg.getWidth(), bfimg.getHeight(), null);
        //
        for (int y = 0; y < bfimg.getHeight(); y++) {
            for (int x = 0; x < bfimg.getWidth(); x++) {
                Color color = new Color(bfimg.getRGB(x, y), true);

                //System.out.println(color.getAlpha());
                if (color.getAlpha() > Alpha) {
                    bfimg.setRGB(x, y, new Color(color.getRed(), color.getGreen(), color.getBlue(), Alpha).getRGB());
                    //bfimg.setRGB(x, y, new Color(Alpha,0,0,255).getRGB());
                }

            }
        }
        bfimg.getGraphics().dispose();
        bufferedImage = bfimg;
        return this;
    }


    //与另一张图片进行比较 颜色相同Alpha
    public text.lib.ImageFilter CompareColor(text.lib.ImageFilter img) {
        BufferedImage bfimg = new BufferedImage(bufferedImage.getWidth(null), bufferedImage.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        bfimg.getGraphics().drawImage(bufferedImage, 0, 0, bfimg.getWidth(), bfimg.getHeight(), null);

        for (int y = 0; y < bfimg.getHeight(); y++) {
            for (int x = 0; x < bfimg.getWidth(); x++) {
                Color color = new Color(bfimg.getRGB(x, y), true);
                Color color2 = new Color(img.bufferedImage.getRGB(x, y), true);
                int r = 3;
                //System.out.println(color.getAlpha());
                if (color.getRed() - color2.getRed() < r) {
                    if (color.getGreen() - color2.getGreen() < r)
                        if (color.getBlue() - color2.getBlue() < r)
                            bfimg.setRGB(x, y, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0).getRGB());

                }

            }
        }
        bfimg.getGraphics().dispose();
        bufferedImage = bfimg;
        return this;
    }


    //截取图片
    public text.lib.ImageFilter Intercept(Rectangle rectangle) {
        BufferedImage bfimg = new BufferedImage(rectangle.width, rectangle.height, BufferedImage.TYPE_4BYTE_ABGR);
        bfimg.getGraphics().drawImage(bufferedImage, -rectangle.x, -rectangle.y, bufferedImage.getWidth(null), bufferedImage.getHeight(null), null);
        bfimg.getGraphics().dispose();
        bufferedImage = bfimg;
        return this;
    }

    //写文字
    public text.lib.ImageFilter WriteString(Point point, String str, Font font, Color color) {
        Graphics2D g2d = bufferedImage.createGraphics();
        FontMetrics fm = g2d.getFontMetrics(font);
        int widthx = (bufferedImage.getWidth() - fm.stringWidth(str)) / 2;
        bufferedImage = WriteString(str, font, color, new Point(widthx, (bufferedImage.getHeight()) / 2 + font.getSize() / 4)).bufferedImage;
        g2d.dispose();
        return this;
    }

    public text.lib.ImageFilter WriteString(String str, Font font, Color color, Point p) {
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GlyphVector v = font.createGlyphVector(g2d.getFontRenderContext(), str);
        Shape shape = v.getOutline();
        g2d.translate(p.x, p.y);
        g2d.setColor(color);
        g2d.fill(shape);
        return this;
    }

    public text.lib.ImageFilter WriteString(String str, Font font, Color color, Point p, Color OutlineColor, float OutlineSize) {
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GlyphVector v = font.createGlyphVector(g2d.getFontRenderContext(), str);
        Shape shape = v.getOutline();
        g2d.translate(p.x, p.y);
        g2d.setColor(color);
        g2d.fill(shape);


        g2d.setColor(OutlineColor);
        g2d.setStroke(new BasicStroke(OutlineSize));
        g2d.draw(shape);
        g2d.dispose();
        //g2d.drawString(str, 0,0);
        return this;
    }

    //抠图
    public text.lib.ImageFilter ChangeBlackAlpha(int Alpha) {
        BufferedImage bfimg = new BufferedImage(bufferedImage.getWidth(null), bufferedImage.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        bfimg.getGraphics().drawImage(bufferedImage, 0, 0, bfimg.getWidth(), bfimg.getHeight(), null);
        for (int y = 0; y < bfimg.getHeight(); y++) {
            for (int x = 0; x < bfimg.getWidth(); x++) {
                Color color = new Color(bfimg.getRGB(x, y), true);
                if (color.getAlpha() > Alpha) {
                    bfimg.setRGB(x, y, new Color(255, 255, 255, 255).getRGB());
                    //bfimg.setRGB(x, y, new Color(Alpha,0,0,255).getRGB());
                }

            }
        }
        return this;
    }

    public void WriteFile(File file) throws IOException {
        ImageIO.write(bufferedImage, "png", file);
    }

    public void WriteFile(String file) throws IOException {
        WriteFile(new File(file));
    }

}
