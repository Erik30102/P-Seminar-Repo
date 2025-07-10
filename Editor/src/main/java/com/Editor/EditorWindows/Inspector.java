package com.Editor.EditorWindows;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import javax.script.ScriptEngine;

import org.joml.Vector3f;

import com.Pseminar.Assets.ScriptingEngine;
import com.Pseminar.Assets.Asset.AssetType;
import com.Pseminar.ECS.Component;
import com.Pseminar.ECS.Entity;
import com.Pseminar.ECS.BuiltIn.AnimationSpriteComponent;
import com.Pseminar.ECS.BuiltIn.BaseComponent;
import com.Pseminar.ECS.BuiltIn.CameraComponent;
import com.Pseminar.ECS.BuiltIn.RidgedBodyComponent;
import com.Pseminar.ECS.BuiltIn.SpriteComponent;
import com.Pseminar.ECS.Component.ComponentType;
import com.Pseminar.Graphics.Animation;
import com.Pseminar.Graphics.Sprite;

import imgui.ImGui;
import imgui.type.ImString;

public class Inspector implements IEditorImGuiWindow {

    private Entity entity;

    public void SetEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void OnImgui() {
        ImGui.begin("Inspector");
        if(entity != null) {

			if (ImGui.collapsingHeader("Entity info")) {
				ImGui.columns(2);

				ImGui.text("Entity name:");
				ImGui.nextColumn();
				ImString EntityName = new ImString();
				EntityName.set(entity.GetName());
				if (ImGui.inputText("##T", EntityName)) {
					entity.SetName(EntityName.get());
				}

				ImGui.nextColumn();

				ImGui.text("Position: ");
				ImGui.nextColumn();
				float[] f = { entity.transform.GetPosition().x(),
						entity.transform.GetPosition().y() };
				if (ImGui.dragFloat2("##P", f, 0.1f)) {
					entity.transform.setPosition(f[0], f[1]);
				}
				ImGui.nextColumn();
				ImGui.text("Rotation: ");
				ImGui.nextColumn();
				float[] r = { entity.transform.GetRotation() };
				if (ImGui.dragFloat("##R", r, 0.1f)) {
					entity.transform.setRotation(r[0]);
				}
				ImGui.nextColumn();
				ImGui.text("Scale: ");
				ImGui.nextColumn();
				float[] s = { entity.transform.GetScale().x(),
						entity.transform.GetScale().y() };
				if (ImGui.dragFloat2("##S", s, 0.1f)) {
					entity.transform.setScale(s[0], s[1]);
				}
				ImGui.nextColumn();

			}
			ImGui.columns(1);

			for (Component component : entity.getComponents()) {
				if (component.GetComponentType() == ComponentType.BaseComponent) {
					if (ImGui.collapsingHeader(component.getClass().getSimpleName())) {
						HandleCustomComponentSlider(component);
					}

					continue;
				}

				if (ImGui.collapsingHeader(component.getClass().getSimpleName())) {
					if (component.getClass() == SpriteComponent.class) {
						ImGui.columns(2);

						ImGui.text("Texture: ");
						ImGui.nextColumn();
						if (ImGui.button("Select Texture")) {
							AssetPicker.Open("spr", AssetType.SPRITE);
						}

						if (AssetPicker.Display("spr")) {
							((SpriteComponent) component).SetSprite(AssetPicker.GetSelected(Sprite.class));
						}

						ImGui.columns(1);
					} else if(component.getClass() == AnimationSpriteComponent.class) {
						ImGui.columns(2);

						ImGui.text("Aniamtion: ");
						ImGui.nextColumn();
						if (ImGui.button("Select Aniamtion")) {
							AssetPicker.Open("ani", AssetType.ANIMATION);
						}

						if (AssetPicker.Display("ani")) {
							((AnimationSpriteComponent) component).SetAnimation(AssetPicker.GetSelected(Animation.class));
						}

						ImGui.columns(1);

					} else if(component.getClass() == CameraComponent.class) {
						ImGui.columns(2);

						CameraComponent c = ((CameraComponent)component);
						
						ImGui.text("Active");
						ImGui.nextColumn();

						boolean val = (boolean) c.GetActive();
						if (ImGui.checkbox("##cameraAcitve", val)) {
							c.SetActive(!c.GetActive());
						}
						ImGui.nextColumn();

						ImGui.columns(1);
					}
				}
			}

			if (ImGui.button("add Component"))
				ImGui.openPopup("Add Component");

			if (ImGui.beginPopup("Add Component")) {
                for (String component : ScriptingEngine.GetInstance().GetClasses()) {
                    if(ImGui.selectable(component))
                    {
                        this.entity.AddComponent(ScriptingEngine.GetInstance().GetNewComponent(component));
                    }
                }

                for(Component c : c) {
                    if(ImGui.selectable(c.getClass().getCanonicalName())) {
                        try {
                            this.entity.AddComponent((Component)(c.getClass().getConstructors()[0].newInstance()));
                        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                                | InvocationTargetException | SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                }
				ImGui.endPopup();
			}

        }
        ImGui.end();
    }

    private static Component[] c = new Component[] { new SpriteComponent(), new CameraComponent(), new RidgedBodyComponent(), new AnimationSpriteComponent() };
    
    private void HandleCustomComponentSlider(Component component) {
		ImGui.columns(2);

		try {
			Field[] fields = component.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (!Modifier.isPrivate(field.getModifiers())) {
					@SuppressWarnings("rawtypes")
					Class type = field.getType();
					Object value = field.get(component);
					String name = field.getName();

					if (type == int.class) {
						ImGui.text(name);
						ImGui.nextColumn();

						int val = (int) value;
						int[] imInt = { val };

						if (ImGui.dragInt("##" + name, imInt)) {
							field.set(component, imInt[0]);
						}

						ImGui.nextColumn();
					} else if (type == float.class) {
						ImGui.text(name);
						ImGui.nextColumn();

						float val = (float) value;
						float[] imfloat = { val };

						if (ImGui.dragFloat("##" + name, imfloat)) {
							field.set(component, imfloat[0]);
						}

						ImGui.nextColumn();
					} else if (type == Vector3f.class) {
						ImGui.text(name);
						ImGui.nextColumn();

						Vector3f val = (Vector3f) value;
						float[] f = { val.x, val.y, val.z };
						if (ImGui.dragFloat3("##v" + name, f)) {
							val.x = f[0];
							val.y = f[1];
							val.z = f[2];
							field.set(component, val);
						}
						ImGui.nextColumn();
					} else if (type == boolean.class) {
						ImGui.text(name);
						ImGui.nextColumn();

						boolean val = (boolean) value;
						if (ImGui.checkbox("##" + name, val)) {
							field.set(component, !val);
						}
						ImGui.nextColumn();
					} else if (type == Sprite.class) {
						ImGui.text(name);
						ImGui.nextColumn();
						ImGui.pushID(field.hashCode());
						if (ImGui.button("Select Texture")) {
							AssetPicker.Open(field.hashCode() + "TEX", AssetType.SPRITE);
						}
						ImGui.popID();

						if (AssetPicker.Display(field.hashCode() + "TEX")) {
							field.set(component, AssetPicker.GetSelected(Sprite.class));
						}

						ImGui.nextColumn();
					}else if (type == Animation.class) {
						ImGui.text(name);
						ImGui.nextColumn();
						ImGui.pushID(field.hashCode());
						if (ImGui.button("Select Animation")) {
							AssetPicker.Open(field.hashCode() + "ANIM", AssetType.ANIMATION);
						}
						ImGui.popID();

						if (AssetPicker.Display(field.hashCode() + "ANIM")) {
							field.set(component, AssetPicker.GetSelected(Animation.class));
						}

						ImGui.nextColumn();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ImGui.columns(1);

		ImGui.columns(1);
	}

    
}
