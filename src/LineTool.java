import java.awt.*;
import java.awt.image.BufferedImage;

public class LineTool {
    int width;
    int x1, y1;

    DrawingPanel drawingPanel;

    boolean isSecondClicked;

    public LineTool(DrawingPanel drawingPanel) {
        width = 1;
        isSecondClicked = false;
        this.drawingPanel = drawingPanel;
    }

    public void draw(int x, int y) {
        if (!isSecondClicked) {
            x1 = x;
            y1 = y;
            isSecondClicked = true;
        } else {
            if (width != 1) {
                Graphics2D g2d = (Graphics2D) drawingPanel.image.getGraphics();
                g2d.setStroke(new BasicStroke(width));
                g2d.setColor(drawingPanel.getCurrentColor());
                g2d.drawLine(x1, y1, x, y);
                drawingPanel.repaint();
            } else {
                drawingPanel.drawLine(x1, y1, x, y);
            }
            isSecondClicked = false;
        }
    }
}
