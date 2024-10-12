import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Write a description of class Menu here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Menu
{
    private Game currentGame;
    
    public static void main(String[] args)
    {
        Menu m = new Menu();
        m.startMenu();
    }
    
    Menu()
    {
        currentGame = new Game();
    }
    
    public void GUI()
    {
        JFrame frame = new JFrame("Battle Ship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setResizable(true);
        frame.setVisible(true);
        currentGame.newGameGUI(frame);
    }
    
    public void startMenu()
    {
        int userChoice;
        do
        {
            displayOptions();
            userChoice = getValidNum();
            switch(userChoice)
            {
                case 1:
                    currentGame.newGame();
                    break;
                case 2:
                    javax.swing.SwingUtilities.invokeLater(new Runnable()
                    {
                       public void run()
                       {
                           GUI();
                       }
                    });
                    break;
                case 0:
                    System.out.println("Quiting");
                    break;
                default:
                    System.out.println("Invalid Choice Entered");
                    System.out.println("Press Enter to Continue");
                    getString();
            }
        }
        while (userChoice != 0);
    }
    
    public void displayOptions()
    {
        System.out.print('\u000C');
        System.out.println("1. Play Text Version");
        System.out.println("2. Play GUI Version");
        System.out.println("0. Quit");
    }
    
    public int getValidNum()
    {
        Scanner s = new Scanner(System.in);
        boolean isValid = false;
        int userInput = -1;
        do 
        {
            try
            {
                userInput = s.nextInt();
                isValid = true;
            }
            catch (Exception e)
            {
                System.out.println("Invalid Integer Enetered");
                s.nextLine();
            }
        }
        while (!isValid);
        return userInput;
    }
    
    public String getString()
    {
        Scanner s = new Scanner(System.in);
        String userInput;
        userInput = s.nextLine();
        return userInput;
    }
}
