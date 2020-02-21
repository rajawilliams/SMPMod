package com.alotofletters.smpmod.init;

import com.alotofletters.smpmod.SMPMod;
import com.alotofletters.smpmod.enchantment.ProspectingEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SMPEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = new DeferredRegister<>(ForgeRegistries.ENCHANTMENTS, SMPMod.MODID);

	public static final RegistryObject<Enchantment> PROSPECTING = ENCHANTMENTS.register("prospecting", ProspectingEnchantment::new);
}
