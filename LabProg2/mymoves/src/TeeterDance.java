import ru.ifmo.se.pokemon.*;
import static ru.ifmo.se.pokemon.Effect.*;
public class TeeterDance extends SpecialMove {
    public TeeterDance(double pow, double acc){
        super(Type.NORMAL, pow, acc);
    }
    public String describe(){
        String[] pieces = this.getClass().toString().split("\\.");
        return "does " + pieces[pieces.length - 1];
    }
    public void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        confuse(p);
        int turns = 1 + (int)(Math.random() * 4);
        Effect e = new Effect()
                .turns(turns);
        if (Math.random() < 0.33){
            System.out.println(p + " hurt itself in confusion!");
            int damage = (int) p.getStat(Stat.ATTACK);
            p.setMod(Stat.HP, damage);
        }
    }
}