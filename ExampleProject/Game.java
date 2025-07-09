import java.util.concurrent.ThreadLocalRandom;

public class Game {
    private Scene scene;
    private CharacterController CharacterController1;
    private Input input;

    public void run() {
        scene = new Scene();
        CharacterController1 = new CharacterController();
        input = new Input();

        // Player erstellen
        Entity player = scene.CreateEntity();
        player.SetName("Player");
        player.AddComponent(new VektorComponent());

        // Gegner erstellen
        Entity enemy = scene.CreateEntity();
        enemy.SetName("Enemy 1");
        enemy.AddComponent(new VektorComponent());
        enemy.AddComponent(new SpriteComponent());
        enemy.AddComponent(new BaseComponent());

        //Setzt den Gegner nach oben links Anfangs auf die Map
        enemy.Move(100, 100);

        //Map erstellen
        Entity map = scene.CreateEntity();
        map.SetName("Map");


        //Erstellt eine 100x100 Grid in die die Map unterteilt ist
        int[][] map_grid = map_grid[100][100];

        //Erstell random Obstacles
        for(int i = 0; i < 100; i++)
        {
            map_grid[ThreadLocalRandom.current().nextInt(0, 100)][ThreadLocalRandom.current().nextInt(0, 100)] = 1;
            System.out.println(map_grid);
        }

        while (true) { // Game Loop
            CharacterController1.update(scene, input);
            
        }
    }

    //Gegner pathfinding
    void pathfinding()
        {
            int spieler_y = player.GiveY();
            int spieler_x = player.GiveX();

            int diff_zu_spieler_x = spieler_x - enemy.GiveX();
            int diff_zu_spieler_y = spieler_y - enemy.GiveY();
        }
}

