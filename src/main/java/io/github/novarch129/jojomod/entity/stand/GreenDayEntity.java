package io.github.novarch129.jojomod.entity.stand;

import io.github.novarch129.jojomod.capability.stand.Stand;
import io.github.novarch129.jojomod.entity.stand.attack.GreenDayPunchEntity;
import io.github.novarch129.jojomod.init.SoundInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

@SuppressWarnings("ConstantConditions")
public class GreenDayEntity extends AbstractStandEntity {
    public GreenDayEntity(EntityType<? extends AbstractStandEntity> type, World world) {
        super(type, world);
    }

    @Override
    public SoundEvent getSpawnSound() {
        return SoundInit.SPAWN_HIEROPHANT_GREEN.get();
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
                GreenDayPunchEntity greenDayPunchEntity = new GreenDayPunchEntity(world, this, getMaster());
                greenDayPunchEntity.shoot(getMaster(), rotationPitch, rotationYaw, 1.5f, 0.2f);
                world.addEntity(greenDayPunchEntity);
            }
    }

    @Override
    public void tick() {
        super.tick();
        if (getMaster() != null) {
            Stand.getLazyOptional(master).ifPresent(props -> ability = props.getAbility());
            master.setNoGravity(false);

            followMaster();
            setRotationYawHead(master.rotationYawHead);
            setRotation(master.rotationYaw, master.rotationPitch);

            if (!world.isRemote)
                getServer().getWorld(dimension).getEntities()
                        .filter(entity -> entity instanceof LivingEntity)
                        .filter(entity -> entity.getDistance(this) < master.getHealth())
                        .filter(entity -> !entity.areEyesInFluid(FluidTags.WATER))
                        .filter(entity -> entity.getMotion().getY() < 0)
                        .forEach(entity -> ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.WITHER, 40, 2, false, false)));
            if (master.swingProgressInt == 0 && !attackRush)
                attackTick = 0;
            if (attackRush) {
                master.setSprinting(false);
                attackTicker++;
                if (attackTicker >= 10)
                    if (!world.isRemote) {
                        master.setSprinting(false);
                        GreenDayPunchEntity greenDay1 = new GreenDayPunchEntity(world, this, master);
                        greenDay1.setRandomPositions();
                        greenDay1.shoot(master, master.rotationPitch, master.rotationYaw, 1, 0.25f);
                        world.addEntity(greenDay1);
                        GreenDayPunchEntity greenDay2 = new GreenDayPunchEntity(world, this, master);
                        greenDay2.setRandomPositions();
                        greenDay2.shoot(master, master.rotationPitch, master.rotationYaw, 1, 0.25f);
                        world.addEntity(greenDay2);
                    }
                if (attackTicker >= 80) {
                    attackRush = false;
                    attackTicker = 0;
                }
            }
        }
    }
}
