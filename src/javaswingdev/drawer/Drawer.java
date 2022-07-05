package javaswingdev.drawer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

public class Drawer {

    public static Drawer newDrawer(JFrame fram) {
        return new Drawer(fram);
    }
    private Animator animator;
    private final JFrame fram;
    private final List<Component> childrens;
    private Component header;
    private int drawerWidth = 250;
    private int headerHeight = 150;
    private Color background = new Color(30, 30, 30);
    private Color drawerBackground = Color.WHITE;
    private float backgroundTransparent = 0.5f;

    private Drawer(JFrame fram) {
        this.fram = fram;
        childrens = new ArrayList<>();
    }

    private void initAnimator(int duration, int resolution, DrawerPanel drawer) {
        animator = new Animator(duration);
        animator.setAcceleration(.5f);
        animator.setDeceleration(.5f);
        animator.setResolution(resolution);
        animator.addTarget(new PropertySetter(drawer, "animate", drawer.getAnimate(), 1f));
    }

    public Drawer header(Component component) {
        this.header = component;
        return this;
    }

    public Drawer addChild(Component component) {
        childrens.add(component);
        return this;
    }

    public Drawer background(Color color) {
        background = color;
        return this;
    }

    public Drawer drawerBackground(Color color) {
        drawerBackground = color;
        return this;
    }

    public Drawer drawerWidth(int drawerWidth) {
        this.drawerWidth = drawerWidth;
        return this;
    }

    public Drawer headerHeight(int headerHeight) {
        this.headerHeight = headerHeight;
        return this;
    }

    public Drawer backgroundTransparent(float backgroundTransparent) {
        this.backgroundTransparent = backgroundTransparent;
        return this;
    }

    public void build() {
        DrawerPanel panel = new DrawerPanel(drawerWidth, backgroundTransparent);
        panel.setBackground(background);
        panel.setDrawerBackground(drawerBackground);
        if (header != null) {
            panel.addItem(header, "h " + headerHeight);
        }
        for (Component com : childrens) {
            panel.addItem(com);
        }
        fram.setGlassPane(panel);
        fram.getGlassPane().setVisible(true);
        panel.setAnimate(1);
    }

    private class DrawerPanel extends JComponent {

        public float getAnimate() {
            return animate;
        }

        public void setAnimate(float animate) {
            this.animate = animate;
            int w = (int) ((width * animate) - width);
            layout.setComponentConstraints(panel, "pos " + w + " 0 n 100%");
            panel.revalidate();
            repaint();
        }

        private final MigLayout layout;
        private final DrawerItem panel;
        private float animate = 0f;
        private final int width;
        private final float targetAlpha;

        public DrawerPanel(int width, float targetAlpha) {
            this.width = width;
            this.targetAlpha = targetAlpha;
            layout = new MigLayout("");
            setLayout(layout);
            panel = new DrawerItem();
            panel.setOpaque(false);
            panel.setLayout(new MigLayout("inset 0, wrap, gap 0", " [" + width + "]", "[fill,top]"));
            add(panel, "pos -" + width + " 0 n 100%");
        }

        public void addItem(Component com) {
            panel.add(com);
        }

        public void addItem(Component com, Object cmd) {
            panel.add(com, cmd);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animate * targetAlpha));
            g2.fill(new Rectangle(0, 0, getWidth(), getHeight()));
            g2.dispose();
            super.paintComponent(g);
        }

        public void setDrawerBackground(Color color) {
            panel.setBackground(color);
        }
    }

    private class DrawerItem extends JComponent {

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.fill(new Rectangle(0, 0, getWidth(), getHeight()));
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
