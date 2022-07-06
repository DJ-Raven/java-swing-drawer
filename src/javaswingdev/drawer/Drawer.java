package javaswingdev.drawer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class Drawer implements DrawerController {

    public static Drawer newDrawer(JFrame fram) {
        return new Drawer(fram);
    }

    private DrawerPanel panelDrawer;
    private Animator animator;
    private MouseListener mouseEvent;
    private final JFrame fram;
    private final List<Component> childrens;
    private Component header;
    private int drawerWidth = 250;
    private int headerHeight = 150;
    private Color background = new Color(30, 30, 30);
    private Color drawerBackground = Color.WHITE;
    private float backgroundTransparent = 0.5f;
    private int duration = 500;
    private int resolution = 10;
    private boolean isShow;
    private boolean closeOnPress = true;
    private boolean leftDrawer = true;

    private Drawer(JFrame fram) {
        this.fram = fram;
        childrens = new ArrayList<>();
    }

    private void createAnimator(int duration, int resolution) {
        animator = new Animator(duration, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (isShow) {
                    panelDrawer.setAnimate(fraction);
                } else {
                    panelDrawer.setAnimate(1f - fraction);
                }
            }

            @Override
            public void end() {
                if (panelDrawer.getAnimate() == 0) {
                    fram.getGlassPane().setVisible(false);
                }
            }

        });
        animator.setAcceleration(.5f);
        animator.setDeceleration(.5f);
        animator.setResolution(resolution);
        if (closeOnPress) {
            mouseEvent = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    hide();
                }
            };
        }
    }

    public Drawer header(Component component) {
        this.header = component;
        return this;
    }

    public Drawer addChild(Component component) {
        childrens.add(component);
        return this;
    }

    public Drawer space(int height) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(0, height));
        childrens.add(label);
        return this;
    }

    public Drawer separator(int height, Color color) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(0, height));
        label.setBackground(color);
        label.setOpaque(true);
        childrens.add(label);
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

    public Drawer duration(int duration) {
        this.duration = duration;
        return this;
    }

    public Drawer resolution(int resolution) {
        this.resolution = resolution;
        return this;
    }

    public Drawer closeOnPress(boolean closeOnPress) {
        this.closeOnPress = closeOnPress;
        return this;
    }

    public Drawer leftDrawer(boolean leftDrawer) {
        this.leftDrawer = leftDrawer;
        return this;
    }

    @Override
    public void show() {
        if (!isShow) {
            fram.getGlassPane().setVisible(true);
            if (closeOnPress) {
                panelDrawer.removeMouseListener(mouseEvent);
                panelDrawer.addMouseListener(mouseEvent);
            }
            start(true);
        }
    }

    @Override
    public void hide() {
        if (isShow) {
            if (closeOnPress) {
                panelDrawer.removeMouseListener(mouseEvent);
            }
            start(false);
        }
    }

    @Override
    public boolean isShow() {
        return isShow;
    }

    private void start(boolean isShow) {
        if (animator.isRunning()) {
            animator.stop();
            float f = animator.getTimingFraction();
            animator.setStartFraction(1f - f);
        } else {
            animator.setStartFraction(0f);
        }
        this.isShow = isShow;
        animator.start();
    }

    public DrawerController build() {
        panelDrawer = new DrawerPanel(drawerWidth, backgroundTransparent, leftDrawer);
        panelDrawer.setBackground(background);
        panelDrawer.setDrawerBackground(drawerBackground);
        if (header != null) {
            panelDrawer.addItem(header, "h " + headerHeight);
        }
        for (Component com : childrens) {
            panelDrawer.addItem(com);
        }
        createAnimator(duration, resolution);
        fram.setGlassPane(panelDrawer);
        fram.getGlassPane().setVisible(true);
        return this;
    }

    protected class DrawerPanel extends JComponent {

        public float getAnimate() {
            return animate;
        }

        public void setAnimate(float animate) {
            this.animate = animate;
            if (leftDrawer) {
                int w = (int) ((width * animate) - width);
                layout.setComponentConstraints(panel, "pos " + w + " 0 n 100%");
            } else {
                int w = (int) ((width * animate));
                layout.setComponentConstraints(panel, "pos 100%-" + w + " 0 n 100%");
            }
            panel.revalidate();
            if (targetAlpha != 0) {
                repaint();
            }
        }

        private final MigLayout layout;
        private final DrawerItem panel;
        private float animate = 0f;
        private final int width;
        private final float targetAlpha;
        private final boolean leftDrawer;

        public DrawerPanel(int width, float targetAlpha, boolean leftDrawer) {
            this.width = width;
            this.targetAlpha = targetAlpha;
            this.leftDrawer = leftDrawer;
            layout = new MigLayout();
            setLayout(layout);
            panel = new DrawerItem();
            panel.setOpaque(false);
            panel.setLayout(new MigLayout("inset 0, wrap, gap 0", " [" + width + "!,fill]", "[fill,top]"));
            if (leftDrawer) {
                add(panel, "pos -" + width + " 0 n 100%");
            } else {
                add(panel, "pos 100% 0 n 100%");
            }
        }

        public void addItem(Component com) {
            panel.add(com);
        }

        public void addItem(Component com, Object cmd) {
            panel.add(com, cmd);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (targetAlpha != 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getBackground());
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animate * targetAlpha));
                g2.fill(new Rectangle(0, 0, getWidth(), getHeight()));
                g2.dispose();
            }
            super.paintComponent(g);
        }

        public void setDrawerBackground(Color color) {
            panel.setBackground(color);
        }
    }

    private class DrawerItem extends JComponent {

        public DrawerItem() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                }
            });
        }

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
