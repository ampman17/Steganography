package Steganography;

import java.awt.*;
import java.awt.image.WritableRaster;
import java.io.File;


public class ImageEditor extends Image{

    private WritableRaster raster;

    public String LoadImage(File file){
        String s = this.LoadImage(file);
        if(s != null) raster = image.getRaster();
        return s;
    }

    //TODO
    public void HideMessage(String text){


    }

    //TODO
    public String RetrieveMessage(){
        String message = null;

        return message;
    }

    private void ChangePixel(int x, int y, Color pixel){
        raster.setPixel(x,y, new int[]{pixel.getAlpha(), pixel.getRed(),  pixel.getGreen(), pixel.getBlue()});
    }
}
