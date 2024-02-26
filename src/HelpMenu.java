import javax.swing.*;

public class HelpMenu extends JMenu {
    public HelpMenu() {
        super("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About program");
        String aboutMessage = "ICGPaint is program for simple 2D graphics.\n Shaikhutdinov Leonid©";
        aboutMenuItem.addActionListener(e->JOptionPane.showMessageDialog(this,  aboutMessage));
        add(aboutMenuItem);

        JMenuItem instruction = new JMenuItem("Instruction");
        String instructionText = "* \t\tИнтерфейс:\n" +
                "    * После запуска приложения вы увидите основной интерфейс, включающий в себя область рисования, меню и панель инструментов (toolbar).\n" +
                "* \t\tВыбор инструмента:\n" +
                "    * Используйте кнопки на панели инструментов или соответствующие пункты в меню для выбора инструмента: \"Линия\", \"Штамп\", \"Заливка\". Выделенный инструмент будет отображаться на панели инструментов с помощью \"залипающих\" кнопок.\n" +
                "* \t\tНастройка параметров инструментов:\n" +
                "        * Линия:\n" +
                "        * Нажмите кнопку «настройка» на панели инструментов или выберите соответствующий пункт в меню.\n" +
                "        * В появившемся диалоговом окне укажите желаемую толщину линии.\n" +
                "        * Штамп:\n" +
                "        * Нажмите кнопку «настройка» на панели инструментов или выберите соответствующий пункт в меню.\n" +
                "        * В появившемся диалоговом окне выберите форму многоугольника (от 3 до n вершин) и укажите его радиус и внутренний радиус (в случае, если вы хотите нарисовать звезду).\n" +
                "        * Также выберите желаемый поворот многоугольника.\n" +
                "        * Заливка:\n" +
                "        * Выберите инструмент \"Заливка\".\n" +
                "        * Нажмите ЛКМ на точке, с которой начнется заливка.\n" +
                "* \t\tВыбор цвета:\n" +
                "    * Выберите цвет для рисования из предустановленных в панели инструментов или выберите \"Другой цвет\" для выбора из палитры.\n" +
                "* \t\tОчистка области рисования:\n" +
                "    * Нажмите кнопку \"Очистка\" на панели инструментов или выберите соответствующий пункт в меню.\n" +
                "* \t\tСохранение и загрузка:\n" +
                "    * Для сохранения текущей области рисования выберите пункт «Save as» в меню и укажите место сохранения и формат файла (.png). При нажатии «Save» файл сохранится по указанному ранее пути.\n" +
                "    * Для загрузки изображения используйте пункт меню «Open»\n" +
                "* \t\tИнформация о программе:\n" +
                "    * Откройте меню «Help», чтобы получить информацию об авторе и программе, а также основные особенности использования.\n" +
                "* \t\tИзменение размеров области рисования:\n" +
                "    * Измените размер окна приложения. При увеличении размера окна вы увидите, что область рисования также увеличивается, и наоборот. При этом текущий рисунок не должен теряться.\n" +
                "* \t\tЗавершение работы:\n" +
                "    * Для завершения работы приложения закройте его, нажав на кнопку \"Закрыть\" или используйте стандартные средства вашей операционной системы.";
        instruction.addActionListener(e->JOptionPane.showMessageDialog(this,  instructionText));
        add(instruction);

    }
}
