package com.backsun.lod.objects;

import java.awt.Color;

import com.backsun.lod.enums.ColorDirection;

import net.minecraft.util.math.ChunkPos;

/**
 * This object contains position
 * and color data for an LOD object.
 * 
 * @author James Seibel
 * @version 05-29-2021
 */
public class LodChunk
{
	/** how many different pieces of data are in one line */
	private static final int DATA_DELIMITER_COUNT = 22;

	/** This is what separates each piece of data in the toData method */
	public static final char DATA_DELIMITER = ',';
	
	/** Width of a Minecraft Chunk */
	public static final int WIDTH = 16;
	
	private static final Color INVISIBLE = new Color(0,0,0,0);
	
	
	
	/** The x coordinate of the chunk. */
	public int x = 0;
	/** The z coordinate of the chunk. */
	public int z = 0;
	
	/*
	 * The reason we are only using 1 height and depth point
	 * instead of multiple is because:
	 * 1. more points would drastically increase the amount of
	 * 		memory and disk space needed
	 * 2. more height/depth points require more color points,
	 * 		which can get out of hand quickly
	 * 3. with the increased disk space reading/writing would
	 * 		take far too long
	 * 4. the increased resolution is generally not worth it,
	 * 		4 LODs per chunk is the limit of diminishing returns.
	 * 		And some of that could potentially be faked through 
	 * 		smart LodTemplates
	 */
	private short height;
	private short depth;
	
	/** The average color for the 6 cardinal directions */
	public Color colors[];
	
	/** If true then this LodChunk contains no data */
	private boolean empty = false;
	
	
	/**
	 * Create an empty invisible LodChunk at (0,0)
	 */
	public LodChunk()
	{
		empty = true;
		
		x = 0;
		z = 0;
		
		height = 0;
		depth = 0;
		colors = new Color[ColorDirection.values().length];
		
		// by default have the colors invisible
		for(ColorDirection dir : ColorDirection.values())
			colors[dir.value] = INVISIBLE;
	}
	
	
	/**
	 * Creates an LodChunk from the string
	 * generated by the toData method.
	 * 
	 * @throws IllegalArgumentException if the data isn't valid to create a LodChunk
	 * @throws NumberFormatException if any piece of data can't be converted at any point
	 */
	public LodChunk(String data) throws IllegalArgumentException, NumberFormatException
	{
		/*
		 * data format:
		 * x, z, height, depth, rgb color data
		 * 
		 * example:
		 * 5,8, 4, 0, 255,255,255, 255,255,255, 255,255,255, 255,255,255, 255,255,255, 255,255,255,
		 */
		
		// make sure there are the correct number of entries
		// in the data string (28)
		int count = 0;
		
		for(int i = 0; i < data.length(); i++)
			if(data.charAt(i) == DATA_DELIMITER)
				count++;
		
		if(count != DATA_DELIMITER_COUNT)
			throw new IllegalArgumentException("LodChunk constructor givin an invalid string. The data given had " + count + " delimiters when it should have had " + DATA_DELIMITER_COUNT + ".");
		
		
		
		// index we will use when going through the String
		int index = 0;
		int lastIndex = 0;
		
		
		
		// x and z position
		index = data.indexOf(DATA_DELIMITER, 0);
		x = Integer.parseInt(data.substring(0,index));
		
		lastIndex = index;
		index = data.indexOf(DATA_DELIMITER, lastIndex+1);
		z = Integer.parseInt(data.substring(lastIndex+1,index));
		
		// height
		lastIndex = index;
		index = data.indexOf(DATA_DELIMITER, lastIndex+1);
		height = Short.parseShort(data.substring(lastIndex+1,index));
		
		// depth
		lastIndex = index;
		index = data.indexOf(DATA_DELIMITER, lastIndex+1);
		depth = Short.parseShort(data.substring(lastIndex+1,index));
		
		
		
		// color
		colors = new Color[6];
		for(ColorDirection dir : ColorDirection.values())
		{
			int red = 0;
			int green = 0;
			int blue = 0;
			
			// get r,g,b
			for(int i = 0; i < 3; i++)
			{
				lastIndex = index;
				index = data.indexOf(DATA_DELIMITER, lastIndex + 1);
				
				String raw = "";
				switch(i)
				{
				case 0:
					raw = data.substring(lastIndex+1,index);
					red = Short.parseShort(raw);
					break;
				case 1:
					raw = data.substring(lastIndex+1,index);
					green = Short.parseShort(raw);
					break;
				case 2:
					raw = data.substring(lastIndex+1,index);
					blue = Short.parseShort(raw);
					break;
				}
			}
			
			colors[dir.value] = new Color(red, green, blue);
		}
		
		
		// after going through all this
		// is this LOD empty?
		empty = determineIfEmtpy();
	}
	
	/**
	 * Create a LodChunk from the given values.
	 */
	public LodChunk(ChunkPos pos, short newHeight, short newDepth, Color[] newColors)
	{
		x = pos.x;
		z = pos.z;
		
		height = newHeight;
		depth = newDepth;
		colors = newColors;
		
		empty = determineIfEmtpy();
	}
	
	
	
	
	
	
	//================//
	// misc functions //
	//================//
	
	/**
	 * Returns true if this LodChunk is an emptyPlaceholder
	 */
	public boolean isPlaceholder()
	{
		return empty;
	}
	
	public boolean isLodEmpty()
	{
		return empty;
	}
	
	/**
	 * Returns true if this LOD is either invisible
	 * from every direction or doesn't have a valid height.
	 */
	private boolean determineIfEmtpy()
	{
		if(height != -1)
			// we don't check the depth since the
			// height should always be greater than or equal
			// to the depth
			return false;
		
		for(ColorDirection dir : ColorDirection.values())
			if(!colors[dir.value].equals(INVISIBLE))
				// at least one direction has a non-invisible color
				return false;
		
		return true;
	}
	
	
	
	
	//========//
	// output //
	//========//
	
	/** Returns the color for the given direction */
	public Color getColor(ColorDirection dir)
	{
		return colors[dir.value];
	}
	
	public short getHeight()
	{
		return height;
	}
	
	public short getDepth()
	{
		return depth;
	}
	
	
	
	
	
	/**
	 * Outputs all data in csv format
	 * with the given delimiter.
	 * <br>
	 * Exports data in the form:
	 * <br>
	 * x, z, height, depth, rgb color data
	 * 
	 * <br>
	 * example output:
	 * <br>
	 * 5,8, 4, 0, 255,255,255, 255,255,255, 255,255,255, 255,255,255, 255,255,255, 255,255,255,
	 */
	public String toData()
	{
		String s = "";
		
		s += Integer.toString(x) + DATA_DELIMITER +  Integer.toString(z) + DATA_DELIMITER;
		
		s += Short.toString(height) + DATA_DELIMITER;
		
		s += Short.toString(depth) + DATA_DELIMITER;
		
		for(int i = 0; i < colors.length; i++)
		{
			s += Integer.toString(colors[i].getRed()) + DATA_DELIMITER + Integer.toString(colors[i].getGreen()) + DATA_DELIMITER + Integer.toString(colors[i].getBlue()) + DATA_DELIMITER;
		}
		
		return s;
	}
	
	
	@Override
	public String toString()
	{
		String s = "";
		
		s += "x: " + x + " z: " + z + "\t";
		
		s += "(" + colors[ColorDirection.TOP.value].getRed() + ", " + colors[ColorDirection.TOP.value].getGreen() + ", " + colors[ColorDirection.TOP.value].getBlue() + ")";
		
		return s;
	}
}
