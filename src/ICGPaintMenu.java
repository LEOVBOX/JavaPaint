import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ICGPaintMenu extends JMenu {
    public ICGPaintMenu() {
        super("ICGPaint");
        JMenuItem aboutMenuItem = new JMenuItem("About program");
        String aboutMessage = "ICGPaint is program for simple 2D graphics";
        aboutMenuItem.addActionListener(e->JOptionPane.showMessageDialog(this,  aboutMessage));
        add(aboutMenuItem);
    }
}
