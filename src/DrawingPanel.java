import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class DrawingPanel extends JPanel implements MouseListener {

    class LineTool {
        int width;
        int x1, y1;

        boolean isSecondClicked;

        public LineTool() {
            width = 1;
            isSecondClicked = false;
        }

        private void draw() {
            if (!isSecondClicked) {
                lineTool.x1 = x;
                lineTool.y1 = y;
                isSecondClicked = true;
            } else {
                if (lineTool.width != 1) {
                    Graphics2D g2d = (Graphics2D) image.getGraphics();
                    g2d.setStroke(new BasicStroke(lineTool.width));
                    g2d.setColor(currentColor);
                    g2d.drawLine(lineTool.x1, lineTool.y1, x, y);
                    repaint();
                } else {
                    drawLine(lineTool.x1, lineTool.y1, x, y);
                }
                isSecondClicked = false;
            }
        }
    }

    class ShapeTool {
        enum Shape {POLYGON, STAR}

        Shape currentShape;

        // Polygon radius
        int radius;

        // Star inner radius
        int innerRadius;

        // Rotation in degrees
        int rotation;

        int anglesCount;

        public ShapeTool() {
            currentShape = Shape.POLYGON;
            radius = 50;
            rotation = 0;
            anglesCount = 4;
        }

        private void drawPolygon(int x, int y) {
            double angle = 2 * Math.PI / anglesCount;
            double rotationRad = Math.toRadians(rotation);
            int[] xPoints = new int[anglesCount];
            int[] yPoints = new int[anglesCount];

            for (int i = 0; i < anglesCount; i++) {
                double rotatedAngle = i * angle + rotationRad;
                xPoints[i] = (int) (x + radius * Math.cos(rotatedAngle));
                yPoints[i] = (int) (y + radius * Math.sin(rotatedAngle));
            }

            // Рисуем линии между вершинами многоугольника
            for (int i = 0; i < anglesCount; i++) {
                int x1 = xPoints[i];
                int y1 = yPoints[i];
                int x2 = xPoints[(i + 1) % anglesCount];
                int y2 = yPoints[(i + 1) % anglesCount];
                drawLine(x1, y1, x2, y2);
            }
        }

        private void drawStar(int x, int y) {
            int[] xCords = new int[anglesCount * 2];
            int[] yCords = new int[anglesCount * 2];

            // Рассчитываем координаты точек звезды
            for (int i = 0; i < anglesCount * 2; i++) {
                if (i % 2 == 0) {
                    // Большие лучи
                    xCords[i] = (int) (x + radius * Math.cos(-Math.PI / 2 + rotation / 180.0 * Math.PI + (i * 2 * Math.PI) / (2 * anglesCount)));
                    yCords[i] = (int) (y + radius * Math.sin(-Math.PI / 2 + rotation / 180.0 * Math.PI + (i * 2 * Math.PI) / (2 * anglesCount)));
                } else {
                    // Малые лучи
                    xCords[i] = (int) (x + innerRadius * Math.cos(-Math.PI / 2 + rotation / 180.0 * Math.PI + (i * 2 * Math.PI) / (2 * anglesCount)));
                    yCords[i] = (int) (y + innerRadius * Math.sin(-Math.PI / 2 + rotation / 180.0 * Math.PI + (i * 2 * Math.PI) / (2 * anglesCount)));
                }
            }
            for (int i = 0; i < anglesCount * 2 - 1; i++) {
                drawLine(xCords[i], yCords[i], xCords[i+1], yCords[i+1]);
            }
            drawLine(xCords[anglesCount * 2 - 1], yCords[anglesCount * 2 - 1], xCords[0], yCords[0]);
        }
        public void draw(int x, int y) {
            if (currentShape == Shape.POLYGON) {
                drawPolygon(x, y);
            }
            else if (currentShape == Shape.STAR) {
                drawStar(x, y);
            }
        }
    }

    // Horizontal line width 1px
    class Span {
        int xStart, xEnd, y;
        public Span(int xStart, int xEnd, int y) {
            this.xStart = xStart;
            this.y = y;
            this.xEnd = xEnd;

        }
    }

    class FillTool {
        // Seed point
        int x, y;
        Stack<Span> SpanStack;
        int seedColor;
        BufferedImage image;

        Color newColor;

        public FillTool(int x, int y, BufferedImage image, Color newColor) {
            this.x = x;
            this.y = y;
            SpanStack = new Stack<>();
            this.image = image;
            seedColor = image.getRGB(x, y);
            this.newColor = newColor;
        }

        public Span findSpan(int x, int y) {
            if (image.getRGB(x, y) != seedColor) {
                return null;
            }

            image.setRGB(x, y, newColor.getRGB());
            int xStart, xEnd;

            // Finding right edge
            int cursor = x + 1;
            while (cursor < image.getWidth() && image.getRGB(cursor, y) == seedColor) {
                image.setRGB(cursor, y, newColor.getRGB());
                repaint();
                cursor ++;
            }

            xEnd = cursor - 1;

            // Finding left edge
            cursor = x - 1;
            while (cursor >= 0 && image.getRGB(cursor, y) == seedColor) {
                image.setRGB(cursor, y, newColor.getRGB());
                repaint();
                cursor --;
            }

            xStart = cursor + 1;

            return new Span(xStart, xEnd, y);
        }

        private void findConnectedSpans(Span curSpan, int x, int y) {
            Span newSpan;

            // Right
            for (int x_i = x + 1; x_i <= curSpan.xEnd && x_i <= image.getWidth(); x_i++) {
                newSpan = findSpan(x_i, y);
                if (newSpan != null) {
                    SpanStack.push(newSpan);
                }
            }
            // Left
            for (int x_i = x; x_i >= curSpan.xStart && x_i >= 0; x_i--) {
                newSpan = findSpan(x_i, y);
                if (newSpan != null) {
                    SpanStack.push(newSpan);
                }
            }
        }

        public void fill() {
            int curX, curY;
            // Находим первый спан затравки
            Span curSpan = findSpan(x, y);
            // Помещаем в стэк
            SpanStack.push(curSpan);
            while (!SpanStack.isEmpty()) {
                // Вытаскиваем спан из стэка
                curSpan = SpanStack.pop();

                curX = curSpan.xStart;
                curY = curSpan.y + 1;
                if (curY < image.getHeight())
                    findConnectedSpans(curSpan, curX, curY);
                curY -= 2;
                if (curY >= 0)
                    findConnectedSpans(curSpan, curX, curY);
            }
        }
    }

    // Variables
    private int x, y;
    private boolean isPressed;
    private boolean drawMode;
    public enum Tool {LINE, SHAPE, FILL, PEN}
    private Tool currentTool;
    private Color currentColor;
    private ShapeTool shapeTool;
    private LineTool lineTool;
    private FillTool fillTool;
    public BufferedImage image;
    private Stack<BufferedImage> undoStack;
    private Graphics2D g2d;
    private File outputFile = new File("outputfile.png");

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

    public DrawingPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 640, 480);


        currentColor = Color.BLACK;
        lineTool = new LineTool();
        shapeTool = new ShapeTool();

        // Setting up default tool
        currentTool = Tool.LINE;

        undoStack = new Stack<>();
        undoStack.push(image);

        setBackground(Color.WHITE);

        // Changing image size
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (image.getWidth() < getHeight() || image.getWidth() < getWidth())
                    image = resizeImage(image, getWidth(), getHeight());
                else
                    setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
            }
        });

        addMouseListener(this);

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (!isPressed) {
                    return;
                }

                if (currentTool == Tool.PEN) {
                    drawLine(x, y, e.getX(), e.getY());
                }

                x = e.getX();
                y = e.getY();
            }
        });
    }

    public void SaveFile() {
        try {
            ImageIO.write(image, "png", outputFile);
            System.out.println("Изображение успешно сохранено в " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении изображения: " + e.getMessage());
        }
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        int x = x1;
        int y = y1;
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        int directionX = 1;
        int directionY = 1;

        if (x1 > x2) {
            directionX = -1;
        }
        else if (x1 == x2) {
            directionX = 0;
        }

        if (y1 > y2) {
            directionY = -1;
        }
        else if (y1 == y2) {
            directionY = 0;
        }


        if (dx > dy) {
            int err = -dx;
            for (int i = 0; i < dx; ++i) {
                x += directionX;
                err += 2*dy;
                if (err > 0) {
                    err -= 2*dx;
                    y += directionY;
                }

                int rgb = currentColor.getRGB();
                if ((x < getWidth()) && (x >= 0) && (y < getHeight()) && (y >= 0))
                    image.setRGB(x, y, rgb);
                repaint();
            }
        }
        else {
            int err = -dy;
            for (int i = 0; i < dy; ++i) {
                y += directionY;
                err += 2*dx;
                if (err > 0) {
                    err -= 2*dy;
                    x += directionX;
                }
                int rgb = currentColor.getRGB();
                if ((x < getWidth()) && (x >= 0) && (y < getHeight()) && (y >= 0))
                    image.setRGB(x, y, rgb);
                repaint();
            }
        }

    }

    public ShapeTool getShapeTool() {
        return shapeTool;
    }

    public LineTool getLineTool() {
        return lineTool;
    }

    public void setCurrentTool(Tool newTool) {
        if (currentTool == Tool.LINE)
            lineTool.isSecondClicked = false;
        currentTool = newTool;
        System.out.println("current tool:" + currentTool);
    }

    public void setCurrentColor(Color newColor) {
        currentColor = newColor;
        System.out.println("current color:" + currentColor);
    }

    public void setShapeToolSettings(int radius, int rotation, int angelsCount, int innerRadius) {
        if (shapeTool != null)
        {
            if (radius > 0) {
                shapeTool.radius = radius;
            }
            shapeTool.rotation = rotation;

            if (angelsCount > 0) {
                shapeTool.anglesCount = angelsCount;
            }

            if (innerRadius > 0) {
                shapeTool.currentShape = ShapeTool.Shape.STAR;
                shapeTool.innerRadius = innerRadius;
            }
            else {
                shapeTool.currentShape = ShapeTool.Shape.POLYGON;
            }

        }

    }

    public void setLineToolSettings(int width) {
        if (lineTool != null) {
            lineTool.width = width;
        }
    }

    public BufferedImage resizeImage(BufferedImage img, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, newWidth, newHeight);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    private BufferedImage copyImage(BufferedImage source) {
        BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics2D g = copy.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return copy;
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            image = undoStack.pop();
            g2d = image.createGraphics();
            repaint();
        }
    }

    public void clean() {
        drawMode = !drawMode;
        image.getGraphics().fillRect(0, 0, getWidth(), getHeight());
        repaint();
    }

    public void openSettingsWindow() {
        if (currentTool == Tool.SHAPE) {
            ShapeToolSettingsWindow settingsWindow = new ShapeToolSettingsWindow(this);
            settingsWindow.showDialog();
        }
        else if (currentTool == Tool.LINE) {
            LineToolSettingsWindow settingsWindow = new LineToolSettingsWindow(this);
            settingsWindow.showDialog();
        }
        else if (currentTool == Tool.FILL) {
            JOptionPane.showMessageDialog(this,  "Для этого инструмента нет параметров, кроме" +
                    "текущего цвета");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (currentTool != Tool.LINE) {
            undoStack.push(copyImage(image));
        }

        isPressed = true;
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isPressed = false;
        x = e.getX();
        y = e.getY();
        if (currentTool == Tool.LINE) {
            if (!lineTool.isSecondClicked)
                undoStack.push(copyImage(image));
            lineTool.draw();
        }
        else if (currentTool == Tool.SHAPE) {
            shapeTool.draw(x, y);
        }
        else if (currentTool == Tool.FILL) {
            fillTool = new FillTool(x, y, image, currentColor);
            fillTool.fill();
        }


    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
