package us.jonathans;

import us.jonathans.drawing.ColorHelper;
import us.jonathans.geom.Align;
import us.jonathans.geom.Obj2;
import us.jonathans.geom.Vec2;
import us.jonathans.sprites.Hole;
import us.jonathans.sprites.Stone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

public class JMancalaPanel extends JPanel implements MouseMotionListener {
    final Dimension dimension = new Dimension(400, 600);
    final int[] board = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    final ArrayList<Hole> holes = new ArrayList<>();
    final ArrayList<Stone> stones = new ArrayList<>();

    public JMancalaPanel() {
        super();
        this.addMouseMotionListener(this);
        this.setDoubleBuffered(true);

        int holeRadius = this.dimension.height / 9;
        int holeMarginY = holeRadius / 6;
        int col1X = this.dimension.width / 3;
        int col2X = this.dimension.width / 3 * 2;

        for (int i = 0; i < 5 ; i++) {
            holes.add(new Hole(
                    col1X,
                    i * (holeMarginY + holeRadius) + holeRadius / 2 + holeMarginY,
                    holeRadius,
                    holeRadius,
                    Align.CENTER,
                    i
            ));
        }
        for (int i = 11; i > 6; i--) {
            holes.add(new Hole(
                    col2X,
                    (i-7) * (holeMarginY + holeRadius) + holeRadius / 2 + holeMarginY,
                    holeRadius,
                    holeRadius,
                    Align.CENTER,
                    i
            ));
        }

        int stoneRadius = 15;
        Random r = new Random();

        holes.forEach(hole -> {
            int nStones = board[hole.getId()];
            for (int i = 0; i < nStones; i++) {
                Vec2 stonePos = pointInsideCircle(hole, stoneRadius, r);
                stones.add(
                        new Stone(
                                stonePos.x,
                                stonePos.y,
                                stoneRadius * 2,
                                stoneRadius * 2,
                                Align.CENTER,
                                ColorHelper.getRandomColor(r)
                        )
                );
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;

//         Set anti-aliasing
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_ENABLE);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.dimension.width, this.dimension.height);

        Point mousePoint = this.getMousePosition();

        holes.forEach(hole -> {
            if (mousePoint != null) {
                hole.setHovered(hole.contains(mousePoint.x, mousePoint.y));
            } else {
                hole.setHovered(false);
            }
            hole.draw(g);
            g.setColor(Color.MAGENTA);
        });

        stones.forEach(stone -> {
            stone.draw(g);
        });
    }



    private Vec2 pointInsideCircle(Obj2 obj, int radius, Random r) {
        double radians = 2.0d * Math.PI * r.nextDouble(1.0d);
        double deviation = (obj.radius() - radius) * Math.sqrt(r.nextDouble(1.0d));
        return new Vec2(
                (int) (obj.cx() + deviation * Math.cos(radians)),
                (int) (obj.cy() + deviation * Math.sin(radians))
        );
    }

    @Override
    public Dimension getPreferredSize() {
        return this.getMaximumSize();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        repaint();
    }
}
