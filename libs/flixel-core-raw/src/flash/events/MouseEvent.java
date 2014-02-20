package flash.events;

/**
 * A copy of Flash's <code>MouseEvent</code>.
 * 
 * @author Ka Wing Chin
 */
public class MouseEvent extends Event
{
	/**
	 * Defines the value of the type property of a mouseUp event object.
	 */
	static public final String MOUSE_UP = "mouseUp";
	/**
	 * Defines the value of the type property of a mouseDown event object.
	 */
	static public final String MOUSE_DOWN = "mouseDown";
	/**
	 * Defines the value of the type property of a mouseWheel event object.
	 */
	static public final String MOUSE_WHEEL = "mouseWheel";
	
	/**
	 * The horizontal coordinate at which the event occurred in global <code>Stage</code> coordinates.
	 */
	public int stageX;
	/**
	 * The vertical coordinate at which the event occurred in global <code>Stage</code> coordinates.
	 */
	public int stageY;
	/**
	 * Indicates how many lines should be scrolled for each unit the user rotates the mouse wheel.
	 */
	public int delta;
	
	/**
	 * Creates an <code>Event</code> object that contains information about mouse events.
	 * 
	 * @param	type				The type of event.
	 * @param	bubbles			Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>true</code>.
	 * @param	cancelable		Determines whether the <code>Event</code> object can be cancelled. The default values is <code>false</code>.
	 * @param	localX			The horizontal coordinate at which the event occurred relative to the containing sprite.
	 * @param	localY			The vertical coordinate at which the event occurred relative to the containing sprite.
	 * @param	delta				Indicates how many lines should be scrolled for each unit the user rotates the mouse wheel. A positive delta value indicates an upward scroll; a negative value indicates a downward scroll.
	 */
	public MouseEvent(String type, boolean bubbles, boolean cancelable, int localX, int localY, int delta)
	{
		super(type, bubbles, cancelable);
		this.stageX = localX;
		this.stageY = localY;
		this.delta = delta;
	}
	
	/**
	 * Creates an <code>Event</code> object that contains information about mouse events.
	 * 
	 * @param	type				The type of event.
	 * @param	bubbles			Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>true</code>.
	 * @param	cancelable		Determines whether the <code>Event</code> object can be cancelled. The default values is <code>false</code>.
	 * @param	localX			The horizontal coordinate at which the event occurred relative to the containing sprite.
	 * @param	localY			The vertical coordinate at which the event occurred relative to the containing sprite.
	 */
	public MouseEvent(String type, boolean bubbles, boolean cancelable, int localX, int localY)
	{
		this(type, bubbles, cancelable, localX, localY, 0);
	}
	
	/**
	 * Creates an <code>Event</code> object that contains information about mouse events.
	 * 
	 * @param	type				The type of event.
	 * @param	bubbles			Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>true</code>.
	 * @param	cancelable		Determines whether the <code>Event</code> object can be cancelled. The default values is <code>false</code>.
	 * @param	localX			The horizontal coordinate at which the event occurred relative to the containing sprite.
	 */
	public MouseEvent(String type, boolean bubbles, boolean cancelable, int localX)
	{
		this(type, bubbles, cancelable, localX, 0, 0);
	}
	
	/**
	 * Creates an <code>Event</code> object that contains information about mouse events.
	 * 
	 * @param	type				The type of event.
	 * @param	bubbles			Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>true</code>.
	 * @param	cancelable		Determines whether the <code>Event</code> object can be cancelled. The default values is <code>false</code>.
	 */
	public MouseEvent(String type, boolean bubbles, boolean cancelable)
	{
		this(type, bubbles, cancelable, 0, 0, 0);
	}
	
	/**
	 * Creates an <code>Event</code> object that contains information about mouse events.
	 * 
	 * @param	type				The type of event.
	 * @param	bubbles			Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>true</code>.
	 */
	public MouseEvent(String type, boolean bubbles)
	{
		this(type, bubbles, false, 0, 0, 0);
	}
	
	/**
	 * Creates an <code>Event</code> object that contains information about mouse events.
	 * 
	 * @param	type				The type of event.
	 * @param	bubbles			Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>true</code>.
	 */
	public MouseEvent(String type)
	{
		this(type, true, false, 0, 0, 0);
	}
}
