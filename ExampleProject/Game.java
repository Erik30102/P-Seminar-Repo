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

        while (true) { // Game Loop
            CharacterController1.update(scene, input);
            
        }
    }
}

