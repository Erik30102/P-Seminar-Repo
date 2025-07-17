package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;

public class VektorComponent extends Component{
    
    private int x = 0;
    private int y = 0;

    /** 
     * @param amount
     */
    void MoveX(int amount)
    {
        this.x += amount;
    }

    /** 
     * @param amount
     */
    void MoveY(int amount)
    {
        this.y += amount;
    }

    /** 
     * @param x_amount
     * @param y_amount
     */
    void Move(int x_amount, int y_amount)
    {
        this.x += x_amount;
        this.y += y_amount;
    }

    /** 
     * @return int
     */
    int GiveX()
    {
        return this.x;
    }

    /** 
     * @return int
     */
    int GiveY()
    {
        return this.y;
    }

   /** 
    * @return ComponentType
    */
   @Override
    public ComponentType GetComponentType() {
        return ComponentType.SpriteComponent;
    }

}
