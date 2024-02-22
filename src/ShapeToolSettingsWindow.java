import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ShapeToolSettingsWindow extends JDialog {
    private boolean confirmed = false;
    private JTextField radiusField;
    private JTextField rotationField;
    private JTextField anglesCountField;
    private JTextField innerRadiusField;
    private JSlider radiusSlider;
    private JSlider innerRadiusSlider;
    private JSlider rotationSlider;
    private DrawingPanel drawingPanel;


    public ShapeToolSettingsWindow(DrawingPanel drawingPanel) {
        super();
        this.drawingPanel = drawingPanel;
        initComponents();
    }

    private void initComponents() {
        setResizable(false);
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JLabel radiusLabel = new JLabel("Радиус:");
        JLabel rotationLabel = new JLabel("Поворот (градусы):");

        DocumentListener innerRadiusFieldListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                int newVal = getInnerRadius();
                innerRadiusSlider.setValue(newVal);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                innerRadiusSlider.setValue(getInnerRadius());
            }
        };

        radiusField = new JTextField();
        radiusSlider = new JSlider();

        radiusField.setText(Integer.toString(drawingPanel.getShapeTool().radius));

        rotationField = new JTextField();
        rotationField.setText(Integer.toString(drawingPanel.getShapeTool().rotation));
        rotationSlider = new JSlider();

        innerRadiusSlider = new JSlider(0, 100, drawingPanel.getShapeTool().innerRadius);
        innerRadiusSlider.setMajorTickSpacing(10);
        innerRadiusSlider.setMinorTickSpacing(1);
        innerRadiusSlider.setPaintTicks(true);
        innerRadiusSlider.setPaintLabels(true);
        innerRadiusSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (source.getValueIsAdjusting()) {
                    innerRadiusField.getDocument().removeDocumentListener(innerRadiusFieldListener);
                    innerRadiusField.setText(Integer.toString(source.getValue()));
                    innerRadiusField.getDocument().addDocumentListener(innerRadiusFieldListener);
                }

            }
        });
        innerRadiusField = new JTextField();
        innerRadiusField.setText(Integer.toString(drawingPanel.getShapeTool().innerRadius));
        innerRadiusField.getDocument().addDocumentListener(innerRadiusFieldListener);

        JLabel anglesCountLabel = new JLabel("Количество углов:");
        JLabel innerRadiusLabel = new JLabel("Внутренний радиус:");
        anglesCountField = new JTextField();
        anglesCountField.setText(Integer.toString(drawingPanel.getShapeTool().anglesCount));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Отмена");

        okButton.addActionListener(e -> {
            confirmed = true;
            drawingPanel.setShapeToolSettings(getRadius(), getRotation(), getAnglesCount(), getInnerRadius());
            dispose();
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        panel.add(radiusLabel);
        panel.add(radiusField);
        panel.add(radiusSlider);

        panel.add(innerRadiusLabel);
        panel.add(innerRadiusField);
        panel.add(innerRadiusSlider);

        panel.add(rotationLabel);
        panel.add(rotationField);
        panel.add(rotationSlider);

        panel.add(anglesCountLabel);
        panel.add(anglesCountField);
        panel.add(new JPanel());

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

    public int getRadius() {
        try {
            int radius = Integer.parseInt(radiusField.getText());
            if (radius <= 0) {
                JOptionPane.showMessageDialog(this,  "Введено некорректное значение радиуса. " +
                        "Убедитесь, что вы вводите целые, положительные числа");
                return 0;
            }
            else {
                return radius;
            }

        } catch (NumberFormatException e) {
            if (!radiusField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,  "Введено некорректное значение радиуса. " +
                        "Убедитесь, что вы вводите целые, положительные числа");
            }
            return 0;
        }
    }

    public int getInnerRadius() {
        try {
            int radius = Integer.parseInt(innerRadiusField.getText());
            if (radius < 0) {
                JOptionPane.showMessageDialog(this,  "Введено некорректное значение внутреннего радиуса. " +
                        "Убедитесь, что вы вводите целые, неотрицательные числа");
                return 0;
            }
            else {
                return radius;
            }

        } catch (NumberFormatException e) {
            if (!innerRadiusField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,  "Введено некорректное значение внутреннего радиуса. " +
                        "Убедитесь, что вы вводите целые, положительные числа");
            }
            return 0;
        }
    }

    public int getRotation() {
        int rotation;
        try {
            rotation = Integer.parseInt(rotationField.getText());
            return rotation;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,  "Введено некорректное значение поворота. " +
                    "Убедитесь, что вы вводите целые числа");
            return 0;
        }
    }
    public int getAnglesCount() {
        try {
            int anglesCount = Integer.parseInt(anglesCountField.getText());
            if (anglesCount <= 0) {
                JOptionPane.showMessageDialog(this,  "Введено некорректное значение количества углов. " +
                        "Убедитесь, что вы вводите целые числа");
                return 0;
            }
            else {
                return anglesCount;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,  "Введено некорректное значение количества углов. " +
                    "Убедитесь, что вы вводите целые числа");
            return 0; // В случае ошибки возвращаем 0
        }
    }
}
