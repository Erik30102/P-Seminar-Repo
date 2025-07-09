public class CharacterController{
    public void update(Scene scene, Input input) {
        for (Entity entity : scene.GetEntites()) {
            for (Component component : entity.getComponents()) {
                if (component instanceof VektorComponent) {
                    VektorComponent pos = (VektorComponent) component;
                    if (input.isKeyPressed("W")) pos.MoveY(-1); 
                    if (input.isKeyPressed("S")) pos.MoveY(1);  
                    if (input.isKeyPressed("A")) pos.MoveX(-1); 
                    if (input.isKeyPressed("D")) pos.MoveX(1);  
                }
            }
        }
    }
}

