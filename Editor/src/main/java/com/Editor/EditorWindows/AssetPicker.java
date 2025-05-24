package com.Editor.EditorWindows;

import java.util.ArrayList;
import java.util.List;

import com.Pseminar.Logger;
import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Asset.AssetType;
import com.Pseminar.Assets.Editor.EditorAssetManager;
import com.Pseminar.Assets.Editor.IntermidiateAssetData;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;

public class AssetPicker {
    private static AssetPicker INSTANCE;

	private String key = "";
	private boolean showDialog = false;
	private AssetType type;

	private Asset selected;

	private List<IntermidiateAssetData> ChashedAssets = new ArrayList<>();
    private static ImString search = new ImString();

	public AssetPicker() {
		INSTANCE = this;
	}

	public static void Init() {
		new AssetPicker();
	}

	public static boolean Display(String key) {
		if (INSTANCE.key.equals(key) && INSTANCE.showDialog) {
			ImGui.openPopup(key);

			if (ImGui.beginPopupModal(key, new ImBoolean(true), ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoTitleBar)) {

                ImGui.inputText("Search", search);

				int columCount = (int) (ImGui.getContentRegionAvailX() / (80));
				ImGui.columns(columCount == 0 ? 1 : columCount, "##assetPicker", false);

				for (IntermidiateAssetData metaData : INSTANCE.ChashedAssets) {
                    if(metaData.GetPath().contains(search.get())) {
                        if (ImGui.button(metaData.GetPath(), 64, 64)) {
                            INSTANCE.selected = ProjectInfo.GetProjectInfo().GetAssetManager()
                                    .GetAsset(((EditorAssetManager) ProjectInfo.GetProjectInfo().GetAssetManager()).GetIdFromMetadata(metaData));

                            INSTANCE.showDialog = false;
                            ImGui.endPopup();

                            return true;
                        }
                    }
				}

				ImGui.endPopup();
			}

		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Asset> T GetSelected(Class<T> clazz) {
		if (INSTANCE.selected.getClass() != clazz) {
			Logger.warn("Selected asset is not of type " + clazz.getSimpleName() + " Edior bug please report!");
			return null;
		}

		return (T) INSTANCE.selected;
	}

	public static void Open(String key, AssetType type) {
		INSTANCE.key = key;
		INSTANCE.showDialog = true;
		INSTANCE.type = type;
		INSTANCE.selected = null;

        search.clear();

		INSTANCE.ChashedAssets = ((EditorAssetManager) ProjectInfo.GetProjectInfo().GetAssetManager())
				.GetAssetsOfType(INSTANCE.type);
	}
}
