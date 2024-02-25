public class ShapeTool {
    enum Shape {POLYGON, STAR}

    Shape currentShape;

    // Polygon radius
    int radius;

    // Star inner radius
    int innerRadius;

    // Rotation in degrees
    int rotation;

    int anglesCount;

    DrawingPanel drawingPanel;

    public ShapeTool(DrawingPanel drawingPanel) {
        currentShape = Shape.POLYGON;
        radius = 50;
        rotation = 0;
        anglesCount = 4;
        this.drawingPanel = drawingPanel;
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
            drawingPanel.drawLine(x1, y1, x2, y2);
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
            drawingPanel.drawLine(xCords[i], yCords[i], xCords[i+1], yCords[i+1]);
        }
        drawingPanel.drawLine(xCords[anglesCount * 2 - 1], yCords[anglesCount * 2 - 1], xCords[0], yCords[0]);
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