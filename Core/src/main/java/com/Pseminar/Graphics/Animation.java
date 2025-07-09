package com.Pseminar.Graphics;

import com.Pseminar.Assets.Asset;

public class Animation extends Asset {
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

    public int[] GetAnimationKeyFrames(){
        return this.animationKeyFrames;
    }

    public SpriteSheet GetSpritesheet() {
        return this.spriteSheet;
    }

    public Sprite GetSpriteAtKeyfarme(int num) {
        return this.spriteSheet.getSprite(animationKeyFrames[num % animationKeyFrames.length]);
    }

    @Override
    public AssetType GetAssetType() {
        return AssetType.ANIMATION;
    }

    @Override
    public void OnDispose() {

    }
}
