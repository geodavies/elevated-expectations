package aston.nabneyit.GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.text.*;


/**
 * Enhanced version of standard Swing JSlider widget
 *
 * (M0D1FIED BY Daniel Cotton)
 *
 * @author Ian T. Nabney
 * @version 20/04/2006
 */


public class LabelledSliderFP extends JComponent {

    /**
     *
     */
    private static final long serialVersionUID = -1978073598544126546L;
    private String labelString;
    private JLabel label;
    private JSlider slider;
    private JTextField textField;
    private int scale;

    private NumberFormat nf;
    private boolean updatingValue;

    /**
     * Creates a <code>LabelledSlider</code>.
     *
     * @param text a {@link java.lang.String} that names the slider
     * @param value a double that gives the initial value
     * @param min an int that is the minimum value for the slider
     * @param max an int that is the maximum value for the slider
     * @param scale an int that multiplies the value to give the value the slider displays
     */
    public LabelledSliderFP(String text, double value, int min, int max,
                            int scale) {
        if (value*scale < min || value*scale > max)
            throw new IllegalArgumentException("Value not in range for LabelledSlider.");
        this.setDoubleBuffered(true);
        label = new JLabel(text);
        labelString = new String(text);
        this.scale = scale;
        slider = new JSlider(min, max, (int)(value*scale));
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);

        // Construct Text Field
        textField = new JTextField();
        textField.setText(value + "");
        textField.getDocument().addDocumentListener(new TextListener());

        // Set slider properties
        slider.setPaintTicks(true);
        slider.addChangeListener(new SliderListener());
        // Put in 5 ticks
        slider.setMajorTickSpacing((max-min)/5);

        // Top Container
        JPanel labelContainer = new JPanel();
        labelContainer.setLayout(new BorderLayout());
        labelContainer.add(label, BorderLayout.NORTH);
        labelContainer.add(textField, BorderLayout.SOUTH);

        this.setLayout(new BorderLayout());
        this.add(labelContainer, BorderLayout.NORTH);
        this.add(slider, BorderLayout.SOUTH);

        int margin = 10;
        this.setBorder(new CompoundBorder(
                new EmptyBorder(margin, margin, margin, margin),
                new EtchedBorder()));

    }

    /**
     * Sets the major tick spacing for the LabelledSlider
     *
     * @param spacing an int that gives the new spacing value
     */
    public void setMajorTickSpacing(int spacing) {
        slider.setMajorTickSpacing(spacing);
        repaint();
    }

    /**
     * Returns scaled slider value
     *
     * @return the double value.
     */
    public double getValue() {
        return ((double)slider.getValue())/scale;
    }

    /**
     * Inner class to update the slider location and the text label
     */
    private class SliderListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            if (!slider.getValueIsAdjusting() && !updatingValue) {
                label.setText(labelString);
                textField.setText(((double)slider.getValue())/scale + "");
            }
            updatingValue = false;
        }
    }

    /**
     * Inner class to update the slider location and the text label
     */
    private class TextListener implements DocumentListener {

        public void stateChanged(DocumentEvent e) {
            String text = textField.getText();
            if (text.length() > 0 && !updatingValue) {
                double value = Double.parseDouble(text);
                int sliderValue = (int) (value * scale);
                updatingValue = true;
                slider.setValue(sliderValue);
            }
            updatingValue = false;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            stateChanged(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            stateChanged(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            stateChanged(e);
        }
    }
}