package com.novarch.jojomod;

import com.novarch.jojomod.capabilities.IStand;
import com.novarch.jojomod.capabilities.IStandCapability;
import com.novarch.jojomod.capabilities.JojoProvider;
import com.novarch.jojomod.entities.stands.EntityStandBase;
import com.novarch.jojomod.entities.stands.kingCrimson.EntityKingCrimson;
import com.novarch.jojomod.network.message.SyncAbilityButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Loader;

import com.novarch.jojomod.init.EntityInit;
import com.novarch.jojomod.init.ItemInit;
import com.novarch.jojomod.init.SoundInit;
import com.novarch.jojomod.network.message.SyncPlayerAttackMessage;
import com.novarch.jojomod.network.message.SyncStandSummonButton;
import com.novarch.jojomod.proxy.CommonProxy;
import com.novarch.jojomod.events.EventControlInputs;
import com.novarch.jojomod.util.JojoLibs;
import com.novarch.jojomod.util.handlers.KeyHandler;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@SuppressWarnings("unused")
@Mod("jojomod")
public class JojoBlockyAdventure
{
    public static final boolean debug = true;
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "jojomod";
    public static JojoBlockyAdventure instance;
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(MOD_ID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public JojoBlockyAdventure()
    {
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
           
        KeyHandler.addKeys();
        MinecraftForge.EVENT_BUS.register(EventControlInputs.class);
        ItemInit.ITEMS.register(modEventBus);
		EntityInit.ENTITY_TYPES.register(modEventBus);
		SoundInit.SOUNDS.register(modEventBus);
		
	    if (Loader.isClassAvailable("endercore"))
			   JojoLibs.setEnderCore(true); 
		
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        int networkId = 0;
		INSTANCE.registerMessage(networkId++,
				SyncStandSummonButton.class,
				SyncStandSummonButton::encode,
				SyncStandSummonButton::decode,
				SyncStandSummonButton::handle);
		INSTANCE.registerMessage(networkId++,
				SyncPlayerAttackMessage.class,
				SyncPlayerAttackMessage::encode,
				SyncPlayerAttackMessage::decode,
				SyncPlayerAttackMessage::handle);
		INSTANCE.registerMessage(networkId++,
                SyncAbilityButton.class,
                SyncAbilityButton::encode,
                SyncAbilityButton::decode,
                SyncAbilityButton::handle);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	CommonProxy.registerCapabilities();
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
    	
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event)
    {
        
    }
    
    public static class JojoItemGroup extends ItemGroup 
    {
		public static final ItemGroup instance = new JojoItemGroup(ItemGroup.GROUPS.length, "jojotab");

		private JojoItemGroup(int index, String label)
		{
			super(index, label);
		}

		@Override
		public ItemStack createIcon() 
		{
			return new ItemStack(ItemInit.stand_arrow.get());
		}
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        LazyOptional<IStand> power = event.player.getCapability(JojoProvider.STAND, null);
        IStand props = power.orElse(new IStandCapability());

        if(!props.getStandOn() && props.getCooldown() >= 0)
        {
            props.subtractCooldown(1);
        }

        if(props.getCooldown() <= 0)
        {

        }

        if(!props.getStandOn() && props.getTimeLeft() > 0)
        {
            props.subtractTimeLeft(1);
        }

        if(!props.getStandOn())
        {
            event.player.setInvulnerable(false);
            if(!event.player.isCreative() && !event.player.isSpectator())
                event.player.setGameType(GameType.SURVIVAL);
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(PlayerEvent.PlayerRespawnEvent event)
    {
        LazyOptional<IStand> power = event.getPlayer().getCapability(JojoProvider.STAND, null);
        IStand props = power.orElse(new IStandCapability());
        props.cloneSaveFunction(props);
    }
}