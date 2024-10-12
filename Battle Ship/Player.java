
/**
 * Write a description of class Player here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Player
{
    private String name;
    private int shipsAlive;
    
    public Player()
    {
        name = "";
        shipsAlive = 5;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String newName)
    {
        name = newName;
    }
    
    public int getShipsAlive()
    {
        return shipsAlive;
    }
    
    public void setShipsAlive(int newShipsAlive)
    {
        shipsAlive = newShipsAlive;
    }
    
    public void decreaseShips()
    {
        shipsAlive--;
    }
    
    public boolean checkLoss()
    {
        if (shipsAlive == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
