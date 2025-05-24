package com.Editor.EditorWindows;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.Asset.AssetType;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.EditorAssetManager;

import imgui.ImGui;

public class ContentBrowser  implements IEditorImGuiWindow{

    private Map<String, File[]> content = new HashMap<>();
    private String currentPath = ProjectInfo.GetProjectInfo().GetAssetDir();
    private final String assetDir = ProjectInfo.GetProjectInfo().GetAssetDir();

    private final int size = 64;

    public ContentBrowser() {
        Refresh();
    }

    @Override
    public void OnImgui() {
		ImGui.begin("Content Browser");
		
        if (!currentPath.equals(assetDir)) {
			if (ImGui.button("Back")) {
				currentPath = new File(currentPath).getParent();
			}
		}

		int columCount = (int) (ImGui.getContentRegionAvailX() / (size + 16));
		ImGui.columns(columCount == 0 ? 1 : columCount, "##contentBrowser", false);

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
		ImGui.columns(1);

        ImGui.end();
    }

    private void HandelFile(File file) {
		ImGui.pushID(file.getPath());

        if (ImGui.button("NF", size, size)) {
            
        }
		ImGui.popID();

		ImGui.text(file.getName());
	}

    private void Refresh() {
		AddToContent(ProjectInfo.GetProjectInfo().GetAssetDir());
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
