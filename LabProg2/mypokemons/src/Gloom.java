import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Gloom extends Oddish{
    public Gloom(String name, int level1){
        super(name, level1);

        super.setType(Type.GRASS, Type.POISON);
        super.setStats(60, 65, 70, 85, 75, 40);
        Facade facade = new Facade(70, 100);
        StunSpore stunSpore = new StunSpore(0, 75);
        Growth growth = new Growth(0, 0);
        super.setMove(facade, stunSpore, growth);
    }
}
