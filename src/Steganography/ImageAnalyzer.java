package Steganography;

import java.awt.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ImageAnalyzer extends Image {

    private List<String> report = new ArrayList<>();

    //Finds each value of the ARGB pixel then increments the array[x] where x is the pixels value
    //The goal is to find how often a pixel value occurs in the image
    public String PixelDensity(){

        int[][] coefficients = new int[4][256];
        float[][] coefficientDensity = new float[4][256];
        float[] percent = new float[4];
        float pixelProb = (float)GetNumPixels()/256;
        int count = 0;
        String[] str = {"", "", "", ""};

        for (Color[] array: pixels) {
            for(Color pixel: array) {
                coefficients[0][pixel.getAlpha()]++;
                coefficients[1][pixel.getRed()]++;
                coefficients[2][pixel.getGreen()]++;
                coefficients[3][pixel.getBlue()]++;
            }
        }

        //Calculates density of pixels and saves the outputs
        for(int i = 0; i<4; i++) {
            for(int j = 0; j<256; j++) {
                coefficientDensity[i][j] = coefficients[i][j] / pixelProb;
                str[i] += (int)(coefficientDensity[i][j]*100) + "% ";
            }
        }

        //Saves the output values to the report
        report.add("The density of each pixel in the range 0-255. This is the Zscore from the mean values. Can be though of as Occurrence/Average\r\n");
        report.add("\r\n-----Alpha Pixels-----\r\n");
        report.add(str[0] + "\r\n");
        report.add("\r\n-----Red Pixels-----\r\n");
        report.add(str[1] + "\r\n");
        report.add("\r\n-----Green Pixels-----\r\n");
        report.add(str[2] + "\r\n");
        report.add("\r\n-----Blue Pixels-----\r\n");
        report.add(str[3] + "\r\n");

        //Check Alpha values for nearest neighbor anomalies. The alpha values should not be irregular
        for(int i = 1; i<255; i++) {
            if (coefficientDensity[0][i] == coefficientDensity[0][i + 1]) count++;
        }

        percent[0] = (float)((float)count/2.56);
        report.add("\r\nAlpha was found to be " + percent[0] + "% consistent. The closer this is to 0; the higher chance of hidden message");

        //Checks pixel values for cluster anomalies. The image should be relatively similar in terms of pixel transitions
        for(int i = 1; i<4; i++){
            for(int j = 0; j<255; j++){
                percent[i] += Math.abs(coefficientDensity[i][j]-coefficientDensity[i][j+1]);
            }
            percent[i] = percent[i]/256;
        }

        report.add("\r\nThe following values represent the percentage of clustering within pixel values. Most images should cluster pixels.");
        report.add("Higher clustering will be closer to 0. The Closer to 1 this value is, the higher chance of a hidden message");
        report.add("Red: " + percent[1] + "\r\nGreen: " + percent[2] + "\r\nBlue: " + percent[3]);

        float probability =  ((1-(percent[0]/100)) + percent[1] + percent[2] + percent[3])*25;
        return "The calculated probability of a hidden message based off pixel density = " + (int)probability + "%.";
    }

    //Finds the Average pixel transitions for RGB as an int
    public String Smoothness() {

        int[] valA = {0, 0, 0};
        int[] valB = {0, 0, 0};
        long[] smooth =  {0, 0, 0};
        int avSmooth;
        float f;

        //Sums the absolute value of the 'jumps'
        for (Color[] array: pixels) {
            for(Color pixel: array) {
                valA[0] = pixel.getRed();
                valA[1] = pixel.getGreen();
                valA[2] = pixel.getBlue();
                smooth[0] += Math.abs(valA[0] - valB[0]);
                smooth[1] += Math.abs(valA[1] - valB[1]);
                smooth[2] += Math.abs(valA[2] - valB[2]);
                valB[0] = valA[0];
                valB[1] = valA[1];
                valB[2] = valA[2];
            }
        }

        //Since the first addition was from 0 and not real pixels we must subtract them from our running total
        smooth[0] -= pixels[0][0].getRed();
        smooth[1] -= pixels[0][0].getBlue();
        smooth[2] -= pixels[0][0].getGreen();

        //Divides by the total number of pixels to get an average
        smooth[0] /= GetNumPixels();
        smooth[1] /= GetNumPixels();
        smooth[2] /= GetNumPixels();

        report.add("\r\nThe average 'jump' value for each RGB pixel is as follows");
        report.add("Red:   " + smooth[0]);
        report.add("Green: " + smooth[1]);
        report.add("Blue:  " + smooth[2]);

        report.add("\r\nThe average smoothness for each RGB component on a scale of 0-100%:");
        f = 1-((float)smooth[0]/256);
        report.add("Red: " + (int)(f*100) + "%.");
        f = 1-((float)smooth[1]/256);
        report.add("Green: " + (int)(f*100) + "%.");
        f = 1-((float)smooth[2]/256);
        report.add("Blue: " + (int)(f*100) + "%.");


        //Formula is derived as first divide by 3 for each RGB component
        //Then we must divide by 256 for each possible pixel value
        //We must then subtract by 1 to get the inverse since we defined this value as "smoothness"
        //Finally we multiply by 100 to get this in a percent form
        f = (((float)smooth[0] + (float)smooth[1] + (float)smooth[2])/(3*256));
        avSmooth = (int) (f*100);
        report.add("\nOverall average chance this image is compromised based on smoothness from 0-100% = " + avSmooth + "%");

        return "The average chance this image is compromised based on smoothness from 0-100% = " + avSmooth + "%.";
    }


    public void SaveReport() {

        try {
            FileWriter file = new FileWriter("report.txt");
            for(String str: report)
                file.write(str + System.lineSeparator());
            file.close();
        }catch(Exception e){ Popup.P("Failure to create report.txt.\n" + e);}

        //Clears the old report for new data
        report = new ArrayList<>();
    }

    public void SaveReport(String name) {

        try {
            FileWriter file = new FileWriter(name + ".txt");
            for(String str: report)
                file.write(str + System.lineSeparator());
            file.close();
        }catch(Exception e){ Popup.P("Failure to create " + name + ".txt.\n" + e);}

        //Clears the old report for new data
        report = new ArrayList<>();
    }
}
