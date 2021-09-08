package Steganography;

import javax.swing.JOptionPane;

public class Popup {

    public static void P(String infoMessage)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "Something Went Wrong", JOptionPane.INFORMATION_MESSAGE);
    }
}