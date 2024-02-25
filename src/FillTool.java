import java.awt.*;
import java.util.Stack;

public class FillTool {
    static class Span {
        int xStart, xEnd, y;
        public Span(int xStart, int xEnd, int y) {
            this.xStart = xStart;
            this.y = y;
            this.xEnd = xEnd;

        }
    }
    // Seed point
    int x, y;
    Stack<Span> SpanStack;
    int seedColor;
    DrawingPanel drawingPanel;

    Color newColor;

    public FillTool(int x, int y, DrawingPanel drawingPanel, Color newColor) {
        this.x = x;
        this.y = y;
        SpanStack = new Stack<Span>();
        this.drawingPanel = drawingPanel;
        seedColor = drawingPanel.image.getRGB(x, y);
        this.newColor = newColor;
    }

    public Span findSpan(int x, int y) {
        if (drawingPanel.image.getRGB(x, y) != seedColor) {
            return null;
        }

        drawingPanel.image.setRGB(x, y, newColor.getRGB());
        int xStart, xEnd;

        // Finding right edge
        int cursor = x + 1;
        while (cursor < drawingPanel.image.getWidth() && drawingPanel.image.getRGB(cursor, y) == seedColor) {
            drawingPanel.image.setRGB(cursor, y, newColor.getRGB());
            drawingPanel.repaint();
            cursor ++;
        }

        xEnd = cursor - 1;

        // Finding left edge
        cursor = x - 1;
        while (cursor >= 0 && drawingPanel.image.getRGB(cursor, y) == seedColor) {
            drawingPanel.image.setRGB(cursor, y, newColor.getRGB());
            drawingPanel.repaint();
            cursor --;
        }

        xStart = cursor + 1;

        return new Span(xStart, xEnd, y);
    }

    private void findConnectedSpans(Span curSpan, int x, int y) {
        Span newSpan;

        // Right
        for (int x_i = x + 1; x_i <= curSpan.xEnd && x_i <= drawingPanel.image.getWidth(); x_i++) {
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
            if (curY < drawingPanel.image.getHeight())
                findConnectedSpans(curSpan, curX, curY);
            curY -= 2;
            if (curY >= 0)
                findConnectedSpans(curSpan, curX, curY);
        }
    }
}