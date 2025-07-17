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

    /** 
     * @param duraiton
     */
    public void SetFrameDruation(float duraiton) {
        this.frameDruation = duraiton;
    }

    /** 
     * @return float
     */
    public float GetFrameDuration() {
        return this.frameDruation;
    }

    /** 
     * @return int[]
     */
    public int[] GetAnimationKeyFrames(){
        return this.animationKeyFrames;
    }

    /** 
     * @return SpriteSheet
     */
    public SpriteSheet GetSpritesheet() {
        return this.spriteSheet;
    }

    /** 
     * @param num
     * @return Sprite
     */
    public Sprite GetSpriteAtKeyfarme(int num) {
        return this.spriteSheet.getSprite(animationKeyFrames[num % animationKeyFrames.length]);
    }

    /** 
     * @return AssetType
     */
    @Override
    public AssetType GetAssetType() {
        return AssetType.ANIMATION;
    }

    @Override
    public void OnDispose() {

    }
}
