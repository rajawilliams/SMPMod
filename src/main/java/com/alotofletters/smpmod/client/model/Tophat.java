package com.alotofletters.smpmod.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Unused Tophat model. Eventually might come to use.
 * @deprecated
 */
@OnlyIn(Dist.CLIENT)
public class Tophat extends BipedModel {
    public ModelRenderer tophat_cap;
    public ModelRenderer tophat_top;

    public Tophat() {
        super(0, 0, 64, 32); // first two arguments i'm unsure of, other 2 are texture size
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.tophat_cap = new ModelRenderer(this, 0, 0);
        this.tophat_cap.setRotationPoint(-3.0F, -9.0F, -3.0F);
        this.tophat_cap.addBox(0.0F, 0.0F, 0.0F, 6, 1, 6, 0.0F);
        this.tophat_top = new ModelRenderer(this, 24, 0);
        this.tophat_top.setRotationPoint(-2.0F, -13.0F, -2.0F);
        this.tophat_cap.addBox(0.0F, 0.0F, 0.0F, 6, 1, 6, 0.0F);
    }

    /**
     * Return ALL ModelRenderers inside the model.
     * @return Iterable of all ModelRenderers (including the ones from BipedModel!)
     */
    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return Iterables.concat(super.getHeadParts(), ImmutableList.of(tophat_cap));
    }

    /**
     * Easily rotate ModelRenderers. Unused.
     * @deprecated
     * @param modelRenderer The ModelRenderer to rotate.
     * @param x The X rotation.
     * @param y The Y rotation.
     * @param z The Z rotation.
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
