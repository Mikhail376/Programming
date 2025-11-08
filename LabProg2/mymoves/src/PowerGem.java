import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class PowerGem extends SpecialMove{
    public PowerGem(double pow, double acc){
        super(Type.ROCK, pow, acc);
    }

    public String describe(){
        String[] pieces = this.getClass().toString().split("\\.");
        return "does " + pieces[pieces.length - 1];
    }
}