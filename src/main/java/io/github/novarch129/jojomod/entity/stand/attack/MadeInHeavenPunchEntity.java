package io.github.novarch129.jojomod.entity.stand.attack;

import io.github.novarch129.jojomod.capability.Stand;
import io.github.novarch129.jojomod.client.entity.model.DefaultStandAttackModel;
import io.github.novarch129.jojomod.entity.stand.AbstractStandEntity;
import io.github.novarch129.jojomod.init.EntityInit;
import io.github.novarch129.jojomod.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public class MadeInHeavenPunchEntity extends AbstractStandAttackEntity {
    public MadeInHeavenPunchEntity(EntityType<? extends AbstractStandAttackEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public MadeInHeavenPunchEntity(World worldIn, AbstractStandEntity shooter, PlayerEntity player) {
        super(EntityInit.MADE_IN_HEAVEN_PUNCH.get(), worldIn, shooter, player);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
        int act = Stand.getCapabilityFromPlayer(standMaster).getAct();
        switch (act) {
            default: {
                entity.attackEntityFrom(DamageSource.causeMobDamage(standMaster), 3);
                entity.hurtResistantTime = 0;
            }
            break;
            case 1: {
                CMoonPunchEntity cMoonPunch = new CMoonPunchEntity(world, shootingStand, standMaster);
                cMoonPunch.onEntityHit(result);
                break;
            }
            case 2: {
                WhitesnakePunchEntity whitesnakePunch = new WhitesnakePunchEntity(world, shootingStand, standMaster);
                whitesnakePunch.onEntityHit(result);
                break;
            }
        }
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult result) {
        BlockPos pos = result.getPos();
        BlockState state = world.getBlockState(pos);
        int act = Stand.getCapabilityFromPlayer(standMaster).getAct();
        switch (act) {
            default: {
                world.removeBlock(pos, false);
                state.getBlock().harvestBlock(world, standMaster, pos, state, null, standMaster.getActiveItemStack());
                break;
            }
            case 1: {
                CMoonPunchEntity cMoonPunch = new CMoonPunchEntity(world, shootingStand, standMaster);
                cMoonPunch.onBlockHit(result);
                break;
            }
            case 2: {
                WhitesnakePunchEntity whitesnakePunch = new WhitesnakePunchEntity(world, shootingStand, standMaster);
                whitesnakePunch.onBlockHit(result);
                break;
            }
        }
    }

    @Override
    public ResourceLocation getEntityTexture() {
        return Util.ResourceLocations.MADE_IN_HEAVEN_PUNCH;
    }

    @Override
    public <T extends AbstractStandAttackEntity> EntityModel<T> getEntityModel() {
        return new DefaultStandAttackModel<>();
    }
}
