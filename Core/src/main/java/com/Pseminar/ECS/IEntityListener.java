package com.Pseminar.ECS;

/**
 * Inteface für listnere von Enties muss erst zu jeder scene hinzugefügt werden das es funktiniert
 */
public interface IEntityListener<T extends Component> {
	/**
	 * wird gecalled wenn ein component einen entity geadded wird für den type von compoennt das ding der listener ist
	 * 
	 * @param entity das entity
	 * @param component das component
	 */
	public void OnEntityAdded(Entity entity, T component);

	/**
	 * wird gecalled wenn ein component einen entity entfernt wird für den type von compoennt das ding der listener ist
	 * 
	 * @param entity das entity 
	 * @param component das component
	 */
	public void OnEntityRemoved(Entity entity, T component);
}