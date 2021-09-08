package Steganography;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.Document;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class StegUI {

    //Filler and panes
    private JFrame frame;
    private JPanel panel;
    private JTabbedPane tabbedPane;
    private JPanel tab1;
    private JPanel tab3;
    private JPanel tab2;


    //Buttons
    private JButton loadAnalyzerButton;
    private JButton singleProcessButton;
    private JButton multiProcessButton;
    private JButton loadEditorButton;
    private JButton embedMessageButton;

    //Inputs
    private JTextField singleFilePath;
    private JTextField MultiFilePath;
    private JTextField editorFilePath;
    private JRadioButton hideMessage;
    private JRadioButton retrieveMessage;


    //Outputs
    private JTextPane output;

    //Local Objects
    private ImageAnalyzer imageAnalyzer;
    private ImageEditor imageEditor;
    private boolean hasImage = false;
    private Document doc;
    private ButtonGroup group = new ButtonGroup();
    private File file;

    public StegUI() {

        group.add(hideMessage);
        group.add(retrieveMessage);
        MakeGui();

        loadAnalyzerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!hasImage) LoadAnalyzer();
                else UnloadFile();
            }
        });

        singleProcessButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (hasImage) DetectSteg();
            }
        });

        multiProcessButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processMulti();
            }
        });

        loadEditorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!hasImage) LoadEditor();
                else UnloadFile();
            }
        });
        embedMessageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (hasImage) editImage();
            }
        });

    }

    private void MakeGui() {
        frame = new JFrame("Steganography tool by Josiah Allard & Eric Bothello");
        frame.setSize(700, 650);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        doc = output.getDocument();
    }

    private void LoadAnalyzer() {
        imageAnalyzer = new ImageAnalyzer();
        file = new File(singleFilePath.getText());
        String s = imageAnalyzer.LoadImage(file);
        if (s != null) Popup.P(s);
        else {
            PaintImage(imageAnalyzer.getImage());
            loadAnalyzerButton.setText("CLOSE IMAGE");
            hasImage = true;
        }
    }

    private void LoadEditor() {
        imageEditor = new ImageEditor();
        file = new File(editorFilePath.getText());
        String s = imageEditor.LoadImage(file);
        if (s != null) Popup.P(s);
        else {
            loadEditorButton.setText("CLOSE IMAGE");
            hasImage = true;
            output.setText("Enter text here for secret message. This will be embedded in the image.");
        }
    }

    private void editImage() {
        if (hideMessage.isSelected()) {
            output.setText("Calculating...");
            imageEditor.HideMessage(output.getText());
            imageEditor.SaveImage(file.getPath(), file.getName() + "_Edited.png");
            AddText("\nThe image has been saved as " + file.getName() + "_Edited.png");
            UnloadFile();
            AddText("\nThe Image has been flushed, the system is finished");
        } else if (retrieveMessage.isSelected()) {


        } else Popup.P("You must select an option from the radio Buttons");
    }

    private void PaintImage(BufferedImage pic) {
        ImageIcon icon = new ImageIcon(pic.getScaledInstance(480, 270, Image.SCALE_DEFAULT));
        output.setText(null);
        output.insertIcon(icon);
    }

    private void UnloadFile() {
        if (hasImage) {
            output.setText(null);
            loadAnalyzerButton.setText("LOAD IMAGE");
            loadEditorButton.setText("LOAD IMAGE");
            imageAnalyzer = null;
            imageEditor = null;
            hasImage = false;
        }
    }

    private void DetectSteg() {

        //Calculates Pixel density
        AddText("\nCalculating Pixel Density...");
        AddText("\n" + imageAnalyzer.PixelDensity());

        //Calculates pixel transition smoothness
        AddText("\nPlease note is NOT accurate for Jpeg's. \n\nCalculating Smoothness of pixel transition...");
        AddText("\n" + imageAnalyzer.Smoothness());

        imageAnalyzer.SaveReport();
        AddText("\n\nFor a more detailed breakdown of the data please read the Report; saved as report.txt at the root location of this program.");
    }

    private void processMulti() {

        File folder = new File(MultiFilePath.getText());
        if (!Files.exists(Path.of(MultiFilePath.getText()))) {
            Popup.P("Cannot find the path that has been requested.");
            return;
        } else {
            FilenameFilter txtFileFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".bmp"))
                        return true;
                    else return false;
                }
            };

            File[] files = folder.listFiles(txtFileFilter);
            output.setText("There are " + files.length + " images in this folder.");
            String s = null;

            for (File file : files) {
                imageAnalyzer = new ImageAnalyzer();
                s = imageAnalyzer.LoadImage(file);
                if (s != null) {
                    Popup.P("While trying to load " + file + " Received error- " + s);
                    continue;
                }

                AddText("\n\nLoading " + file.toString());
                AddText("\n" + imageAnalyzer.PixelDensity());
                AddText("\n" + imageAnalyzer.Smoothness());
                imageAnalyzer.SaveReport(file.getName());
            }
        }
        AddText("\n\nFor a breakdown of the compiled data please read the Reports; saved as (filename).txt at the root location of this program.");
    }

    private void AddText(String s) {
        try {
            doc.insertString(doc.getLength(), s, null);
        } catch (Exception e) {
            Popup.P("Attempting to write to the document gave error: " + e);
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel.setForeground(new Color(-14014418));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(-5717270));
        tabbedPane.setForeground(new Color(-16318459));
        panel.add(tabbedPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, 200), new Dimension(-1, 200), 0, false));
        tabbedPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tab1 = new JPanel();
        tab1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 6, new Insets(0, 0, 0, 0), -1, -1));
        tab1.setMinimumSize(new Dimension(504, 100));
        tabbedPane.addTab("Image Analyzer", tab1);
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        tab1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 6, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(500, -1), null, null, 0, false));
        singleFilePath = new JTextField();
        singleFilePath.setText("ENTER PATHNAME HERE");
        tab1.add(singleFilePath, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 6, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        loadAnalyzerButton = new JButton();
        loadAnalyzerButton.setLabel("LOAD IMAGE");
        loadAnalyzerButton.setText("LOAD IMAGE");
        tab1.add(loadAnalyzerButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        singleProcessButton = new JButton();
        singleProcessButton.setSelected(false);
        singleProcessButton.setText("PROCESS DATA");
        tab1.add(singleProcessButton, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tab2 = new JPanel();
        tab2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Muti Image Processor", tab2);
        multiProcessButton = new JButton();
        multiProcessButton.setText("CALCULATE");
        tab2.add(multiProcessButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        MultiFilePath = new JTextField();
        MultiFilePath.setText("To process all images in a folder at once; enter the pathname here. All images that are found will be analyzed.");
        tab2.add(MultiFilePath, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tab3 = new JPanel();
        tab3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Image Editor", tab3);
        editorFilePath = new JTextField();
        tab3.add(editorFilePath, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        hideMessage = new JRadioButton();
        hideMessage.setLabel("Imbed Message");
        hideMessage.setText("Imbed Message");
        tab3.add(hideMessage, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        retrieveMessage = new JRadioButton();
        retrieveMessage.setText("Retrieve Message");
        tab3.add(retrieveMessage, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        embedMessageButton = new JButton();
        embedMessageButton.setText("CALCULATE");
        tab3.add(embedMessageButton, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadEditorButton = new JButton();
        loadEditorButton.setText("LOAD IMAGE");
        tab3.add(loadEditorButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(-1, 10), null, null, 0, false));
        output = new JTextPane();
        Font outputFont = this.$$$getFont$$$("Arial Black", -1, 12, output.getFont());
        if (outputFont != null) output.setFont(outputFont);
        output.setText("Output Displayed here");
        panel.add(output, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(480, 270), new Dimension(150, 600), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(-1, 10), null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
