import ru.ifmo.se.pokemon.*;
import static ru.ifmo.se.pokemon.Effect.*;
public class Growth extends StatusMove {
    public Growth(double pow, double acc){
        super(Type.NORMAL, pow, acc);
    }
    public String describe(){
        String[] pieces = this.getClass().toString().split("\\.");
        return "does " + pieces[pieces.length - 1];
    }
    protected void applySelfEffects(Pokemon p){
        super.applySelfEffects(p);
        Effect e = new Effect().stat(Stat.SPECIAL_ATTACK,1);
        p.addEffect(e);
        Effect a = new Effect().stat(Stat.ATTACK,1);
        p.addEffect(a);
    }
}