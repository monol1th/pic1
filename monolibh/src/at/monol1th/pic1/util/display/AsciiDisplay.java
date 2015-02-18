package at.monol1th.pic1.util.display;

import asciiPanel.AsciiPanel;
import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.util.output.ascii.AsciiPlotter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by David on 18.02.2015.
 */
public class AsciiDisplay extends JFrame
{
    private AsciiPanel terminal;
    private AsciiPlotter plotter;

    private int NX;
    private int NY;
    private Simulation s;

    private final int topSpace = 2;

    private final Color phaseSpaceColor    = new Color(200, 234, 255);
    private final Color electricFieldColor = new Color(150, 79, 58);
    private final Color chargeDensityColor = new Color(42, 45, 170);
    private final Color currentDensityColor = new Color(76, 170, 74);
    private final Color textColor          = new Color(255, 218, 139);


    public boolean drawPhaseSpace = true;
    public boolean drawElectricField = true;
    public boolean drawChargeDensity = true;
    public boolean drawCurrentDensity = true;

    public AsciiDisplay(int nx, int ny, Simulation s)
    {
        super();

        this.NX         = nx;
        this.NY         = ny;
        this.s          = s;

        this.plotter = new AsciiPlotter(this.s, this.NX, this.NY - topSpace);

        terminal = new AsciiPanel(NX, NY);
        terminal.setDefaultBackgroundColor(new Color(16, 26, 41));
        terminal.clear();
        add(terminal);
        pack();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(true);
    }

    public void update()
    {
        update(true);
    }

    public void update(boolean doRepaint)
    {
        if(doRepaint)   terminal.repaint();
        if(drawPhaseSpace)      writeCharacterArray(plotter.getPhaseSpaceOutput(), phaseSpaceColor, true);
        if(drawElectricField)   writeCharacterArray(plotter.getElectricFieldOutput(), electricFieldColor, false);
        if(drawChargeDensity)   writeCharacterArray(plotter.getChargeDensityOutput(), chargeDensityColor, false);
        if(drawCurrentDensity)   writeCharacterArray(plotter.getCurrentDensityOutput(), currentDensityColor, false);
    }

    private void writeCharacterArray(char[][] characterArray, Color color, boolean drawEmptyCharacters)
    {
        for (int y = 0; y < NY-topSpace; y++) {
            for (int x = 0; x < NX; x++) {
                if(characterArray[y][x] > 0 || drawEmptyCharacters)
                    terminal.write(characterArray[y][x], x, y+topSpace, color);
            }
        }
    }

    public void writeStatusLine(String line, int x, int y)
    {
        terminal.write(line, x, y, textColor);
    }

    public void repaint()
    {
        //terminal.clear();
        super.repaint();
    }

}
