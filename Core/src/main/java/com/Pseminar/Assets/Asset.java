package com.Pseminar.Assets;

public abstract class Asset {
    private transient int numberOfHandlers = 0;
    private transient int id;

    public enum AssetType {
        TEXTURE2D,
    }

    /**
     * wird vom asset manager ausfeführt um dem asset die richtige id zuzuweisen
     * 
     * @param id
     */
    public final void SetId(int id) {
        this.id = id;
    }

    public int GetAssetId() {
        return this.id;
    }

    public abstract AssetType GetAssetType();
    public abstract void OnDispose();

    /**
     * wenn ein asset benötigt wird diese methopde ausfürhen sonst kann es einfach entladen werden
     */
    public void Handle() {
        numberOfHandlers++;
    }

    /**
     * wenn ein component ein asset nicht mehr braucht die methode ausführen damit es wieder entladen wird
     */
    public void RemoveHandle() {
        numberOfHandlers--;

        if(numberOfHandlers <= 0) {
            OnDispose();
            ProjectInfo.GetProjectInfo().GetAssetManager().DisposeAsset(this.id);
        }
    }

    /**
     * gibt den asset type basirend auf dem file path an also wenn die extension .png ist wirds ne texture sein
     * 
     * @param filePath der path zu dem file
     * @return der asset type
     */
    public static AssetType GetAssetTypeFromFilePath(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf(".")+1);
        
        switch (extension) {
            case "png":
            case "jpg":
            case "bmp":
                return AssetType.TEXTURE2D;
        
            default:
                return null;
        }
    }

}
