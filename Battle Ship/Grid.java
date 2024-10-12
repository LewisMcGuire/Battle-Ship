import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Write a description of class Grid here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Grid
{
    private GridSpace[] playerGrid;
    
    public Grid()
    {
        playerGrid = new GridSpace[100];
        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                playerGrid[i*10 + j] = new GridSpace(j,i);
            }
        }
    }
    
    public void displayGrid(boolean currentPlayer)
    {
        System.out.println("  A B C D E F G H I J");
        for (int i = 0; i < 10; i++)
        {
            System.out.print(i + " ");
            for (int j = 0; j < 10; j++)
            {   
                if (currentPlayer)
                {
                    playerGrid[i*10+j].setCurrentPlayerDisplay();
                }
                else
                {
                    playerGrid[i*10+j].setOpposingPlayerDisplay();
                }
        
                System.out.print(playerGrid[i*10+j].getDisplay() + " ");    
            }
            System.out.println();
        }
    }
    
    public JButton[][] createGridGUI(boolean currentPlayer, JPanel panel)
    {
        int labelX = 140;
        int labelY = 100;
                
        String alphabet[] = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        JLabel xAxis[] = new JLabel[10];
        for (int i = 0; i < 10; i++)
        {
            xAxis[i] = createLabel(alphabet[i], labelX, 50, 30, panel);
            xAxis[i].setHorizontalAlignment(SwingConstants.CENTER);
            labelX += 40;
        }
        
        JButton[][] rows = new JButton[10][];
        JLabel yAxis[] = new JLabel[10]; 
        for (int i = 0; i < 10; i++)
        {
            yAxis[i] = createLabel(Integer.toString(i), 100, labelY, 30, panel);
            
            rows[i] = new JButton[10];
            labelX = 140;
            for (int j = 0; j < 10; j++)
            {   
                if (currentPlayer)
                {
                    playerGrid[i*10+j].setCurrentPlayerDisplay();
                }
                else
                {
                    playerGrid[i*10+j].setOpposingPlayerDisplay();
                }
                
                rows[i][j] = createButton(playerGrid[i*10+j].getDisplay(), labelX, labelY, panel);
                //rows[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                labelX += 40;
            }
            
            labelY += 50;
        }
        return rows;
    }
    
    public void updateGridGUI(boolean currentPlayer, JButton[][] rows)
    {
        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                if (currentPlayer)
                {
                    playerGrid[i*10+j].setCurrentPlayerDisplay();
                }
                else
                {
                    playerGrid[i*10+j].setOpposingPlayerDisplay();
                }
                rows[i][j].setText(playerGrid[i*10+j].getDisplay());
            }
        }
    }
    
    public JLabel createLabel(String message, int xPos, int yPos, int length, JPanel panel)
    {
        Font f = new Font("serif", Font.PLAIN, 30);
        JLabel label = new JLabel(message);
        label.setFont(f);
        label.setBounds(xPos, yPos, length, 40);
        panel.add(label);
        return label;
    }
    
    public JButton createButton(String message, int xPos, int yPos, JPanel panel)
    {
        Font f = new Font("serif", Font.PLAIN, 30);
        JButton button = new JButton(message);
        button.setBounds(xPos, yPos, 40, 40);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setFont(f);
        panel.add(button);
        return button;
    }
    
    public void displayOneLine(boolean currentPlayer, int row)
    {
        for (int i = 0; i < 10; i++)
        {
            if (currentPlayer)
            {
                playerGrid[row*10+i].setCurrentPlayerDisplay();
            }
            else
            {
                playerGrid[row*10+i].setOpposingPlayerDisplay();
            }
            System.out.print(playerGrid[row*10+i].getDisplay() + " ");
        }
    }
    
    public JButton[] displayOneLineGUI(boolean currentPlayer, int row, JPanel panel)
    {
        JButton[] enemySpace = new JButton[10];
        for (int i = 0; i < 10; i++)
        {
            if (currentPlayer)
            {
                playerGrid[row*10+i].setCurrentPlayerDisplay();
                JLabel yourSpace = createLabel(playerGrid[row*10+i].getDisplay(), i * 40 + 140, row * 50 + 150, 30, panel);
                yourSpace.setHorizontalAlignment(SwingConstants.CENTER);
            }
            else
            {
                playerGrid[row*10+i].setOpposingPlayerDisplay();
                enemySpace[i] = createButton(playerGrid[row*10+i].getDisplay(), i * 40 + 640, row * 50 + 150, panel);
            }
        }
        return enemySpace;
    }
    
    public GridSpace[] getPlayerGrid()
    {
        return playerGrid;
    }
}
