package io.github.novarch129.jojomod.entity.stand.attack;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.novarch129.jojomod.entity.stand.AbstractStandEntity;
import io.github.novarch129.jojomod.init.EntityInit;
import io.github.novarch129.jojomod.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class AerosmithBulletEntity extends AbstractStandAttackEntity {
    public AerosmithBulletEntity(World worldIn, AbstractStandEntity shooter, PlayerEntity player) {
        super(EntityInit.AEROSMITH_BULLET.get(), worldIn, shooter, player);
    }

    public AerosmithBulletEntity(EntityType<AerosmithBulletEntity> aerosmithEntityType, World world) {
        super(aerosmithEntityType, world);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
        entity.attackEntityFrom(DamageSource.causeMobDamage(standMaster), 1.5f);
        entity.hurtResistantTime = 0;
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult result) {
        BlockPos pos = result.getPos();
        BlockState state = world.getBlockState(pos);
        if (state.getBlockHardness(world, pos) != -1 && state.getBlockHardness(world, pos) < 3) {
            if (state.getBlock() == Blocks.TNT) {
                world.removeBlock(pos, false);
                world.createExplosion(this, pos.getX(), pos.getY(), pos.getZ(), 0.8f, Explosion.Mode.DESTROY);
            } else
                world.setBlockState(pos, world.rand.nextBoolean() ? Blocks.AIR.getDefaultState() : Blocks.FIRE.getDefaultState());
        }
    }

    @Override
    public ResourceLocation getEntityTexture() {
        return Util.ResourceLocations.AEROSMITH_BULLET;
    }

    @Override
    public <T extends AbstractStandAttackEntity> EntityModel<T> getEntityModel() { //They don't render at all.
        return new EntityModel<T>() {
            @Override
            public void setRotationAngles(T t, float v, float v1, float v2, float v3, float v4) {
            }

            @Override
            public void render(MatrixStack matrixStack, IVertexBuilder iVertexBuilder, int i, int i1, float v, float v1, float v2, float v3) {
            }
        };
    }
}
