package Steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class Image {

    protected BufferedImage image;
    protected Color[][] pixels;
    private int[] dimensions = new int[4];

    //loads an image returns true if successful or false if exception was thrown
    public String LoadImage(File file){
        try {
            if(!file.canRead()) return ("Path does not exist");
            image = ImageIO.read(file);
            if(ProcessImage()) return null;
            else return "System failed to retrieve pixels";

        } catch (IOException e) {
            return ("Failed to read the image at " + file + "\n" + e);
        }
    }

    //Sets the dimensions of the image and creates the array of pixels
    private boolean ProcessImage(){

        if(image == null) return false;

        dimensions = new int[]{0, 0, image.getWidth(), image.getHeight()};
        pixels = new Color[dimensions[2]][dimensions[3]];

        try {
            for(int x = 0; x<dimensions[2]; x++){
                for(int y = 0; y<dimensions[3]; y++){
                    int pixel = image.getRGB(x,y);
                    pixels[x][y] = new Color(pixel, true);
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    protected boolean SaveImage(String path, String name){
        try{
            File file = new File(path + "/" + name);
            ImageIO.write(image, ".png", file);
            return true;

        }catch(IOException e){
            System.out.println("Failed to save the image at " + path + "/" + name);
            System.out.println(e);
            return false;
        }
    }

    protected int GetNumPixels(){
        int count = 0;
        for(int i = 0; i< pixels.length; i++)
            count += pixels[i].length;
        return count;
    }

    public Color GetPixel(int x, int y){ return pixels[x][y]; }

    public int GetRedPixel(int x, int y){ return pixels[x][y].getRed(); }

    public int GetGreenPixel(int x, int y){ return pixels[x][y].getGreen(); }

    public int GetBluePixel(int x, int y){ return pixels[x][y].getBlue(); }

    public int GetAlphaPixel(int x, int y){ return pixels[x][y].getAlpha(); }

    public BufferedImage getImage(){ return image;}

}
