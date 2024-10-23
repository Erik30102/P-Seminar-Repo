package com.Pseminar.Graphics;

import com.Pseminar.Assets.Asset;

public class Texture2D extends Asset {

    @Override
    public AssetType GetAssetType() {
        return AssetType.TEXTURE2D;
    }

    @Override
    public void OnDispose() {
        // TODO: implment when OpenglLib is added
    }
}
