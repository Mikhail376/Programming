import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Bellossom extends Pokemon{
    public Bellossom(String name, int level1){
        super(name, level1);

        super.setType(Type.GRASS);
        super.setStats(75, 80, 95, 90, 100, 50);
        Facade facade = new Facade(70, 100);
        StunSpore stunSpore = new StunSpore(0, 75);
        Growth growth = new Growth(0, 0);
        DazzlingGleam dazzlingGleam = new DazzlingGleam(80, 100);
        super.setMove(facade, stunSpore, growth, dazzlingGleam);
    }
}
