import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Palkia extends Pokemon{
    public Palkia(String name, int level1){
        super(name, level1);

        super.setType(Type.WATER, Type.DRAGON);
        super.setStats(90, 120, 100, 150, 120, 100);
        DragonClaw dragonClaw = new DragonClaw(80, 100);
        PowerGem powerGem = new PowerGem(80, 100);
        DracoMeteor dracoMeteor = new DracoMeteor(130, 90);
        Swagger swagger = new Swagger(0, 85);
        super.setMove(dragonClaw, powerGem, dracoMeteor, swagger);
    }
}



