package io.github.novarch129.jojomod.client.entity.render;

import io.github.novarch129.jojomod.client.entity.model.TwentiethCenturyBoyModel;
import io.github.novarch129.jojomod.entity.stand.TwentiethCenturyBoyEntity;
import io.github.novarch129.jojomod.util.Util;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class TwentiethCenturyBoyRenderer extends AbstractStandRenderer<TwentiethCenturyBoyEntity, TwentiethCenturyBoyModel> {
    public TwentiethCenturyBoyRenderer(EntityRendererManager manager) {
        super(manager, new TwentiethCenturyBoyModel());
    }

    @Override
    public ResourceLocation getEntityTexture(TwentiethCenturyBoyEntity entity) {
        return Util.ResourceLocations.TWENTIETH_CENTURY_BOY;
    }
}
