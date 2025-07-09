package com.Pseminar.Graphics;

public class Animation {
    private SpriteSheet spriteSheet;
    private int[] animationKeyFrames;
    private float frameDruation = 1;

    public Animation(SpriteSheet spriteSheet, int[] keyframes) {
        this.spriteSheet = spriteSheet;
        this.animationKeyFrames = keyframes;
    }

    public void SetFrameDruation(float duraiton) {
        this.frameDruation = duraiton;
    }

    public float GetFrameDuration() {
        return this.frameDruation;
    }

    public Sprite GetSpriteAtKeyfarme(int num) {
        return this.spriteSheet.getSprite(animationKeyFrames[num % animationKeyFrames.length]);
    }
}
