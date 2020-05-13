package com.novarch.jojomod.network.message;

import com.novarch.jojomod.util.DimensionHopTeleporter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncDimensionHopOther
{
    public int type;
    public UUID player;

    public SyncDimensionHopOther(int type, UUID player)
    {
        this.type = type;
        this.player = player;
    }

    public SyncDimensionHopOther() {}

    public static void encode(SyncDimensionHopOther msg, PacketBuffer buffer)
    {
        buffer.writeInt(msg.type);
        buffer.writeUniqueId(msg.player);
    }

    public static SyncDimensionHopOther decode(PacketBuffer buffer)
    {
        SyncDimensionHopOther msg = new SyncDimensionHopOther();
        msg.type = buffer.readInt();
        msg.player = buffer.readUniqueId();
        return msg;
    }

    public static void handle(SyncDimensionHopOther msg, Supplier<NetworkEvent.Context> supplier)
    {
        final NetworkEvent.Context ctx = supplier.get();
        if(ctx.getDirection().getReceptionSide().isServer())
        {
            ctx.enqueueWork(() ->
            {
                ServerPlayerEntity sender = ctx.getSender();
                if(sender == null)
                    return;
                dimensionHop(sender, msg.player, msg.type);
            });
        }
        ctx.setPacketHandled(true);
    }

    protected static void dimensionHop(PlayerEntity player, UUID uuid, int type)
    {
        if(uuid != null)
        {
            PlayerEntity passenger = player.world.getPlayerByUuid(uuid);
            passenger.changeDimension(DimensionType.getById(type), new DimensionHopTeleporter((ServerWorld) passenger.getEntityWorld(), passenger.getPosX(), passenger.getPosY(), passenger.getPosZ()));
        }
    }
}