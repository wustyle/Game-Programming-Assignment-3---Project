import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;



public class Inventory extends JPanel{

    private ArrayList<Item> items;
    
    public Inventory() {
        setSize(320, 320);

        setLayout(new GridLayout(10, 10));

        setLocation(getBounds().width - 320, getBounds().height - 320);

        items = new ArrayList<>(100);

        for (int i = 0; i < 100 ; i++) {
            addItem("");
            System.out.println(items.size());
        }

        setBackground(Color.black);
        
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
            Item item = new Item(name);
            items.add(item);
            add(new JLabel(item.getIcon()));
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
