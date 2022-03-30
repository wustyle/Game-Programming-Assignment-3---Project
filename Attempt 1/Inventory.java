import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;



public class Inventory extends JPanel{

    private ArrayList<Item> items;
    
    public Inventory() {
        setSize(500, 500);

        setLayout(new GridLayout(10, 10));

        setLocation(getBounds().width - 500, getBounds().height - 500);

        items = new ArrayList<>(100);

        setBackground(Color.WHITE);
        
    }

    private void updateList() {
        Component[] componentList = getComponents();

        //Loop through the components
        for(Component c : componentList){
                remove(c);
        }

        for (Item item : items) {
            add(new JLabel(item.getIcon()));
        }
        //IMPORTANT
        revalidate();
        repaint();

        
    }

    public void addItem(String name) {
        if (items.size() < 100) {
            items.add(new Item(name));
        } 
        
    }

    public void removeItem(Item i) {
        /* for (Item item : items) {
            if (item.equals(i)) {
                items.remove(item);
            }
        } */

        items.remove(i);
        
    }

    public void removeItem(ImageIcon i) {
        for (Item item : items) {
            if (i.equals(item.getIcon())) {
                items.remove(item);
            }
        }

    }

    public boolean isFull() {
        if (items.size() >= 100) {
            return true;
        }

        return false;
    }
}
