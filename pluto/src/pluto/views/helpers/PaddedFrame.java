package pluto.views.helpers;

import javax.swing.*;
import java.awt.*;

public class PaddedFrame extends JFrame {
    public PaddedFrame(String title) {
        super(title);

        /* Padding on top and bottom */
        JPanel jTop = new JPanel();
        JPanel jBottom = new JPanel();
        JPanel jLeft = new JPanel();
        JPanel jRight = new JPanel();
        jTop.setPreferredSize(new Dimension(getWidth(), 60));
        jBottom.setPreferredSize(new Dimension(getWidth(), 80));
        jLeft.setPreferredSize(new Dimension(50, 0));
        jRight.setPreferredSize(new Dimension(50, 0));
        add(jTop, BorderLayout.NORTH);
        add(jBottom, BorderLayout.SOUTH);
        add(jLeft, BorderLayout.WEST);
        add(jRight, BorderLayout.EAST);
    }
}
