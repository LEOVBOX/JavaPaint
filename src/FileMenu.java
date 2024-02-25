import javax.swing.*;

public class FileMenu extends JMenu {
    public FileMenu(DrawingPanel drawingPanel) {
        super("File");

        JMenuItem newFile = new JMenuItem("new");

        JMenuItem openFile = new JMenuItem("open");
        openFile.addActionListener(e->drawingPanel.openFile());

        JMenuItem saveAs = new JMenuItem("save as");
        saveAs.addActionListener(e->drawingPanel.saveFileAs());

        JMenuItem save = new JMenuItem("save");
        save.addActionListener(e->drawingPanel.saveFile());

        add(newFile);
        add(saveAs);
        add(save);
        add(openFile);
    }

}
