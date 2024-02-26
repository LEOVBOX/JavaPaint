import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class DrawingPanel extends JPanel implements MouseListener {
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
    private File outputFile;

    private boolean isResizeable;

    private boolean fileOpened;
    private MainWindow mainWindow;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

    public DrawingPanel(MainWindow mainWindow) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        this.mainWindow = mainWindow;
        isResizeable = true;
        fileOpened = false;


        image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 640, 480);


        currentColor = Color.BLACK;
        lineTool = new LineTool(this);
        shapeTool = new ShapeTool(this);

        // Setting up default tool
        currentTool = Tool.LINE;

        undoStack = new Stack<>();
        undoStack.push(image);

        setBackground(Color.WHITE);

        // Changing image size
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (isResizeable) {
                    super.componentResized(e);
                    if (image.getWidth() < getHeight() || image.getWidth() < getWidth())
                        image = resizeImage(image, getWidth(), getHeight());
                    else
                        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
                }
                if (fileOpened)
                    isResizeable = false;

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

    public void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // Получаем выбранный файл
            File selectedFile = fileChooser.getSelectedFile();

            // Сохраняем BufferedImage в выбранный файл
            if (!selectedFile.getName().toLowerCase().endsWith(".png")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".png");
            }
            outputFile = selectedFile;
            saveFile();
        }
    }

    public void saveFile() {
        if (outputFile == null) {
            saveFileAs();
        }
        else {
            try {
                ImageIO.write(image, "png", outputFile);
                System.out.println("Изображение успешно сохранено в " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Ошибка при сохранении изображения: " + e.getMessage());
            }
        }
    }


    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "png", "jpeg", "jpg", "gif"));
        int result = fileChooser.showOpenDialog(DrawingPanel.this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(selectedFile);
                if (image != null) {
                    this.image = image;
                    this.setSize(new Dimension(image.getWidth(), image.getHeight()));
                    this.setMaximumSize(new Dimension(image.getWidth(), getHeight()));
                    fileOpened = true;
                }
                else {
                    JOptionPane.showMessageDialog(this, "Failed to load image", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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

        int rgbColor = currentColor.getRGB();


        if (dx > dy) {
            int err = -dx;
            for (int i = 0; i < dx; ++i) {
                x += directionX;
                err += 2*dy;
                if (err > 0) {
                    err -= 2*dx;
                    y += directionY;
                }

                if ((x < image.getWidth()) && (x >= 0) && (y < image.getHeight()) && (y >= 0))
                    image.setRGB(x, y, rgbColor);
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
                if ((x < image.getWidth()) && (x >= 0) && (y < image.getHeight()) && (y >= 0))
                    image.setRGB(x, y, rgbColor);
            }
        }
        repaint();

    }

    public ShapeTool getShapeTool() {
        return shapeTool;
    }

    public LineTool getLineTool() {
        return lineTool;
    }

    public Color getCurrentColor() {
        return currentColor;
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
            lineTool.draw(x, y);
        }
        else if (currentTool == Tool.SHAPE) {
            shapeTool.draw(x, y);
        }
        else if (currentTool == Tool.FILL) {
            if ((x >= 0) && (x < image.getWidth()) && (y >= 0) && (y < image.getHeight())) {
                fillTool = new FillTool(x, y, this, currentColor);
                fillTool.fill();
            }

        }


    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
