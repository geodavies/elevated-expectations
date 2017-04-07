package uk.ac.aston.dc2300.gui;

import de.craften.ui.swingmaterial.MaterialProgressSpinner;
import de.craften.ui.swingmaterial.MaterialTextField;
import javafx.scene.paint.Material;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

/**
 * Created by dan on 05/04/2017.
 */
public class ConfigFrame extends JFrame {

    // Constructor:
    public ConfigFrame() {
        setTitle("My Closeable Frame");
        setSize(800,600); // default size is 0,0
        setLocation(10,200); // default is 0,0 (top left corner)
        Container contentPane = this.getContentPane();
        MaterialProgressSpinner loadingSpinner = new MaterialProgressSpinner();
        loadingSpinner.setSize(50, 50);
        loadingSpinner.setLocation(375, 75);
        contentPane.add(loadingSpinner);


        // Window Listeners
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            } //windowClosing
        } );
    }

}