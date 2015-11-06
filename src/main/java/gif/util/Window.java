package gif.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

/**
 * Created by dgoetsch on 11/5/15.
 */
public class Window extends JFrame {
    //static Window frame = new Window("Ascii GIF");
    JTextArea display;

    public Window(String name) {
        super(name);
        init();
    }

    public void init() {
        display = new JTextArea();
        display.setFont(new Font("monospaced", Font.PLAIN, 6));
        JScrollPane scrollPane = new JScrollPane(display);
        scrollPane.setPreferredSize(new Dimension(1500, 1000));
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    public void updateText(String newVal) {
        display.setText(newVal);
    }
}
