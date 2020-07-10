package io.github.novarch129.jojomod.client.entity.render;

import io.github.novarch129.jojomod.entity.stands.CrazyDiamondEntity;
import io.github.novarch129.jojomod.JojoBizarreSurvival;
import io.github.novarch129.jojomod.client.entity.model.CrazyDiamondModel;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CrazyDiamondRenderer extends MobRenderer<CrazyDiamondEntity, CrazyDiamondModel<CrazyDiamondEntity>> {
	protected static final ResourceLocation TEXTURE = new ResourceLocation(JojoBizarreSurvival.MOD_ID, "textures/stands/crazy_diamond.png");

	public CrazyDiamondRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new CrazyDiamondModel<>(), 0.5f);
	}

	@Nonnull
	@Override
	public ResourceLocation getEntityTexture(final CrazyDiamondEntity entity) {
		return TEXTURE;
	}
}

