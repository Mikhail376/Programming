import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Oddish extends Pokemon{
    public Oddish(String name, int level1){
        super(name, level1);

        super.setType(Type.GRASS, Type.POISON);
        super.setStats(45, 50, 55, 75, 65, 30);
        Facade facade = new Facade(70, 100);
        StunSpore stunSpore = new StunSpore(0, 75);
        super.setMove(facade, stunSpore);
    }
}
