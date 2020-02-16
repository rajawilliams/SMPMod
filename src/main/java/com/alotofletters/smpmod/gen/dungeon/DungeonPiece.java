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
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.common.DungeonHooks;

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

	private Template template;

	/**
	 * Constructor for the structure to simplify stuff.
	 * @param templateManager TemplateManager for loading in the template.
	 * @param templateName Name of the template to load.
	 * @param templatePosition Position of the template to load.
	 * @param rotation Rotation of the template to load.
	 */
	public DungeonPiece(TemplateManager templateManager, String templateName, BlockPos templatePosition, Rotation rotation) {
		super(SMPPieceRegistry.SDUNGEON, 0);
		this.templatePosition = templatePosition;
		this.templateName = templateName;
		this.rotation = rotation;
		this.loadTemplate(templateManager);
	}

	/**
	 * Get a dungeon piece from NBT.
	 * @param templateManager TemplateManager to load in the template.
	 * @param compoundNBT NBT to load.
	 */
	public DungeonPiece(TemplateManager templateManager, CompoundNBT compoundNBT) {
		super(SMPPieceRegistry.SDUNGEON, compoundNBT);
		this.templateName = compoundNBT.getString("Template");
		this.rotation = Rotation.valueOf(compoundNBT.getString("Rotation"));
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
	}

	/**
	 * Load the template in.
	 * @param templateManager Template manaager.
	 */
	private void loadTemplate(TemplateManager templateManager) {
		template = templateManager.getTemplateDefaulted(new ResourceLocation(SMPMod.MODID + ":" + this.templateName));
		PlacementSettings placementSettings = (new PlacementSettings())
				.setIgnoreEntities(true)
				.setRotation(this.rotation)
				.setMirror(Mirror.NONE)
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
					((MobSpawnerTileEntity)tileentity).getSpawnerBaseLogic().setEntityType(this.getRandomDungeonMob(random));
				} else {
					SMPMod.LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", blockPos.getX(), blockPos.getY(), blockPos.getZ());
				}
				break;
			case "smpmod:spawner_chest": // the chests near the spawner
				world.setBlockState(blockPos, Blocks.CAVE_AIR.getDefaultState(), 2);
				LockableLootTileEntity.setLootTable(world, random, blockpos2, LootTables.CHESTS_SIMPLE_DUNGEON); // TODO: initialize and use custom loot tables
				break;
			case "smpmod:chest": // boring chests in the other room
				world.setBlockState(blockPos, Blocks.CAVE_AIR.getDefaultState(), 2);
				LockableLootTileEntity.setLootTable(world, random, blockpos2, LootTables.CHESTS_SPAWN_BONUS_CHEST); // TODO: initialize and use custom loot tables
				break;
		}
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

	public void addBlocksToWorld(IWorld worldIn, BlockPos pos, PlacementSettings placementIn) {
		template.addBlocksToWorld(worldIn, pos, placementIn);
	}
}
