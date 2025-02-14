package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;

public class VektorComponent extends Component{
    
    private int x = 0;
    private int y = 0;

    void MoveX(int amount)
    {
        this.x += amount;
    }

    void MoveY(int amount)
    {
        this.y += amount;
    }

    void Move(int x_amount, int y_amount)
    {
        this.x += x_amount;
        this.y += y_amount;
    }

    int GiveX()
    {
        return this.x;
    }

    int GiveY()
    {
        return this.y;
    }

}
