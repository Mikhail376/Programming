import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class MrMime extends MimeJr {
    public MrMime(String name, int level1){
        super(name, level1);

        super.setType(Type.PSYCHIC, Type.FAIRY);
        super.setStats(40, 45, 65, 100, 120, 90);
        Psybeam psybeam = new Psybeam(65, 100);
        Psychic psychic = new Psychic(90, 100);
        TeeterDance teeterDance = new TeeterDance(0, 100);
        FocusBlast focusBlast = new FocusBlast(120, 70);
        super.setMove(psybeam, psychic, teeterDance, focusBlast);
    }
}
