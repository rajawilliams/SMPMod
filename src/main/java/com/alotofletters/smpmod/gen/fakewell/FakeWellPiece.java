package com.alotofletters.smpmod.gen.fakewell;

import com.alotofletters.smpmod.init.gen.SMPPieceRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import static com.alotofletters.smpmod.SMPMod.MODID;

import java.util.Random;

public class FakeWellPiece extends TemplateStructurePiece {

	private static final ResourceLocation fakeWellLocation = new ResourceLocation(MODID,"fake_well");

	public FakeWellPiece(TemplateManager manager, BlockPos position) {
		super(SMPPieceRegistry.FAKE_WELL, 0);
		this.templatePosition = position;
		this.loadTemplate(manager);
	}

	public FakeWellPiece(TemplateManager manager, CompoundNBT nbt) {
		super(SMPPieceRegistry.FAKE_WELL, nbt);
		this.loadTemplate(manager);
	}

	public void loadTemplate(TemplateManager manager) {
		Template template = manager.getTemplateDefaulted(fakeWellLocation);
		PlacementSettings placementSettings = (new PlacementSettings())
				.setIgnoreEntities(true)
				.addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
		this.setup(template, templatePosition, placementSettings);
	}

	@Override
	protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb) {
		// no data markers ;c
	}
}
