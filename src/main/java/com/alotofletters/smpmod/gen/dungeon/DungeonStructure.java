package com.alotofletters.smpmod.gen.dungeon;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;
import java.util.function.Function;

/**
 * Uses templates to use the "data/smpmod/structures/" folder. Insane, right?
 */
public class DungeonStructure extends ScatteredStructure<NoFeatureConfig> {

	/**
	 * Confusing constructor for a confusing generational algorithm.
	 * @param function A function? Not referenced directly in mod code.
	 */
	public DungeonStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> function) {
		super(function);
	}

	/**
	 * Get starting position.
	 * @param chunkGenerator World generation algorithm.
	 * @param random Random for generation
	 * @param x start chunk pos x
	 * @param z start chunk pos y
	 * @param spacingOffsetsX How spaced, x-wise?
	 * @param spacingOffsetsZ How spaced, y-wise?
	 * @return Ending chunk position
	 */
	@Override
	protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
		random.setSeed(this.getSeedModifier());
		final int distance = 20;
		final int separation = this.getSize() + 3;
		int x1 = x + distance * spacingOffsetsX;
		int z1 = z + distance * spacingOffsetsZ;
		int x2 = x1 < 0 ? x1 - distance + 1 : x1;
		int z2 = z1 < 0 ? z1 - distance + 1 : z1;
		int x3 = x2 / distance;
		int z3 = z2 / distance;
		((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), x3, z3, this.getSeedModifier());
		x3 = x3 * distance;
		z3 = z3 * distance;
		x3 = x3 + random.nextInt(distance - separation);
		z3 = z3 + random.nextInt(distance - separation);

		return new ChunkPos(x3, z3);
	}

	/**
	 * Seed modifier. Not sure what it is.
	 * @return int that is a seed modifier??
	 */
	@Override
	protected int getSeedModifier() {
		return 784400;
	}

	/**
	 * Seems to create a generator factory? Not sure.
	 * @return Constructor for DungeonStructure.Start.
	 * @see DungeonStructure.Start
	 */
	@Override
	public IStartFactory getStartFactory() {
		return Start::new;
	}

	/**
	 * The name of the structure for /locate among other things.
	 * Has to be human readable, aka display name in the locate
	 * command.
	 * @return String of the name of the structure.
	 */
	@Override
	public String getStructureName() {
		return "Improved_Dungeon";
	}

	/**
	 * I'll assume this is about how much the size should be.
	 * @return Size of the structure.
	 */
	@Override
	public int getSize() {
		return 5;
	}

	public static class Start extends StructureStart {

		/**
		 * Constructor for StructureStart. Required for some dumb reason.
		 */
		public Start(Structure<?> p_i225876_1_, int p_i225876_2_, int p_i225876_3_, MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
			super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
		}

		/**
		 * Actually generate the structure!
		 * @param generator Similar to IWorld I believe.
		 * @param templateManagerIn TemplateManager to get the actual templates.
		 * @param chunkX Chunk-relative x position.
		 * @param chunkZ Chunk-relative z position
		 * @param biomeIn The biome the structure is being generated in.
		 */
		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
			int x = (chunkX << 4) + 2;
			int z = (chunkZ << 4) + 2;

			Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
			int i = 5;
			int j = 5;
			if (rotation == Rotation.CLOCKWISE_90) {
				i = -5;
			}
			else if (rotation == Rotation.CLOCKWISE_180) {
				i = -5;
				j = -5;
			}
			else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
				j = -5;
			}

			int k = x + 7;
			int l = z + 7;
			int i1 = generator.func_222531_c(x, z, Heightmap.Type.WORLD_SURFACE_WG);
			int j1 = generator.func_222531_c(x, z + j, Heightmap.Type.WORLD_SURFACE_WG);
			int k1 = generator.func_222531_c(x + i, z, Heightmap.Type.WORLD_SURFACE_WG);
			int l1 = generator.func_222531_c(x + i, z + j, Heightmap.Type.WORLD_SURFACE_WG);

			int min = Math.min(Math.min(i1, j1), Math.min(k1, l1));
			int y = min - this.rand.nextInt(min + 12);

			DungeonPiece piece;
			piece = new DungeonPiece(templateManagerIn, "dungeon_plains", new BlockPos(x, y, z), rotation);

			this.components.add(piece); // add to components list
			this.recalculateStructureSize(); // recalculate structure size
		}
	}


}
