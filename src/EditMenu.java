import javax.swing.*;
import java.awt.*;

public class EditMenu extends JMenu {
    public EditMenu(DrawingPanel drawingPanel) {
        super("Edit");
        JMenuItem undoMenuButton = new JMenuItem("Undo");
        undoMenuButton.addActionListener(e->drawingPanel.undo());
        add(undoMenuButton);
        JMenuItem cleanMenuButton = new JMenuItem("Clean");
        cleanMenuButton.addActionListener(e->drawingPanel.clean());
        add(cleanMenuButton);
        JMenuItem toolSettingsButton = new JMenuItem("Tool settings");
        add(toolSettingsButton);
        JMenuItem paletteButton = new JMenuItem("Palette");
        paletteButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(drawingPanel, "Выберите цвет", Color.BLACK);
            if (selectedColor != null) {
                drawingPanel.setCurrentColor(selectedColor);
            }
        });
        add(paletteButton);

    }
}
