import ru.ifmo.se.pokemon.*;
import static ru.ifmo.se.pokemon.Effect.*;
public class DazzlingGleam extends SpecialMove {
    public DazzlingGleam(double pow, double acc){
        super(Type.FAIRY, pow, acc);
    }
    public String describe(){
        String[] pieces = this.getClass().toString().split("\\.");
        return "does " + pieces[pieces.length - 1];
    }

}