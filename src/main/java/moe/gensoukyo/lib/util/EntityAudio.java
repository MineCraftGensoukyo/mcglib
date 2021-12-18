package moe.gensoukyo.lib.util;

import moe.gensoukyo.lib.internal.common.network.MCGLibNetworkManager;
import moe.gensoukyo.lib.internal.common.network.PacketStCEntitySound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

/**
 * @author ChloePrime
 */
@SuppressWarnings("unused")
public class EntityAudio {
    public static void bindSound(
            Entity entity, String soundId, SoundCategory category
    ) {
        bindSound(entity, soundId, category, 1F, 1F, 256);
    }

    public static void bindSound(
            Entity entity, String soundId, SoundCategory category,
            float volume, float pitch, double eventRange
    ) {
        PacketStCEntitySound packet = new PacketStCEntitySound();
        packet.setEntityId(entity.getEntityId());
        packet.setSound(soundId);
        packet.setCategory(category);
        packet.setVolume(volume);
        packet.setPitch(pitch);

        TargetPoint target = new TargetPoint(
                entity.dimension,
                entity.posX,
                entity.posY,
                entity.posZ,
                eventRange
        );

        MCGLibNetworkManager.CHANNEL.sendToAllAround(packet, target);
    }

    private EntityAudio() {
    }
}
