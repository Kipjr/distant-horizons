/*
 *    This file is part of the Distant Horizon mod (formerly the LOD Mod),
 *    licensed under the GNU GPL v3 License.
 *
 *    Copyright (C) 2020  James Seibel
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, version 3.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.seibel.lod.common;

import com.seibel.lod.common.wrappers.config.ConfigGui;
import com.seibel.lod.common.wrappers.world.DimensionTypeWrapper;
import com.seibel.lod.core.config.*;
import com.seibel.lod.core.enums.config.*;
import com.seibel.lod.core.enums.rendering.*;
import com.seibel.lod.core.wrapperInterfaces.config.ILodConfigWrapperSingleton.IClient.IAdvanced.*;
import com.seibel.lod.core.wrapperInterfaces.config.ILodConfigWrapperSingleton.IClient.IGraphics.*;
import com.seibel.lod.core.wrapperInterfaces.config.ILodConfigWrapperSingleton.IClient.IWorldGenerator;
import net.minecraft.client.renderer.DimensionSpecialEffects;

/**
 * This handles any configuration the user has access to.
 * @author coolGi2007
 * @version 12-12-2021
 */
public class Config extends ConfigGui
//public class Config extends TinyConfig
{
	// CONFIG STRUCTURE
	// 	-> Client
	//		|
	//		|-> Graphics
	//		|		|-> Quality
	//		|		|-> FogQuality
	//		|		|-> AdvancedGraphics
	//		|
	//		|-> World Generation
	//		|
	//		|-> Advanced
	//				|-> Threads
	//				|-> Buffers
	//				|-> Debugging

	// Since the original config system uses forge stuff, that means we have to rewrite the whole config system

	@ConfigAnnotations.ScreenEntry
	public static Client client;

	@ConfigAnnotations.Entry
	public static boolean ShowButton = true;

	public static class Client
	{
		@ConfigAnnotations.ScreenEntry
		public static Graphics graphics;

		@ConfigAnnotations.ScreenEntry
		public static WorldGenerator worldGenerator;

		@ConfigAnnotations.ScreenEntry
		public static Advanced advanced;


		public static class Graphics
		{
			@ConfigAnnotations.ScreenEntry
			public static Quality quality;

			@ConfigAnnotations.ScreenEntry
			public static FogQuality fogQuality;

			@ConfigAnnotations.ScreenEntry
			public static CloudQuality cloudQuality;

			@ConfigAnnotations.ScreenEntry
			public static AdvancedGraphics advancedGraphics;


			public static class Quality
			{
				@ConfigAnnotations.Entry
				public static HorizontalResolution drawResolution = IQuality.DRAW_RESOLUTION_DEFAULT;

				@ConfigAnnotations.Entry(minValue = 16, maxValue = 1024)
				public static int lodChunkRenderDistance = IQuality.LOD_CHUNK_RENDER_DISTANCE_MIN_DEFAULT_MAX.defaultValue;

				@ConfigAnnotations.Entry
				public static VerticalQuality verticalQuality = IQuality.VERTICAL_QUALITY_DEFAULT;

				@ConfigAnnotations.Entry(minValue = 2, maxValue = 32)
				public static int horizontalScale = IQuality.HORIZONTAL_SCALE_MIN_DEFAULT_MAX.defaultValue;

				@ConfigAnnotations.Category("client.graphics.quality")
				@ConfigAnnotations.Entry
				public static HorizontalQuality horizontalQuality = IQuality.HORIZONTAL_QUALITY_DEFAULT;
			}


			public static class FogQuality
			{
				@ConfigAnnotations.Entry
				public static FogDistance fogDistance = IFogQuality.FOG_DISTANCE_DEFAULT;

				@ConfigAnnotations.Entry
				public static FogDrawMode fogDrawMode = IFogQuality.FOG_DRAW_MODE_DEFAULT;

				@ConfigAnnotations.Entry
				public static FogColorMode fogColorMode = IFogQuality.FOG_COLOR_MODE_DEFAULT;

				@ConfigAnnotations.Entry
				public static boolean disableVanillaFog = IFogQuality.DISABLE_VANILLA_FOG_DEFAULT;
			}


			public static class CloudQuality
			{
				@ConfigAnnotations.Entry
				public static boolean customClouds = ICloudQuality.CUSTOM_CLOUDS_DEFAULT;

				@ConfigAnnotations.Entry
				public static boolean fabulousClouds = ICloudQuality.FABULOUS_CLOUDS_DEFAULT;

				@ConfigAnnotations.Entry
				public static boolean extendClouds = ICloudQuality.EXTEND_CLOUDS_DEFAULT;

				@ConfigAnnotations.Entry
				public static double cloudHeight = DimensionSpecialEffects.OverworldEffects.CLOUD_LEVEL;
			}


			public static class AdvancedGraphics
			{
				@ConfigAnnotations.Entry
				public static boolean disableDirectionalCulling = IAdvancedGraphics.DISABLE_DIRECTIONAL_CULLING_DEFAULT;

				@ConfigAnnotations.Entry
				public static boolean alwaysDrawAtMaxQuality = IAdvancedGraphics.ALWAYS_DRAW_AT_MAD_QUALITY_DEFAULT;

				@ConfigAnnotations.Entry
				public static VanillaOverdraw vanillaOverdraw = IAdvancedGraphics.VANILLA_OVERDRAW_DEFAULT;

				@ConfigAnnotations.Entry
				public static boolean useExtendedNearClipPlane = IAdvancedGraphics.USE_EXTENDED_NEAR_CLIP_PLANE_DEFAULT;

				@ConfigAnnotations.Entry(minValue = 0, maxValue = 512)
				public static int backsideCullingRange = IAdvancedGraphics.VANILLA_CULLING_RANGE_MIN_DEFAULT_MAX.defaultValue;
			}
		}


		public static class WorldGenerator
		{
			@ConfigAnnotations.Entry
			public static GenerationPriority generationPriority = IWorldGenerator.GENERATION_PRIORITY_DEFAULT;

			@ConfigAnnotations.Entry
			public static DistanceGenerationMode distanceGenerationMode = IWorldGenerator.DISTANCE_GENERATION_MODE_DEFAULT;

			// FIXME: Temperary override. In 1.18, the newer Unstable gnerator is more usable
			@ConfigAnnotations.Entry
			public static boolean allowUnstableFeatureGeneration = true;//IWorldGenerator.ALLOW_UNSTABLE_FEATURE_GENERATION_DEFAULT;

			@ConfigAnnotations.Entry
			public static BlocksToAvoid blocksToAvoid = IWorldGenerator.BLOCKS_TO_AVOID_DEFAULT;
		}

		public static class Advanced
		{
			@ConfigAnnotations.ScreenEntry
			public static Threading threading;

			@ConfigAnnotations.ScreenEntry
			public static Debugging debugging;

			@ConfigAnnotations.ScreenEntry
			public static Buffers buffers;


			public static class Threading
			{
				@ConfigAnnotations.Entry(minValue = 1, maxValue = 50)
				public static int numberOfWorldGenerationThreads = IThreading.NUMBER_OF_WORLD_GENERATION_THREADS_DEFAULT.defaultValue;

				@ConfigAnnotations.Entry(minValue = 1, maxValue = 50)
				public static int numberOfBufferBuilderThreads = IThreading.NUMBER_OF_BUFFER_BUILDER_THREADS_MIN_DEFAULT_MAX.defaultValue;
			}


			public static class Debugging
			{
				@ConfigAnnotations.Entry
				public static boolean drawLods = IDebugging.DRAW_LODS_DEFAULT;

				@ConfigAnnotations.Entry
				public static DebugMode debugMode = IDebugging.DEBUG_MODE_DEFAULT;

				@ConfigAnnotations.Entry
				public static boolean enableDebugKeybindings = IDebugging.DEBUG_KEYBINDINGS_ENABLED_DEFAULT;
			}


			public static class Buffers
			{
				@ConfigAnnotations.Entry
				public static GpuUploadMethod gpuUploadMethod = IBuffers.GPU_UPLOAD_METHOD_DEFAULT;

				@ConfigAnnotations.Entry(minValue = 0, maxValue = 5000)
				public static int gpuUploadPerMegabyteInMilliseconds = IBuffers.GPU_UPLOAD_PER_MEGABYTE_IN_MILLISECONDS_DEFAULT.defaultValue;

				@ConfigAnnotations.Entry
				public static BufferRebuildTimes rebuildTimes = IBuffers.REBUILD_TIMES_DEFAULT;
			}
		}
	}
}
