
/**
 * Write a description of class GridSpace here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class GridSpace
{
    private int xPos;
    private int yPos;
    private String display;
    private Ship ship;
    private boolean checked;
    private boolean hit;
    private boolean marker;
    
    public GridSpace(int newX, int newY)
    {
        xPos = newX;
        yPos = newY;
        ship = null;
        checked = false;
        hit = false;
    }
    
    public void setCurrentPlayerDisplay()
    {
        if (hit)
        {
            display = "X";
        }
        else if (ship != null)
        {
            display = ship.getLetter();
        }
        else if (checked)
        {
            display = "*";
        }
        else if (marker)
        {
            display = ":";
        }
        else
        {
            display = "~";
        }
    }
    
    public void setOpposingPlayerDisplay()
    {
        if (hit)
        {
            display = "X";
        }
        else if (checked)
        {
            display = "*";
        }
        else 
        {
            display = "~";
        }
    }
    
    public int getX()
    {
        return xPos;
    }
    
    public void setX(int newX)
    {
        xPos = newX;
    }
    
    public int getY()
    {
        return yPos;
    }
    
    public void setY(int newY)
    {
        yPos = newY;
    }
    
    public String getDisplay()
    {
        return display;
    }
    
    public void setDisplay(String newDisplay)
    {
        display = newDisplay;
    }
    
    public Ship getShip()
    {
        return ship;
    }
    
    public void setShip(Ship newShip)
    {
        ship = newShip;
    }
    
    public boolean getChecked()
    {
        return checked;
    }
    
    public void setChecked(boolean newChecked)
    {
        checked = newChecked;
    }
    
    public boolean getHit()
    {
        return hit;
    }
    
    public void setHit(boolean newHit)
    {
        hit = newHit;
    }
    
    public boolean getMarker()
    {
        return marker;
    }
    
    public void setMarker(boolean newMarker)
    {
        marker = newMarker;
    }
      
    public boolean checkForHit()
    {
        if (ship != null)
        {
            hit = true;
            return true;
        }
        else
        {
            return false;
        }
    }
}
