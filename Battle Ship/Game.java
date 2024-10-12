import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Write a description of class Game here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Game
{
    private Player[] players;
    private Grid[] grids;
    private int currentPlayer;
    private int otherPlayer;
    private Ship[][] fleets;
    private String[] letters;
    
    public Game()
    {
        players = new Player[2];
        grids = new Grid[2];
        fleets = new Ship[2][];
        for (int i = 0; i < 2; i++)
        {
            players[i] = new Player();
            grids[i] = new Grid();
            fleets[i] = new Ship[5];
        }
        currentPlayer = 0;
        otherPlayer = 1;
        letters = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    }
    
    public void newGame()
    {
        initialise();
        System.out.print('\u000C');
        System.out.println("Player 1");
        System.out.println("Enter name:");
        players[0].setName(getString());
        
        System.out.print('\u000C');
        System.out.println("Player 2");
        System.out.println("Enter name:");
        players[1].setName(getString());
        
        System.out.print('\u000C');
        placeShips(grids[0], fleets[0], players[0]);
        placeShips(grids[1], fleets[1], players[1]);
        
        boolean endGame = false;
        
        do
        {
            playerTurn();
            changePlayer();
            endGame = players[currentPlayer].checkLoss();
        } 
        while(!endGame);
        
        System.out.println(players[otherPlayer].getName() + " Wins");
        System.out.println("Press Enter to Continue");
        getString();
    }
    
    public void newGameGUI(JFrame frame)
    {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        initialise();
        
        // Player 1
        createLabel("Player 1", 100, 50, 150, panel);
        createLabel("Enter name:", 100, 100, 150, panel);
        JTextField player1 = createTextField(250, 110, 150, panel);
        
        // Player 2
        createLabel("Player 2", 100, 150, 150, panel);
        createLabel("Enter name:", 100, 200, 150, panel);
        JTextField player2 = createTextField(250, 210, 150, panel);
        
        JButton submit = createButton("Submit", 100, 250, panel);
        
        /*submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                players[0].setName(player1.getText());
                players[1].setName(player2.getText());
                
                placeShipsGUI(grids[0], fleets[0], players[0], panel, 0, "");
                startSecondTaskWhenReady();
            } 
            
            private void startSecondTaskWhenReady() {
                Timer checkTimer = new Timer(500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (firstTaskDone) {
                            ((Timer) e.getSource()).stop();
                            placeShipsGUI(grids[1], fleets[1], players[1], panel, 0, "");
                        }
                    }
                });
                checkTimer.start();
            }
        });*/
        
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                players[0].setName(player1.getText());
                players[1].setName(player2.getText());
                
                resizeFrame(frame, 700, 800);
                placeShipsGUI(grids[0], fleets[0], players[0], panel, 0, true, "", new Runnable() {
                    public void run() {
                        placeShipsGUI(grids[1], fleets[1], players[1], panel, 0, true, "", new Runnable() {
                            public void run() {
                                resizeFrame(frame, 1140, 750);
                                playerTurnGUI("", false, panel);
                            }
                        });
                    }
                });
            } 
        });

        frame.add(panel);
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
    
    public JTextField createTextField(int xPos, int yPos, int length, JPanel panel)
    {
        JTextField textField = new JTextField();
        textField.setBounds(xPos, yPos, length, 30);
        panel.add(textField);
        return textField;
    }
    
    public JButton createButton(String message, int xPos, int yPos, JPanel panel)
    {
        JButton button = new JButton(message);
        button.setBounds(xPos, yPos, 100, 30);
        panel.add(button);
        return button;
    }
    
    public void clearPanel(JPanel panel)
    {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }
    
    public void resizeFrame(JFrame frame, int x, int y)
    {
        frame.setSize(x, y);
        frame.revalidate();
        frame.repaint();
    }
    
    public void playerTurn()
    {
        boolean valid = false;
        boolean hit = false;
        int[] xy;
        int index;
        GridSpace space;
        do
        {
            /*System.out.print('\u000C');
            System.out.println(players[currentPlayer].getName());
            System.out.println("Ships Alive: " + players[currentPlayer].getShipsAlive());
            System.out.println("Your Grid");
            grids[currentPlayer].displayGrid(true);
    
            System.out.println("Enemy Grid");
            grids[otherPlayer].displayGrid(false);
            */
            displayBothGrids();
            
            System.out.println("Enter coordinate to shoot");
            String coordinate = getValidCoordinate();
            xy = convertToXY(coordinate);
            index = xy[1]*10 + xy[0];
            space = grids[otherPlayer].getPlayerGrid()[index];
            if (!space.getChecked())
            {
                valid = true;
                space.setChecked(true);
                hit = space.checkForHit();
            }
            else
            {
                System.out.println("Space already checked");
                System.out.println("Press Enter to Continue");
                getString();
            }
        } while (!valid);
        

        /*System.out.print('\u000C');
        System.out.println(players[currentPlayer].getName());
        System.out.println("Ships Alive: " + players[currentPlayer].getShipsAlive());
        System.out.println("Your Grid");
        grids[currentPlayer].displayGrid(true);

        System.out.println("Enemy Grid");
        grids[otherPlayer].displayGrid(false);
        */
        displayBothGrids();
        
        if (hit)
        {
            System.out.println("Hit");
            space.getShip().increaseHits();
            boolean sunk = space.getShip().checkSunk();
            if (sunk)
            {
                System.out.println("You sunk their battleship");
                players[otherPlayer].decreaseShips();
            }
        }
        else
        {
            System.out.println("Miss");
        }
        System.out.println("Press Enter to Continue");
        getString();
    }
    
    public void playerTurnGUI(String message, boolean endGame, JPanel panel)
    {
        if (!endGame)
        {
            JButton[][] enemySpace = displayBothGridsGUI(panel);
            createLabel("Select coordinate to shoot", 100, 650, 500, panel);
            JLabel messageBox = createLabel(message, 600, 650, 300, panel);
            
            for (int i = 0; i < 10; i++)
            {
                for (int j = 0; j < 10; j++)
                {
                    shootEnemy(j, i, enemySpace[i][j], messageBox, panel);
                }
            }
        }
        else
        {
            displayWinner(panel);
        }
    }
    
    public void shootEnemy(int x, int y, JButton button, JLabel messageBox, JPanel panel)
    {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean valid = false;
                boolean hit = false;
                int index = y*10 + x;
                GridSpace space = grids[otherPlayer].getPlayerGrid()[index];
                if (!space.getChecked())
                {
                    valid = true;
                    space.setChecked(true);
                    hit = space.checkForHit();
                    if (hit)
                    {
                        messageBox.setText("Hit");
                        space.getShip().increaseHits();
                        boolean sunk = space.getShip().checkSunk();
                        if (sunk)
                        {
                            messageBox.setText("You sunk their battleship");
                            players[otherPlayer].decreaseShips();
                        }
                    }
                    else
                    {
                        messageBox.setText("Miss");
                    }
                }
                else
                {
                    messageBox.setText("Space already checked");
                }
                
                if (valid)
                {
                    changePlayer();
                    boolean endGame = players[currentPlayer].checkLoss();
                    playerTurnGUI(messageBox.getText(), endGame, panel);
                }
                else 
                {
                    playerTurnGUI(messageBox.getText(), false, panel);
                }
            } 
        });
    }
    
    public void displayWinner(JPanel panel)
    {
        clearPanel(panel);
        createLabel(players[otherPlayer].getName() + " WINS!!!", 100, 50, 300, panel);
    }
    
    public void displayBothGrids()
    {
        System.out.print('\u000C');
        System.out.println(players[currentPlayer].getName());
        System.out.println("Ships Alive: " + players[currentPlayer].getShipsAlive());
        System.out.println("Your Grid               Enemy Grid");
        System.out.println("  A B C D E F G H I J     A B C D E F G H I J");
        for (int i = 0; i < 10; i++)
        {
            System.out.print(i + " ");
            grids[currentPlayer].displayOneLine(true, i);
            System.out.print("  ");
            System.out.print(i + " ");
            grids[otherPlayer].displayOneLine(false, i);
            System.out.println();
        }
    }
    
    public JButton[][] displayBothGridsGUI(JPanel panel)
    {
        clearPanel(panel);
        JButton[][] enemySpace = new JButton[10][];
        JLabel playerName = createLabel(players[currentPlayer].getName(), 100, 0, 150, panel);
        createLabel("Ships Alive: " + players[currentPlayer].getShipsAlive(), 200, 0, 300, panel);
        createLabel("Your Grid", 100, 50, 150, panel);
        createLabel("Enemy Grid", 600, 50, 150, panel);
        String alphabet[] = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        for (int j = 0; j < 2; j++)
        {
            JLabel xAxis[] = new JLabel[10];
            int labelX = 140 + 500 * j;
            for (int i = 0; i < 10; i++)
            {
                xAxis[i] = createLabel(alphabet[i], labelX, 100, 30, panel);
                xAxis[i].setHorizontalAlignment(SwingConstants.CENTER);
                labelX += 40;
            }
        }
        
        for (int i = 0; i < 10; i++)
        {
            createLabel(Integer.toString(i), 100, i * 50 + 150, 30, panel);
            grids[currentPlayer].displayOneLineGUI(true, i, panel);
            createLabel(Integer.toString(i), 600, i * 50 + 150, 30, panel);
            enemySpace[i] = grids[otherPlayer].displayOneLineGUI(false, i, panel);
        }
        return enemySpace;
    }
    
    public void initialise()
    {
        String[] shipNames = new String[] 
        {   
            "Aircraft Carrier",
            "Battleship",
            "Cruiser",
            "Submarine",
            "Destroyer"
        };
        int[] shipLength = new int[]
        {
            5, 4, 3, 3, 2
        };
        char firstLetter;
        for (int i = 0; i < 5; i++)
        {
            firstLetter = shipNames[i].charAt(0);
            fleets[0][i] = new Ship(shipNames[i], String.valueOf(firstLetter), shipLength[i]);
            fleets[1][i] = new Ship(shipNames[i], String.valueOf(firstLetter), shipLength[i]);
        }
    }
    
    public void placeShips(Grid grid, Ship[] fleet, Player player)
    {
        String startCoordinate;
        int[] startXY = new int[2];
        String endCoordinate;
        int[] endXY = new int[2];
        boolean placed;
        for (int i = 0; i < 5; i++)
        {
            do
            {
                placed = false;
                System.out.print('\u000C');
                System.out.println(player.getName());
                System.out.println("Place ships");
                grid.displayGrid(true);
                System.out.println(fleet[i].getName() + " - length " + fleet[i].getLength());
                System.out.println("Enter start coordinate: ");
                startCoordinate = getValidCoordinate();
                startXY = convertToXY(startCoordinate);
                if (grid.getPlayerGrid()[startXY[1]*10+startXY[0]].getShip() != null)
                {
                    System.out.println("Ship already here");
                    System.out.println("Press Enter to Continue");
                    getString();
                    continue;
                }
                
                grid.getPlayerGrid()[startXY[1]*10+startXY[0]].setMarker(true);
                
                System.out.print('\u000C');
                System.out.println(player.getName());
                System.out.println("Place ships");
                grid.displayGrid(true);
                System.out.println(fleet[i].getName() + " - length " + fleet[i].getLength());
                System.out.println("Enter end coordinate: ");
                endCoordinate = getValidEnd(startXY[0], startXY[1], fleet[i]);
                endXY = convertToXY(endCoordinate);
                placed = placeShipOnGrid(startXY[0], startXY[1], endXY[0], endXY[1], fleet[i], grid);
                if (!placed)
                {
                    System.out.println("Ships overlap");
                    System.out.println("Press Enter to Continue");
                    getString();
                }
                grid.getPlayerGrid()[startXY[1]*10+startXY[0]].setMarker(false);
            }
            while (!placed);
        }
    }
    
    /*public void placeShipsGUI(Grid grid, Ship[] fleet, Player player, JPanel panel, int index, String errorMessage, Runnable callback)
    {
        if (index != 5)
        {
            clearPanel(panel);
            JLabel playerName = createLabel(player.getName(), 100, 0, 150, panel);
            createLabel("Place ships", 200, 0, 150, panel);
            
            JLabel[][] gridLabels = grid.createGridGUI(true, panel);
            
            JLabel shipDetails = createLabel("current ship - length ", 100, 600, 500, panel);
            JLabel coordinateMessage = createLabel("Enter coordinate: ", 100, 650, 300, panel);
            JTextField coordinateBox = createTextField(380, 660, 50, panel);
            JButton enter = createButton("Enter", 440, 660, panel);
            JLabel errorLabel = createLabel(errorMessage, 100, 700, 400, panel);
            
            shipDetails.setText(fleet[index].getName() + " - length " + fleet[index].getLength());
            coordinateMessage.setText("Enter start coordinate: ");
            
            ActionListener enterStart = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    boolean validStart = getValidCoordinateGUI(panel, coordinateBox, errorLabel);
                    if (validStart)
                    {
                        String startCoordinate = coordinateBox.getText();
                        int[] startXY = convertToXY(startCoordinate);  
                        if (grid.getPlayerGrid()[startXY[1]*10+startXY[0]].getShip() != null)
                        {
                            errorLabel.setText("Ship already here");
                            validStart = false;
                        }
                        
                        if (validStart)
                        {
                            grid.getPlayerGrid()[startXY[1]*10+startXY[0]].setMarker(true);
                            errorLabel.setText("");
                            
                            grid.updateGridGUI(true, gridLabels);
                            coordinateMessage.setText("Enter end coordinate: ");
                            enter.removeActionListener(this);
                            
                            ActionListener enterEnd = new ActionListener()
                            {
                                public void actionPerformed(ActionEvent e)
                                {
                                    boolean validEnd = getValidCoordinateGUI(panel, coordinateBox, errorLabel);
                                    if (validEnd)
                                    {
                                        String endCoordinate = coordinateBox.getText();
                                        validEnd = getValidEndGUI(startXY[0], startXY[1], fleet[index], endCoordinate, errorLabel);
                                        if (validEnd)
                                        {
                                            int[] endXY = convertToXY(endCoordinate);
                                            validEnd = placeShipOnGrid(startXY[0], startXY[1], endXY[0], endXY[1], fleet[index], grid);
                                            if (!validEnd)
                                            {
                                                errorLabel.setText("Ships overlap");
                                            }
                                        }
                                    }
                                    grid.getPlayerGrid()[startXY[1]*10+startXY[0]].setMarker(false);
                                    enter.removeActionListener(this);
                                    if (validEnd)
                                    {
                                        placeShipsGUI(grid, fleet, player, panel, index + 1, errorLabel.getText(), callback);
                                    }
                                    else
                                    {
                                        placeShipsGUI(grid, fleet, player, panel, index, errorLabel.getText(), callback);
                                    }
                                }
                            };
                            enter.addActionListener(enterEnd);
                        }
                    }
                    if (!validStart)
                    {
                        placeShipsGUI(grid, fleet, player, panel, index, errorLabel.getText(), callback);
                    }
                }
            };
            enter.addActionListener(enterStart); 
        } 
        else {
            if (callback != null) {
                callback.run();
            }
        }
    }*/
    
    public void placeShipsGUI(Grid grid, Ship[] fleet, Player player, JPanel panel, int index, boolean start, String errorMessage, Runnable callback)
    {
        if (index != 5)
        {
            clearPanel(panel);
            JLabel playerName = createLabel(player.getName(), 100, 0, 150, panel);
            createLabel("Place ships", 200, 0, 150, panel);
            
            JButton[][] gridButtons = grid.createGridGUI(true, panel);
            
            JLabel shipDetails = createLabel("current ship - length ", 100, 600, 500, panel);
            JLabel coordinateMessage = createLabel("Enter coordinate: ", 100, 650, 300, panel);
            JLabel errorLabel = createLabel(errorMessage, 100, 700, 400, panel);
            shipDetails.setText(fleet[index].getName() + " - length " + fleet[index].getLength());
            
            if (start)
            {
                coordinateMessage.setText("Select start coordinate: ");
                
                for (int i = 0; i < 10; i++)
                {
                    for (int j = 0; j < 10; j++)
                    {
                        startShipPlacement(j, i, gridButtons[i][j], grid, fleet, player, panel, index, callback);
                    }
                }
            }
            else
            {
                int[] startXY = convertToXY(errorMessage);
                grid.getPlayerGrid()[startXY[1]*10+startXY[0]].setMarker(true);
                errorLabel.setText("");
                grid.updateGridGUI(true, gridButtons);
                coordinateMessage.setText("Select end coordinate: ");
                for (int i = 0; i < 10; i++)
                {
                    for (int j = 0; j < 10; j++)
                    {
                        endShipPlacement(j, i, startXY, gridButtons[i][j], grid, fleet, player, panel, errorLabel, index, callback);
                    }
                }
            }
            
            /*ActionListener enterStart = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    boolean validStart = getValidCoordinateGUI(panel, coordinateBox, errorLabel);
                    if (validStart)
                    {
                        String startCoordinate = coordinateBox.getText();
                        int[] startXY = convertToXY(startCoordinate);  
                        if (grid.getPlayerGrid()[startXY[1]*10+startXY[0]].getShip() != null)
                        {
                            errorLabel.setText("Ship already here");
                            validStart = false;
                        }
                        
                        if (validStart)
                        {
                            grid.getPlayerGrid()[startXY[1]*10+startXY[0]].setMarker(true);
                            errorLabel.setText("");
                            
                            grid.updateGridGUI(true, gridButtons);
                            coordinateMessage.setText("Enter end coordinate: ");
                            enter.removeActionListener(this);
                            
                            ActionListener enterEnd = new ActionListener()
                            {
                                public void actionPerformed(ActionEvent e)
                                {
                                    boolean validEnd = getValidCoordinateGUI(panel, coordinateBox, errorLabel);
                                    if (validEnd)
                                    {
                                        String endCoordinate = coordinateBox.getText();
                                        validEnd = getValidEndGUI(startXY[0], startXY[1], fleet[index], endCoordinate, errorLabel);
                                        if (validEnd)
                                        {
                                            int[] endXY = convertToXY(endCoordinate);
                                            validEnd = placeShipOnGrid(startXY[0], startXY[1], endXY[0], endXY[1], fleet[index], grid);
                                            if (!validEnd)
                                            {
                                                errorLabel.setText("Ships overlap");
                                            }
                                        }
                                    }
                                    grid.getPlayerGrid()[startXY[1]*10+startXY[0]].setMarker(false);
                                    enter.removeActionListener(this);
                                    if (validEnd)
                                    {
                                        placeShipsGUI(grid, fleet, player, panel, index + 1, errorLabel.getText(), callback);
                                    }
                                    else
                                    {
                                        placeShipsGUI(grid, fleet, player, panel, index, errorLabel.getText(), callback);
                                    }
                                }
                            };
                            enter.addActionListener(enterEnd);
                        }
                    }
                    if (!validStart)
                    {
                        placeShipsGUI(grid, fleet, player, panel, index, errorLabel.getText(), callback);
                    }
                }
            };
            enter.addActionListener(enterStart); */
        } 
        else {
            if (callback != null) {
                callback.run();
            }
        }
    }
    
    public void startShipPlacement(int x, int y, JButton button, Grid grid, Ship[] fleet, Player player, JPanel panel, int index, Runnable callback)
    {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] startXY = {x, y}; 
                if (grid.getPlayerGrid()[startXY[1]*10+startXY[0]].getShip() != null)
                {
                    placeShipsGUI(grid, fleet, player, panel, index, true, "Ship alreay here", callback);
                }
                else 
                {
                    placeShipsGUI(grid, fleet, player, panel, index, false, convertToCoordinate(startXY[0], startXY[1]), callback);
                }
            }
        });
    }
    
    public void endShipPlacement(int x, int y, int[] startXY, JButton button, Grid grid, Ship[] fleet, Player player, JPanel panel, JLabel errorLabel, int index, Runnable callback)
    { 
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String endCoordinate = convertToCoordinate(x,y);
                boolean validEnd = getValidEndGUI(startXY[0], startXY[1], fleet[index], endCoordinate, errorLabel);
                if (validEnd)
                {
                    int[] endXY = convertToXY(endCoordinate);
                    validEnd = placeShipOnGrid(startXY[0], startXY[1], endXY[0], endXY[1], fleet[index], grid);
                    if (!validEnd)
                    {
                        errorLabel.setText("Ships overlap");
                    }
                }
                grid.getPlayerGrid()[startXY[1]*10+startXY[0]].setMarker(false);
                if (validEnd)
                {
                    placeShipsGUI(grid, fleet, player, panel, index + 1, true, errorLabel.getText(), callback);
                }
                else
                {
                    placeShipsGUI(grid, fleet, player, panel, index, true, errorLabel.getText(), callback);
                }
            }
        });
    }

    
    public boolean placeShipOnGrid(int startX, int startY, int endX, int endY, Ship currentShip, Grid currentGrid)
    {
        String direction = currentShip.getDirection();
        int startIndex = startY*10 + startX;
        int endIndex = endY*10 + endX;
        if (direction.equals("Down"))
        {
            if (checkForOverlap(startIndex,endIndex,10,currentGrid))
            {
                return false;
            }
            for (int i = startIndex; i <= endIndex; i += 10)
            {
                currentGrid.getPlayerGrid()[i].setShip(currentShip);
            }
        }
        else if (direction.equals("Up"))
        {
            if (checkForOverlap(endIndex,startIndex,10,currentGrid))
            {
                return false;
            }
            for (int i = endIndex; i <= startIndex; i += 10)
            {
                currentGrid.getPlayerGrid()[i].setShip(currentShip);
            }
        }
        else if (direction.equals("Right"))
        {
            if (checkForOverlap(startIndex,endIndex,1,currentGrid))
            {
                return false;
            }
            for (int i = startIndex; i <= endIndex; i++)
            {
                currentGrid.getPlayerGrid()[i].setShip(currentShip);
            }
        }
        else
        {
            if (checkForOverlap(endIndex,startIndex,1,currentGrid))
            {
                return false;
            }
            for (int i = endIndex; i <= startIndex; i++)
            {
                currentGrid.getPlayerGrid()[i].setShip(currentShip);
            }
        }
        return true;
    }
    
    public boolean checkForOverlap(int start, int end, int iterator, Grid currentGrid)
    {
        boolean overlap = false;
        for (int i = start; i <= end; i += iterator)
        {
            if (currentGrid.getPlayerGrid()[i].getShip() != null)
            {
                overlap = true;
            }
        }
        return overlap;
    }
    
    public String getValidEnd(int startX, int startY, Ship currentShip)
    {
        boolean valid = false;
        String end;
        int[] endXY = new int[2];
        int length = currentShip.getLength();
        do
        {
            end = getValidCoordinate();
            endXY = convertToXY(end);
            if (startX == endXY[0])
            {
                if (startY == endXY[1] + (length-1))
                {
                    valid = true;
                    currentShip.setDirection("Up");
                }
                else if (startY == endXY[1] - (length-1))
                {
                    valid = true;
                    currentShip.setDirection("Down");
                }
            }
            else if (startY == endXY[1])
            {
                if (startX == endXY[0] + (length-1))
                {
                    valid = true;
                    currentShip.setDirection("Left");
                }
                else if (startX == endXY[0] - (length-1))
                {
                    valid = true;
                    currentShip.setDirection("Right");
                }
            }
            if (!valid)
            {
                System.out.println("Invalid end");
            }
        } 
        while (!valid);
        return end;
    }
    
    public boolean getValidEndGUI(int startX, int startY, Ship currentShip, String end, JLabel errorMessage)
    {
        boolean valid = false;
        int[] endXY = new int[2];
        int length = currentShip.getLength();
        
        endXY = convertToXY(end);
        if (startX == endXY[0])
        {
            if (startY == endXY[1] + (length-1))
            {
                valid = true;
                currentShip.setDirection("Up");
            }
            else if (startY == endXY[1] - (length-1))
            {
                valid = true;
                currentShip.setDirection("Down");
            }
        }
        else if (startY == endXY[1])
        {
            if (startX == endXY[0] + (length-1))
            {
                valid = true;
                currentShip.setDirection("Left");
            }
            else if (startX == endXY[0] - (length-1))
            {
                valid = true;
                currentShip.setDirection("Right");
            }
        }
        if (!valid)
        {
            errorMessage.setText("Invalid end");
        }
        return valid;
    }
    
    public String getValidCoordinate()
    {
        boolean valid = false;
        String coordinate = "";
        int[] xy = new int[2];
        do 
        {
            coordinate = getString();
            if (coordinate.length() != 2)
            {
                System.out.println("Invalid coordinate length");
                continue;
            }
            try
            {
                xy = convertToXY(coordinate);
                System.out.println(xy[0] + "," + xy[1]);
                if (xy[0] == -1)
                {
                    throw new Exception("Invalid");
                }
                valid = true;
            }  
            catch (Exception e)
            {
                System.out.println("Invalid coordinate entered");
            }
        } while (!valid);
        return coordinate;
    }
    
    /*public boolean getValidCoordinateGUI(JPanel panel, JTextField coordinateBox, JLabel errorMessage)
    {
        int[] xy = new int[2];
 
        String coordinate = coordinateBox.getText();
        if (coordinate.length() != 2)
        {
            errorMessage.setText("Invalid coordinate length");
            return false;
        }
        try
        {
            xy = convertToXY(coordinate);
            //System.out.println(xy[0] + "," + xy[1]);
            if (xy[0] == -1)
            {
                throw new Exception("Invalid");
            }
            return true;
        }  
        catch (Exception e)
        {
            errorMessage.setText("Invalid coordinate entered");
        }
        return false;
    }*/
    
    public int[] convertToXY(String coordinate)
    {
        int[] xyPos = new int[2];
        String[] seperated = coordinate.split("");
        String xLetter = seperated[0];
        xyPos[0] = -1;
        for (int i = 0; i < 10; i++)
        {
            if (xLetter.equals(letters[i]))
            {
                xyPos[0] = i;
            }
        }
        xyPos[1] = Integer.parseInt(seperated[1]);
        return xyPos;
    }
    
    public String convertToCoordinate(int xPos, int yPos)
    {
        String coordinate;
        String x = letters[xPos];
        String y = String.valueOf(yPos);
        coordinate = x + y;
        return coordinate;
    }
    
    public String getString()
    {
        Scanner s = new Scanner(System.in);
        String userInput;
        userInput = s.nextLine();
        return userInput;
    }
    
    public void changePlayer()
    {
        if (currentPlayer == 1)
        {
            currentPlayer = 0;
            otherPlayer++;
        }
        else
        {
            currentPlayer++;
            otherPlayer = 0;
        }
    }
}
