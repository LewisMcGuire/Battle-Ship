
/**
 * Write a description of class Ship here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Ship
{
    private String name;
    private String letter;
    private int length;
    private String direction;
    private int hits;
    
    public Ship(String newName, String newLetter, int newLength)
    {
        name = newName;
        letter = newLetter;
        length = newLength;
        direction = "";
        hits = 0;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String newName)
    {
        name = newName;
    }
    
    public String getLetter()
    {
        return letter;
    }
    
    public void setLetter(String newLetter)
    {
        letter = newLetter;
    }
    
    public int getLength()
    {
        return length;
    }
    
    public void setLength(int newLength)
    {
        length = newLength;
    }
    
    public String getDirection()
    {
        return direction;
    }
    
    public void setDirection(String newDirection)
    {
        direction = newDirection;
    }
    
    public int getHits()
    {
        return hits;
    }
    
    public void setHits(int newHits)
    {
        hits = newHits;
    }
    
    public void increaseHits()
    {
        hits++;
    }
    
    public boolean checkSunk()
    {
        if (hits == length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
