import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LineToolSettingsWindow extends JDialog {
    private boolean confirmed = false;

    private JTextField widthField;

    private DrawingPanel drawingPanel;

    public LineToolSettingsWindow(DrawingPanel drawingPanel) {
        super();
        this.drawingPanel = drawingPanel;
        initComponents();
    }

    private void initComponents() {
        //setResizable(false);
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JLabel widthLabel = new JLabel("Толщина линии в пикселях:");
        widthField = new JTextField();
        widthField.setText(Integer.toString(drawingPanel.getLineTool().width));

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Отмена");

        okButton.addActionListener(e -> {
            confirmed = true;
            drawingPanel.setLineToolSettings(getWidthParam());
            dispose();
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        panel.add(widthLabel);
        panel.add(widthField);
        panel.add(okButton);
        panel.add(cancelButton);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(getParent());
    }

    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }

    public int getWidthParam() {
        try {
            int width = Integer.parseInt(widthField.getText());
            if (width <= 0) {
                JOptionPane.showMessageDialog(this,  "Введено некорректное значение толщины. " +
                        "Убедитесь, что вы вводите целые положительные числа");
                return 0;
            }
            else {
                return width;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,  "Введено некорректное значение толщины. " +
                    "Убедитесь, что вы вводите целые положительные числа");
            return 0;
        }
    }
}
