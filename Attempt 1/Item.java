import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Item
 * will be used in arrays
 */
public class Item {

    protected String name;
    private Image image;
    private ImageIcon icon;

    public Item(String n) {
        name = n;

        setImage();
    }

    private void setImage() {
        if (name == "potion") {
            icon = new ImageIcon("images/items/PNG/Background/Icon1.png");
            image = icon.getImage();
            
        }
        else if (name == "sword") {
            icon = new ImageIcon("images/weapons icons/noglow/tile000.png");
            image = icon.getImage();
            
        }
        else if (name == "shield") {
            icon = new ImageIcon("images/weapons icons/noglow/tile049.png");
            image = icon.getImage();
            
        }
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public ImageIcon getIcon() {
        return icon;
    }
    
}

//Hello Test test test test test


// Second attempt hello world
