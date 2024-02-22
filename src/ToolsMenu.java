import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolsMenu extends JMenu{
    public ToolsMenu(DrawingPanel drawingPanel) {
        super("Tools");

        JMenuItem lineTool = new JMenuItem("Line");
        lineTool.addActionListener(e -> drawingPanel.setCurrentTool(DrawingPanel.Tool.LINE));

        JMenuItem shapeTool = new JMenuItem("Shape");
        shapeTool.addActionListener(e -> drawingPanel.setCurrentTool(DrawingPanel.Tool.SHAPE));

        JMenuItem fillTool = new JMenuItem("Fill");
        fillTool.addActionListener(e -> drawingPanel.setCurrentTool(DrawingPanel.Tool.FILL));

        JMenuItem penTool = new JMenuItem("Pen");
        penTool.addActionListener(e->drawingPanel.setCurrentTool(DrawingPanel.Tool.PEN));

        add(lineTool);
        add(fillTool);
        add(shapeTool);
        add(penTool);
    }
}
