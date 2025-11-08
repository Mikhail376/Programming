import ru.ifmo.se.pokemon.*;
import static ru.ifmo.se.pokemon.Effect.*;
public class StunSpore extends StatusMove {
    public StunSpore(double pow, double acc){
        super(Type.GRASS, pow, acc);
    }
    public String describe(){
        String[] pieces = this.getClass().toString().split("\\.");
        return "does " + pieces[pieces.length - 1];
    }
    protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);

        if (p.getCondition() == Status.NORMAL) {
            p.addEffect(new Effect().condition(Status.PARALYZE));
        }
    }
}