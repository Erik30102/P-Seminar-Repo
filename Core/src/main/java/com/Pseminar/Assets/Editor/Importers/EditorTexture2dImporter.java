package com.Pseminar.Assets.Editor.Importers;

import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.IEditorAssetImporter;
import com.Pseminar.Assets.Editor.IntermidiateAssetData;
import com.Pseminar.Graphics.Texture;

public class EditorTexture2dImporter implements IEditorAssetImporter {

    /** 
     * @param assetMetaData
     * @return Asset
     */
    @Override
    public Asset LoadAsset(IntermidiateAssetData assetMetaData) {
       Texture texture = new Texture(ProjectInfo.GetProjectInfo().GetProjectPath() + assetMetaData.GetPath());

       return texture;
    }

    /** 
     * @param path
     * @param asset
     */
    @Override
    public void SerializeAsset(String path, Asset asset) {
        throw new UnsupportedOperationException("sollte eigentlich ned getriggert werden auser man will texturen erstellen und saven per programm dann muss das wer implementiren");
    } 
}
