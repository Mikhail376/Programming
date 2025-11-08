import ru.ifmo.se.pokemon.*;
import static ru.ifmo.se.pokemon.Effect.*;
public class FocusBlast extends SpecialMove {
    public FocusBlast(double pow, double acc){
        super(Type.FIGHTING, pow, acc);
    }
    public String describe(){
        String[] pieces = this.getClass().toString().split("\\.");
        return "does " + pieces[pieces.length - 1];
    }
    protected void applyOppEffects(Pokemon p){
        super.applyOppEffects(p);
        if(Math.random() < 0.1){;
            Effect e = new Effect().stat(Stat.SPECIAL_DEFENSE, -1);
            p.addEffect(e);
        }
    }
}