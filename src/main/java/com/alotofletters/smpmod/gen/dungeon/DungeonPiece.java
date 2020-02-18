package com.alotofletters.smpmod.gen.dungeon;

import com.alotofletters.smpmod.SMPMod;
import com.alotofletters.smpmod.init.gen.SMPPieceRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.common.DungeonHooks;

import java.util.Hashtable;
import java.util.Random;

/**
 * I believe "TemplateStructurePiece" means from the structures folder in data.
 * Partially confusing though.
 */
public class DungeonPiece extends TemplateStructurePiece {

	/**
	 * Name of template. Represents a ResourceLocation for the ending template/structure.
	 */
	private final String templateName;
	/**
	 * End rotation of template.
	 */
	private final Rotation rotation;
	private final DungeonBiome biome;
	private Template template;
	private ChunkGenerator<?> chunkGenerator;

	/**
	 * Constructor that defaults to plains.
	 * @param templateManager TemplateManager for loading in the template.
	 * @param templatePosition Position of the template to load.
	 * @param rotation Rotation of the template to load.
	 */
	public DungeonPiece(TemplateManager templateManager, BlockPos templatePosition, Rotation rotation) {
		this(templateManager, templatePosition, rotation, DungeonBiome.PLAINS);
	}

	/**
	 * Constructor for the structure to simplify stuff.
	 * @param templateManager TemplateManager for loading in the template.
	 * @param templatePosition Position of the template to load.
	 * @param rotation Rotation of the template to load.
	 * @param biome Biome of the template to load.
	 */
	public DungeonPiece(TemplateManager templateManager, BlockPos templatePosition, Rotation rotation, DungeonBiome biome) {
		super(SMPPieceRegistry.DUNGEON, 0);
		this.templatePosition = templatePosition;
		this.templateName = biome.location.getPath();
		this.rotation = rotation;
		this.biome = biome;
		this.loadTemplate(templateManager);
	}

	/**
	 * Get a dungeon piece from NBT.
	 * @param templateManager TemplateManager to load in the template.
	 * @param compoundNBT NBT to load.
	 */
	public DungeonPiece(TemplateManager templateManager, CompoundNBT compoundNBT) {
		super(SMPPieceRegistry.DUNGEON, compoundNBT);
		this.templateName = compoundNBT.getString("Template");
		this.rotation = Rotation.valueOf(compoundNBT.getString("Rotation"));
		this.biome = DungeonBiome.fromLocation(compoundNBT.getString("Biome"));
		this.loadTemplate(templateManager);
	}

	/**
	 * Save piece to NBT
	 * @param tagCompound NBT to save to
	 */
	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putString("Template", this.templateName);
		tagCompound.putString("Rotation", this.placeSettings.getRotation().name()); // TODO: why not this.rotation.name()?
		tagCompound.putString("Biome", this.biome.toString());
	}

	/**
	 * Load the template in.
	 * @param templateManager Template manaager.
	 */
	private void loadTemplate(TemplateManager templateManager) {
		template = templateManager.getTemplateDefaulted(new ResourceLocation(SMPMod.MODID + ":" + this.templateName));
		PlacementSettings placementSettings = (new PlacementSettings())
				.setIgnoreEntities(true)
				.addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
		this.setup(template, this.templatePosition, placementSettings);
	}

	/**
	 * Handle each and every data marker.
	 * @param dataFunction The data marker's actual data tag. Useful!
	 * @param blockPos Data marker's position
	 * @param world ZA WARUDO!... or the world.
	 * @param random Random for map generation.
	 * @param boundingBox Bounding box for the something...
	 */
	@Override
	protected void handleDataMarker(String dataFunction, BlockPos blockPos, IWorld world, Random random, MutableBoundingBox boundingBox) {
		BlockPos blockpos2 = blockPos.add(0, -1, 0);
		switch (dataFunction) {
			case "smpmod:spawner":
				world.setBlockState(blockPos, Blocks.SPAWNER.getDefaultState(), 2);
				TileEntity tileentity = world.getTileEntity(blockPos);
				if (tileentity instanceof MobSpawnerTileEntity) {
					((MobSpawnerTileEntity)tileentity).getSpawnerBaseLogic().setEntityType(biome.spawnerFunction.apply(random));
				} else {
					SMPMod.LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", blockPos.getX(), blockPos.getY(), blockPos.getZ());
				}
				break;
			case "smpmod:spawner_chest": // the chests near the spawner
				world.setBlockState(blockPos, Blocks.CAVE_AIR.getDefaultState(), 2);
				LockableLootTileEntity.setLootTable(world, random, blockpos2, biome.spawnerChestLootTableLocation); // TODO: initialize and use custom loot tables
				break;
			case "smpmod:chest": // boring chests in the other room
				world.setBlockState(blockPos, Blocks.CAVE_AIR.getDefaultState(), 2);
				LockableLootTileEntity.setLootTable(world, random, blockpos2, biome.chestLootTableLocation); // TODO: initialize and use custom loot tables
				break;
			case "smpmod:pole":
				world.setBlockState(blockPos, biome.poleMaterial.getDefaultState(), 2);
//				if (world.chunkExists((blockPos.getX() >> 4), (blockPos.getZ() >> 4))) {
//					int y = this.chunkGenerator.func_222531_c(blockPos.getX(), blockPos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
//					for (int i = blockPos.getY(); i < y + 5; i++) {
//						world.setBlockState(bindToBoundingBox(new BlockPos(blockPos.getX(), i, blockPos.getZ()), boundingBox),
//								biome.poleMaterial.getDefaultState(),
//								2);
//					}
//					world.setBlockState(bindToBoundingBox(new BlockPos(blockPos.getX(), y + 5, blockPos.getZ()), boundingBox),
//							biome.poleTopMaterial.getDefaultState(),
//							2);
//				}
				break;
		}
	}

	private BlockPos bindToBoundingBox(BlockPos pos, MutableBoundingBox box) {
		return new BlockPos(Math.min(box.maxX, Math.max(pos.getX(), box.minX)),
							Math.min(192, Math.max(pos.getY(), 1)),
							Math.min(box.maxZ, Math.max(pos.getZ(), box.minZ)));
	}

	/**
	 * Ripped straight from DungeonsFeature.
	 * @param rand Random for world generation
	 * @return A random weighted EntityType
	 * @see net.minecraft.world.gen.feature.DungeonsFeature
	 * @see DungeonHooks
	 */
	private EntityType<?> getRandomDungeonMob(Random rand) {
		return DungeonHooks.getRandomDungeonMob(rand);
	}

	private EntityType<?> getRandomDesertDungeonMob(Random rand) {
		return DesertDungeonHooks.getRandomDungeonMob(rand);
	}

	@Override
	public boolean func_225577_a_(IWorld worldIn, ChunkGenerator<?> chunkGenerator, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {
		this.chunkGenerator = chunkGenerator;
		return super.func_225577_a_(worldIn, chunkGenerator, randomIn, structureBoundingBoxIn, chunkPosIn);
	}
}
