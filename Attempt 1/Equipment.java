public class Equipment extends Item{

    private int atk;
    private int mag;
    private int health;



    public Equipment(String n) {
        super(n);
        //TODO Auto-generated constructor stub

        if (name == "sword") {
           atk = 5;
           mag = 0;
           health = 0;
        }
    }
    
    public int getAtk() {
        return atk;
    }
    
}
