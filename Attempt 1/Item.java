import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Item
 * will be used in arrays
 */
public class Item {

    private String name;
    private Image image;
    private ImageIcon icon;

    public Item(String n) {
        name = n;

        setImage();
    }

    private void setImage() {
        if (name == "") {
            icon = new ImageIcon("");
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