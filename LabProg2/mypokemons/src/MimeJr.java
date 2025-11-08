import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class MimeJr extends Pokemon {
    public MimeJr(String name, int level1){
        super(name, level1);

        super.setType(Type.PSYCHIC, Type.FAIRY);
        super.setStats(20, 25, 45, 70, 90, 60);
        Psybeam psybeam = new Psybeam(65, 100);
        Psychic psychic = new Psychic(90, 100);
        TeeterDance teeterDance = new TeeterDance(0, 100);
        super.setMove(psybeam, psychic, teeterDance);
    }
}
