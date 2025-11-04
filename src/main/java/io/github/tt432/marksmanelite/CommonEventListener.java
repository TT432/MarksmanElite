package io.github.tt432.marksmanelite;

import io.github.tt432.marksmanelite.player.MouseClickPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MarksmanElite.MODID)
public class CommonEventListener {
    @SubscribeEvent
    public static void registryPayload(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.commonToServer(MouseClickPayload.TYPE, MouseClickPayload.STREAM_CODEC, MouseClickPayload::handleDataOnServer);
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event) {
        event.getEntity().getData(MarksmanElite.PLAYER_GUN_STATE_ATTACHMENT).tick(event.getEntity());
    }
}
