package org.flixel.system;

import org.flixel.FlxObject;
import org.flixel.FlxTilemap;
import org.flixel.event.IFlxTile;

public class FlxTile extends FlxObject
{
	/**
	 * This function is called whenever an object hits a tile of this type.
	 * This function should take the form <code>myFunction(Tile:FlxTile,Object:FlxObject):void</code>.
	 * Defaults to null, set through <code>FlxTilemap.setTileProperties()</code>.
	 */
	public IFlxTile callback;
	/**
	 * Each tile can store its own filter class for their callback functions.
	 * That is, the callback will only be triggered if an object with a class
	 * type matching the filter touched it.
	 * Defaults to null, set through <code>FlxTilemap.setTileProperties()</code>.
	 */
	public Class<? extends FlxObject> filter;
	/**
	 * A reference to the tilemap this tile object belongs to.
	 */
	public FlxTilemap tilemap;
	/**
	 * The index of this tile type in the core map data.
	 * For example, if your map only has 16 kinds of tiles in it,
	 * this number is usually between 0 and 15.
	 */
	public int index;
	/**
	 * The current map index of this tile object at this moment.
	 * You can think of tile objects as moving around the tilemap helping with collisions.
	 * This value is only reliable and useful if used from the callback function.
	 */
	public int mapIndex;
	
	/**
	 * Instantiate this new tile object.  This is usually called from <code>FlxTilemap.loadMap()</code>.
	 * 
	 * @param Tilemap			A reference to the tilemap object creating the tile.
	 * @param Index				The actual core map data index for this tile type.
	 * @param Width				The width of the tile.
	 * @param Height			The height of the tile.
	 * @param Visible			Whether the tile is visible or not.
	 * @param AllowCollisions	The collision flags for the object.  By default this value is ANY or NONE depending on the parameters sent to loadMap().
	 */
	public FlxTile(FlxTilemap Tilemap, int Index, int Width, int Height, boolean Visible, int AllowCollisions)
	{
		super(0, 0, Width, Height);
		immovable = true;
		moves = false;
		callback = null;
		
		tilemap = Tilemap;
		index = Index;
		visible = Visible;
		allowCollisions = AllowCollisions;
		
		mapIndex = 0;
	}
	
	@Override
	public void destroy()
	{		
		callback = null;
		tilemap = null;
		super.destroy();
	}
}
