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
    private DocumentListener radiusFieldListener;
    private DocumentListener innerRadiusFieldListener;
    private DocumentListener rotationFieldListener;


    public ShapeToolSettingsWindow(DrawingPanel drawingPanel) {
        super();
        this.drawingPanel = drawingPanel;
        initComponents();
    }

    private void initRadiusField() {
        radiusFieldListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                int newVal = getRadius();
                radiusSlider.setValue(newVal);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                radiusSlider.setValue(getInnerRadius());
            }
        };

        radiusField = new JTextField();
        radiusField.setText(Integer.toString(drawingPanel.getShapeTool().radius));
    }

    private void initRadiusSlider() {
        radiusSlider = new JSlider(0, drawingPanel.getHeight(), drawingPanel.getShapeTool().radius);
        radiusSlider.setPaintTicks(true);
        radiusSlider.setPaintLabels(true);
        radiusSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (source.getValueIsAdjusting()) {
                radiusField.getDocument().removeDocumentListener(radiusFieldListener);
                radiusField.setText(Integer.toString(source.getValue()));
                radiusField.getDocument().addDocumentListener(radiusFieldListener);
            }
        });
    }

    private void initInnerRadiusField() {
        innerRadiusFieldListener = new DocumentListener() {
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
        innerRadiusField = new JTextField();
        innerRadiusField.setText(Integer.toString(drawingPanel.getShapeTool().innerRadius));
        innerRadiusField.getDocument().addDocumentListener(innerRadiusFieldListener);
    }
    private void initInnerRadiusSlider() {
        innerRadiusSlider = new JSlider(0, drawingPanel.getHeight(), drawingPanel.getShapeTool().innerRadius);
        innerRadiusSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (source.getValueIsAdjusting()) {
                innerRadiusField.getDocument().removeDocumentListener(innerRadiusFieldListener);
                innerRadiusField.setText(Integer.toString(source.getValue()));
                innerRadiusField.getDocument().addDocumentListener(innerRadiusFieldListener);
            }
        });
    }

    private void initRotationField() {
        rotationFieldListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                rotationSlider.setValue(getRotation());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                rotationSlider.setValue(getRotation());
            }
        };
        rotationField = new JTextField();
        rotationField.setText(Integer.toString(drawingPanel.getShapeTool().rotation));
    }

    private void initRotationSlider() {
        rotationSlider = new JSlider(0, 360, drawingPanel.getShapeTool().rotation);
        rotationSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (source.getValueIsAdjusting()) {
                rotationField.getDocument().removeDocumentListener(rotationFieldListener);
                rotationField.setText(Integer.toString(source.getValue()));
                rotationField.getDocument().addDocumentListener(rotationFieldListener);
            }
        });
    }



    private void initComponents() {
        setResizable(false);
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JLabel radiusLabel = new JLabel("Радиус:");
        JLabel rotationLabel = new JLabel("Поворот (градусы):");

        initRadiusField();
        initRadiusSlider();

        initInnerRadiusField();
        initInnerRadiusSlider();

        initRotationField();
        initRotationSlider();


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
