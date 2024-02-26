import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;

// Default layout for JFrame = BorderLayout
public class MainWindow extends JFrame {
    private JToggleButton currentColorButton;
    private JToggleButton currentToolButton;

    private void onColorButtonPressed(JToggleButton newColorButton) {
        if (currentColorButton != newColorButton) {
            currentColorButton.setSelected(false);
            currentColorButton = newColorButton;
        }
        else {
            currentColorButton.setSelected(true);
        }
    }

    private void onToolButtonPressed(JToggleButton newToolButton) {
        if (currentToolButton != newToolButton) {
            currentToolButton.setSelected(false);
            currentToolButton = newToolButton;
        }
        else {
            currentToolButton.setSelected(true);
        }
    }
    private void menuInit(DrawingPanel drawingPanel) {
        JMenuBar menuBar = new JMenuBar();
        JMenu aboutMenu = new HelpMenu();
        JMenu editMenu = new EditMenu(drawingPanel);
        JMenu fileMenu = new FileMenu(drawingPanel);
        JMenu toolsMenu = new ToolsMenu(drawingPanel);


        menuBar.add(aboutMenu);
        menuBar.add(editMenu);
        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);

        setJMenuBar(menuBar);
    }

    private void toolbarInit(DrawingPanel drawingPanel) {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);

        // Undo button
        JButton undoButton = new JButton();
        undoButton.addActionListener(e -> drawingPanel.undo());
        ImageIcon undoButtonIcon = new ImageIcon("src/resources/undoButtonIcon.png");
        undoButton.setIcon(undoButtonIcon);
        undoButton.setToolTipText("Undo last action");
        toolBar.add(undoButton);

        // Clean button
        JButton cleanButton = new JButton();
        cleanButton.addActionListener(e -> drawingPanel.clean());
        ImageIcon cleanButtonIcon = new ImageIcon("src/resources/deleteIcon.png");
        cleanButton.setIcon(cleanButtonIcon);
        cleanButton.setToolTipText("Cleaning the work area");
        toolBar.add(cleanButton);

        toolBar.addSeparator();

        // Line tool button
        JToggleButton lineToolButton = new JToggleButton();
        lineToolButton.addActionListener(e -> drawingPanel.setCurrentTool(DrawingPanel.Tool.LINE));
        lineToolButton.addActionListener(e -> onToolButtonPressed(lineToolButton));
        ImageIcon lineToolIcon = new ImageIcon("src/resources/lineToolIcon.png");
        lineToolButton.setIcon(lineToolIcon);
        lineToolButton.setToolTipText("Draws a line from two points");
        toolBar.add(lineToolButton);

        // Fill tool button
        JToggleButton fillToolButton = new JToggleButton();
        fillToolButton.addActionListener(e -> drawingPanel.setCurrentTool(DrawingPanel.Tool.FILL));
        fillToolButton.addActionListener(e -> onToolButtonPressed(fillToolButton));
        ImageIcon fillToolIcon = new ImageIcon("src/resources/fillToolIcon.png");
        fillToolButton.setIcon(fillToolIcon);
        fillToolButton.setToolTipText("Fills the selected enclosed area");
        toolBar.add(fillToolButton);

        // Shape tool button
        JToggleButton shapeToolButton = new JToggleButton();
        shapeToolButton.addActionListener(e -> drawingPanel.setCurrentTool(DrawingPanel.Tool.SHAPE));
        shapeToolButton.addActionListener(e -> onToolButtonPressed(shapeToolButton));
        ImageIcon shapeToolIcon = new ImageIcon("src/resources/shapeToolIcon.png");
        shapeToolButton.setIcon(shapeToolIcon);
        shapeToolButton.setToolTipText("Draws a polygon or star");
        toolBar.add(shapeToolButton);

        // Pen tool button
        JToggleButton penToolButton = new JToggleButton();
        penToolButton.addActionListener(e -> drawingPanel.setCurrentTool(DrawingPanel.Tool.PEN));
        penToolButton.addActionListener(e -> onToolButtonPressed(penToolButton));
        ImageIcon penToolIcon = new ImageIcon("src/resources/penToolIcon.png");
        penToolButton.setIcon(penToolIcon);
        penToolButton.setToolTipText("Draws continues line while pressed");
        toolBar.add(penToolButton);

        toolBar.addSeparator();

        // Settings button
        JButton toolSettingsButton = new JButton();
        toolSettingsButton.addActionListener(e ->drawingPanel.openSettingsWindow());

        ImageIcon toolSettingsButtonIcon = new ImageIcon("src/resources/settingsIcon.png");
        toolSettingsButton.setIcon(toolSettingsButtonIcon);
        toolSettingsButton.setToolTipText("Tool settings");
        toolBar.add(toolSettingsButton);

        toolBar.addSeparator();

        // Black color button
        JToggleButton blackColorButton = new JToggleButton();
        blackColorButton.setBackground(Color.BLACK);
        blackColorButton.addActionListener(e -> drawingPanel.setCurrentColor(Color.BLACK));
        blackColorButton.addActionListener(e -> onColorButtonPressed(blackColorButton));
        ImageIcon blackColorButtonIcon = new ImageIcon("src/resources/blackColorButtonIcon.png");
        blackColorButton.setIcon(blackColorButtonIcon);
        blackColorButton.setToolTipText("Set current color black");
        toolBar.add(blackColorButton);

        // Red color button
        JToggleButton redColorButton = new JToggleButton();
        redColorButton.addActionListener(e -> drawingPanel.setCurrentColor(Color.RED));
        redColorButton.addActionListener(e -> onColorButtonPressed(redColorButton));
        ImageIcon redColorButtonIcon = new ImageIcon("src/resources/redColorButtonIcon.png");
        redColorButton.setIcon(redColorButtonIcon);
        redColorButton.setToolTipText("Set current color red");
        toolBar.add(redColorButton);

        // Green color button
        JToggleButton greenColorButton = new JToggleButton();
        greenColorButton.addActionListener(e -> drawingPanel.setCurrentColor(Color.GREEN));
        greenColorButton.addActionListener(e -> onColorButtonPressed(greenColorButton));
        ImageIcon greenColorButtonIcon = new ImageIcon("src/resources/greenColorButtonIcon.png");
        greenColorButton.setIcon(greenColorButtonIcon);
        greenColorButton.setToolTipText("Set current color green");
        toolBar.add(greenColorButton);

        // Blue color button
        JToggleButton blueColorButton = new JToggleButton();
        blueColorButton.addActionListener(e -> drawingPanel.setCurrentColor(Color.BLUE));
        blueColorButton.addActionListener(e -> onColorButtonPressed(blueColorButton));
        ImageIcon blueColorButtonIcon = new ImageIcon("src/resources/blueColorButtonIcon.png");
        blueColorButton.setIcon(blueColorButtonIcon);
        blueColorButton.setToolTipText("Set current color blue");
        toolBar.add(blueColorButton);

        // Cyan color button
        JToggleButton cyanColorButton = new JToggleButton();
        cyanColorButton.addActionListener(e -> drawingPanel.setCurrentColor(Color.CYAN));
        cyanColorButton.addActionListener(e -> onColorButtonPressed(blueColorButton));
        ImageIcon cyanColorButtonIcon = new ImageIcon("src/resources/cyanColorButtonIcon.png");
        cyanColorButton.setIcon(cyanColorButtonIcon);
        cyanColorButton.setToolTipText("Set current color cyan");
        toolBar.add(cyanColorButton);

        // Magenta color button
        JToggleButton magentaColorButton = new JToggleButton();
        magentaColorButton.addActionListener(e -> drawingPanel.setCurrentColor(Color.MAGENTA));
        magentaColorButton.addActionListener(e -> onColorButtonPressed(magentaColorButton));
        ImageIcon magentaColorButtonIcon = new ImageIcon("src/resources/magentaColorButtonIcon.png");
        magentaColorButton.setIcon(magentaColorButtonIcon);
        magentaColorButton.setToolTipText("Set current color magenta");
        toolBar.add(magentaColorButton);

        // Yellow color button
        JToggleButton yellowColorButton = new JToggleButton();
        yellowColorButton.addActionListener(e -> drawingPanel.setCurrentColor(Color.YELLOW));
        yellowColorButton.addActionListener(e -> onColorButtonPressed(yellowColorButton));
        ImageIcon yellowColorButtonIcon = new ImageIcon("src/resources/yellowColorButtonIcon.png");
        yellowColorButton.setIcon(yellowColorButtonIcon);
        yellowColorButton.setToolTipText("Set current color yellow");
        toolBar.add(yellowColorButton);

        // White color button
        JToggleButton whiteColorButton = new JToggleButton();
        whiteColorButton.addActionListener(e -> drawingPanel.setCurrentColor(Color.WHITE));
        whiteColorButton.addActionListener(e -> onColorButtonPressed(whiteColorButton));
        ImageIcon whiteColorButtonIcon = new ImageIcon("src/resources/whiteColorButtonIcon.png");
        whiteColorButton.setIcon(whiteColorButtonIcon);
        whiteColorButton.setToolTipText("Set current color white");
        toolBar.add(whiteColorButton);

        JButton paletteButton = new JButton();
        paletteButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(drawingPanel, "Выберите цвет", Color.BLACK);
            if (selectedColor != null) {
                drawingPanel.setCurrentColor(selectedColor);
            }
        });
        ImageIcon paletteButtonIcon = new ImageIcon("src/resources/paletteIcon.png");
        paletteButton.setIcon(paletteButtonIcon);
        paletteButton.setToolTipText("Open palette for chose current color");
        toolBar.add(paletteButton);

        currentColorButton = blackColorButton;
        blackColorButton.setSelected(true);
        currentToolButton = lineToolButton;
        lineToolButton.setSelected(true);
    }


    public MainWindow() {
        super("ICGPaint");
        try {
            setPreferredSize(new Dimension(640, 480));
            //setMinimumSize(new Dimension(640, 480));
            setLocation(0, 0);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            DrawingPanel drawingPanel = new DrawingPanel();
            JScrollPane scrollPane = new JScrollPane(drawingPanel);
            getContentPane().add(scrollPane);
            toolbarInit(drawingPanel);
            menuInit(drawingPanel);

            pack();
            setVisible(true);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
