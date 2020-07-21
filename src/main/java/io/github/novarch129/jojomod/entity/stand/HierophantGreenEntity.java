package io.github.novarch129.jojomod.entity.stand;

import io.github.novarch129.jojomod.entity.stand.attack.EmeraldSplashEntity;
import io.github.novarch129.jojomod.entity.stand.attack.HierophantGreenTailEntity;
import io.github.novarch129.jojomod.init.SoundInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

@SuppressWarnings("ConstantConditions")
public class HierophantGreenEntity extends AbstractStandEntity {
    public LivingEntity possessedEntity;

    public HierophantGreenEntity(EntityType<? extends AbstractStandEntity> type, World world) {
        super(type, world);
    }

    @Override
    public SoundEvent getSpawnSound() {
        return SoundInit.SPAWN_THE_HAND.get();
    }

    @Override
    public void attack(boolean special) {
        if (getMaster() == null) return;
        attackTick++;
        if (attackTick == 1)
            if (special)
                attackRush = true;
            else {
                world.playSound(null, getPosition(), SoundInit.PUNCH_MISS.get(), SoundCategory.NEUTRAL, 1, 0.6f / (rand.nextFloat() * 0.3f + 1) * 2);
                HierophantGreenTailEntity hierophantGreenTailEntity = new HierophantGreenTailEntity(world, this, getMaster());
                hierophantGreenTailEntity.shoot(getMaster(), rotationPitch, rotationYaw, 3, 0.15f);
                world.addEntity(hierophantGreenTailEntity);
            }
    }

    @Override
    public void tick() {
        super.tick();
        if (master != null) {
            if (master.getLastAttackedEntity() != null)
                possessedEntity = master.getLastAttackedEntity();
            if (possessedEntity instanceof MobEntity)
                ((MobEntity) possessedEntity).setNoAI(true);

            followMaster();
            setRotationYawHead(master.getRotationYawHead());
            setRotation(master.rotationYaw, master.rotationPitch);

            if (master.swingProgressInt == 0 && !attackRush)
                attackTick = 0;
            if (attackRush) {
                master.setSprinting(false);
                attackTicker++;
                if (attackTicker >= 10)
                    if (!world.isRemote) {
                        master.setSprinting(false);
                        EmeraldSplashEntity emeraldSplashEntity = new EmeraldSplashEntity(world, this, master);
                        emeraldSplashEntity.setRandomPositions();
                        emeraldSplashEntity.shoot(master, master.rotationPitch, master.rotationYaw, 2, 0.25f);
                        world.addEntity(emeraldSplashEntity);
                        EmeraldSplashEntity emeraldSplashEntity1 = new EmeraldSplashEntity(world, this, master);
                        emeraldSplashEntity1.setRandomPositions();
                        emeraldSplashEntity1.shoot(master, master.rotationPitch, master.rotationYaw, 2, 0.25f);
                        world.addEntity(emeraldSplashEntity1);
                    }
                if (attackTicker >= 80) {
                    attackRush = false;
                    attackTicker = 0;
                }
            }
        }
    }
}