import javax.swing.*;
import java.io.File;

public class FileMenu extends JMenu {
    public FileMenu(DrawingPanel drawingPanel) {
        super("File");

        JMenuItem newFile = new JMenuItem("new");

        JMenuItem openFile = new JMenuItem("open");

        JMenuItem saveAs = new JMenuItem("save as");

        JMenuItem save = new JMenuItem("save");
        save.addActionListener(e->drawingPanel.SaveFile());

        add(newFile);
        add(saveAs);
        add(save);
        add(openFile);
    }

}
