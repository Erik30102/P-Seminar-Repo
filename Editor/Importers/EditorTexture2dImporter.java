package com.Pseminar.Assets.Editor.Importers;

import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.IEditorAssetImporter;
import com.Pseminar.Assets.Editor.IntermidiateAssetData;
import com.Pseminar.Graphics.Texture;

public class EditorTexture2dImporter implements IEditorAssetImporter {

    @Override
    public Asset LoadAsset(IntermidiateAssetData assetMetaData) {
       Texture texture = new Texture(ProjectInfo.GetProjectInfo().GetProjectPath() + assetMetaData.GetPath());

       return texture;
    } 
}
