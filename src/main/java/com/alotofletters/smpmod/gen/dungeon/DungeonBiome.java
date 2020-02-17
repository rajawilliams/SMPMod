package com.alotofletters.smpmod.gen.dungeon;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.common.DungeonHooks;

import javax.annotation.Resource;
import java.util.Random;
import java.util.function.Function;

import static com.alotofletters.smpmod.SMPMod.MODID;

/**
 * TODO: move into DungeonPiece
 */
public enum DungeonBiome {

	PLAINS(Blocks.COBBLESTONE, Blocks.COBBLESTONE_WALL, r("dungeon_plains")),
	DESERT(Blocks.SANDSTONE, Blocks.SANDSTONE_WALL, r("dungeon_desert"), DesertDungeonHooks::getRandomDungeonMob),
	SPRUCE_GENERAL(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_FENCE, r("dungeon_spruce_general")),
	FLOWER(Blocks.WHITE_TERRACOTTA, Blocks.WHITE_TERRACOTTA, r("dungeon_flower"), r("chest_flower")),
	DESERT_SPIDER(Blocks.SANDSTONE, Blocks.SANDSTONE_WALL, r("dungeon_desert_spider"), random -> {
		return EntityType.CAVE_SPIDER;
	});

	public final Block poleMaterial;
	public final Block poleTopMaterial;
	public final ResourceLocation location;
	public final ResourceLocation chestLootTableLocation;
	public final ResourceLocation spawnerChestLootTableLocation;
	public final Function<Random, EntityType> spawnerFunction;

	DungeonBiome(Block poleMaterial, Block poleTopMaterial, ResourceLocation location) {
		this(poleMaterial, poleTopMaterial, location, r("chest_dungeon"), r("spawner_dungeon"), DungeonHooks::getRandomDungeonMob);
	}

	DungeonBiome(Block poleMaterial, Block poleTopMaterial, ResourceLocation location, ResourceLocation chestLootTableLocation) {
		this(poleMaterial, poleTopMaterial, location, chestLootTableLocation, r("spawner_dungeon"), DungeonHooks::getRandomDungeonMob);
	}

	DungeonBiome(Block poleMaterial, Block poleTopMaterial, ResourceLocation location, Function<Random, EntityType> spawnerFunction) {
		this(poleMaterial, poleTopMaterial, location, r("chest_dungeon"), r("spawner_dungeon"), spawnerFunction);
	}

	DungeonBiome(Block poleMaterial, Block poleTopMaterial, ResourceLocation location, ResourceLocation chestLootTableLocation, ResourceLocation spawnerChestLootTableLocation,
				 Function<Random, EntityType> spawnerFunction) {
		this.poleMaterial = poleMaterial;
		this.poleTopMaterial = poleTopMaterial;
		this.location = location;
		this.chestLootTableLocation = chestLootTableLocation;
		this.spawnerChestLootTableLocation = spawnerChestLootTableLocation;
		this.spawnerFunction = spawnerFunction;
	}

	@Override
	public String toString() {
		return location.getPath();
	}

	/**
	 * Better non-hardcoded way of doing this.
	 * @param string ResourceLocation path of the structure.
	 * @return DungeonBiome according to the path.
	 */
	public static DungeonBiome fromLocation(String string) {
		for (DungeonBiome biome : DungeonBiome.values()) {
			if (biome.location.getPath() == string) {
				return biome;
			}
		}
		return PLAINS;
	}

	/**
	 * Helper function to easily create a resourcelocation for structures.
	 * @param string Path for resourcelocation.
	 * @return ResourceLocation formed out of MODID and string.
	 */
	static ResourceLocation r(String string) {
		return new ResourceLocation(MODID, string);
	}
}
