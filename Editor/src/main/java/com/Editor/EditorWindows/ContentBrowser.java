package com.Editor.EditorWindows;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.Pseminar.Logger;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Asset.AssetType;
import com.Pseminar.Assets.Editor.EditorAssetManager;
import com.Pseminar.Assets.Editor.IntermidiateAssetData;

import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;

public class ContentBrowser  implements IEditorImGuiWindow{

    private Map<String, File[]> content = new HashMap<>();
    private String currentPath = Path.of(ProjectInfo.GetProjectInfo().GetAssetDir()).toString();
    private final String assetDir = Path.of(ProjectInfo.GetProjectInfo().GetAssetDir()).toString();

    private final int size = 64;

    public ContentBrowser() {
        Refresh();
    }

    @Override
    public void OnImgui() {
		ImGui.begin("Content Browser");
		
		if(ImGui.beginPopup("CBFileHandling")) {
			IntermidiateAssetData assetData = ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).GetAssetDataFromPath(currentPathToImport);

			if(assetData == null) {
				if(ImGui.selectable("Load File Into AssetManager")) {
					String adjustedPath = currentPathToImport.replace(ProjectInfo.GetProjectInfo().GetProjectPath(), "");
					((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).ImportAsset(adjustedPath);
				}
			} else {
				switch (assetData.GetAssetType()) {
					case SCENE:
						if(ImGui.selectable("Open Scene")) {

						}
						break;
					default:
						ImGui.text("Es gibt nix was du damit einfach im editor machen kannst");;
						break;
				}
			}
			ImGui.endPopup();
		}

        if (!currentPath.equals(assetDir)) {
			if (ImGui.button("Back")) {
				currentPath = new File(currentPath).getParent();
			}
		}

		int columCount = (int) (ImGui.getContentRegionAvailX() / (size + 16));
		ImGui.columns(columCount == 0 ? 1 : columCount, "##contentBrowser", false);

		if(content.get(currentPath) != null) {
			for (File file : content.get(currentPath)) {
				if (file.isDirectory()) {
					ImGui.pushID(file.getPath());
					if (ImGui.button("Folder", size, size)) {
						currentPath = file.getPath();
					}
					ImGui.popID();
					ImGui.text(file.getName());
				} else {
					HandelFile(file);
				}
				ImGui.nextColumn();
			}
		}
		
		ImGui.columns(1);

		ImGui.separator();

		if(ImGui.button("refresh")) {
			this.Refresh();
		}
		
		if (ImGui.isMouseClicked(ImGuiMouseButton.Right) && ImGui.isWindowHovered()) {
			ImGui.openPopup("CreateNewAsset");
		}

        ImGui.end();
		
    }

	private String currentPathToImport = "";

	private static AssetType GetAssetTypeFromFileName(String filePath) {
		String extension = "";

		int i = filePath.lastIndexOf('.');
		if (i > 0) {
			extension = filePath.substring(i+1);
		}	

		switch (extension) {
			case "png":
				return AssetType.TEXTURE2D;		
			case "scene":
				return AssetType.SCENE;
			case "sprite":
				return AssetType.SPRITE;
			default:
				return AssetType.NULL;
		}
	} 

	// Kein plan warum ich das so machen muss aber irgendwas damit das es push bla bla machen will 
	// aber zu faul jetzt die wiki zu lesen 
	private boolean tooglePopup = false;

    private void HandelFile(File file) {
		ImGui.pushID(file.getPath());

        if (ImGui.button(GetAssetTypeFromFileName(file.getPath()).name(), size, size)) {
			tooglePopup = true;
        }

		ImGui.popID();

		ImGui.text(file.getName());

		if(tooglePopup) {
            ImGui.openPopup("CBFileHandling");
			currentPathToImport = file.getPath();

			tooglePopup = false;
		}
	}

    private void Refresh() {
		AddToContent(Path.of(ProjectInfo.GetProjectInfo().GetAssetDir()).toString());
	}

	private void AddToContent(String path) {
		File folder = new File(path);

		content.put(new File(path).getPath(), folder.listFiles());

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				AddToContent(fileEntry.getPath());
			}
		}
	}
    
}
