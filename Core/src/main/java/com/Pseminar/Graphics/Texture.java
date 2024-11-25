package com.Pseminar.Graphics;

import org.lwjgl.opengl.GL46;

import com.Pseminar.Assets.Asset;
import com.Pseminar.TextureLoader.TextureLoader;

public class Texture extends Asset {
    /**
     * DIe Klasse Texture ist ein Asset, was man in einem Grafik-Rendering-Prozess verwendet (also z.B.Texturen werden in Sichtbare Bilder umgewandelt) 
     * enthält die Logik für den Umgang mit Texturdaten
    */

    // Nicht finaler code aber kein bock an der OPengllib weiter zu machen deswegene erstmal so 
    private int TextureId;

    public Texture(String path) {
        this.TextureId = TextureLoader.createTexture(path);
    }

    @Override
    public AssetType GetAssetType() {
        return AssetType.TEXTURE2D;
    }

    @Override
    public void OnDispose() {
        GL46.glDeleteTextures(this.TextureId);
    }

    public void Bind(int index) {
        GL46.glActiveTexture(GL46.GL_TEXTURE0 + index);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.TextureId);
    }

    public void Unbind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
    }

    @Override
    public String toString() {
        return new String("Texture with id: " + this.TextureId);
    }
}
