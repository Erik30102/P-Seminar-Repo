package com.Pseminar.ECS;

public interface IEntityListener<T extends Component> {
	public void OnEntityAdded(Entity entity, T component);

	public void OnEntityRemoved(Entity entity, T component);
}