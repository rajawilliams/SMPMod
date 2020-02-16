package com.alotofletters.smpmod.init;

import com.alotofletters.smpmod.SMPMod;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Easy organization for eazy registry.
 */
public class SMPBlocks {

	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, SMPMod.MODID);

	public static final RegistryObject<Block> COMPRESSED_DIAMOND_BLOCK_1 = BLOCKS.register("compressed_db1", () ->
			new Block(Properties.create(Material.IRON, MaterialColor.DIAMOND).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL)));
}
