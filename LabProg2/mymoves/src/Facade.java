import ru.ifmo.se.pokemon.*;

public class Facade extends SpecialMove {
    public Facade(double pow, double acc){
        super(Type.NORMAL, pow, acc);
    }
    public String describe(){
        String[] pieces = this.getClass().toString().split("\\.");
        return "does " + pieces[pieces.length - 1];
    }
    protected double calcBaseDamage(Pokemon attacker, Pokemon defender) {
        double basePower = super.calcBaseDamage(attacker, defender);


        Status status = attacker.getCondition();
        if (status == Status.BURN || status == Status.POISON || status == Status.PARALYZE) {
            basePower *= 2;
        }

        return basePower;
        }
}
