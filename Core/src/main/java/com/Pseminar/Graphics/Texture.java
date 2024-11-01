package com.Pseminar.Graphics;

import com.Pseminar.Assets.Asset;

public class Texture extends Asset {

    // Nicht finaler code aber kein bock an der OPengllib weiter zu machen deswegene erstmal so 
    private byte[] data;

    public Texture(byte[] data) {
        this.data = data;
    }

    @Override
    public AssetType GetAssetType() {
        return AssetType.TEXTURE2D;
    }

    @Override
    public void OnDispose() {
        // TODO: implment when OpenglLib is added
    }

    public byte[] GetData() {
        return this.data;
    }

    @Override
    public String toString() {
        return new String(GetData());
    }
}
