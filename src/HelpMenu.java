import javax.swing.*;

public class HelpMenu extends JMenu {
    public HelpMenu() {
        super("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About program");
        String aboutMessage = "ICGPaint is program for simple 2D graphics.\n Shaikhutdinov Leonid©";
        aboutMenuItem.addActionListener(e->JOptionPane.showMessageDialog(this,  aboutMessage));
        add(aboutMenuItem);
    }
}
