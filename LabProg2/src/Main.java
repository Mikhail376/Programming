import ru.ifmo.se.pokemon.Battle;
import ru.ifmo.se.pokemon.Pokemon;
public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Palkia palkia = new Palkia("Cjsaj", 1);
        MimeJr mimeJr = new MimeJr("asd", 1);
        MrMime mrMime = new MrMime("aSDF", 1);
        Oddish oddish = new Oddish("ijmgt", 1);
        Gloom gloom = new Gloom("bhdvb", 1);
        Bellossom bellossom = new Bellossom("iojhte", 1);
        b.addAlly(mimeJr);
        b.addAlly(mrMime);
        b.addAlly(bellossom);
        b.addFoe(palkia);
        b.addFoe(oddish);
        b.addFoe(gloom);
        b.go();
    }
}