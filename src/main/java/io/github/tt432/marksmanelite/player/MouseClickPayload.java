package io.github.tt432.marksmanelite.player;

import io.github.tt432.marksmanelite.MarksmanElite;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MouseClickPayload(boolean isLeft, boolean isPress) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, MouseClickPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            MouseClickPayload::isLeft,
            ByteBufCodecs.BOOL,
            MouseClickPayload::isPress,
            MouseClickPayload::new
    );

    public static final CustomPacketPayload.Type<MouseClickPayload> TYPE =
            new CustomPacketPayload.Type<MouseClickPayload>(ResourceLocation.fromNamespaceAndPath(MarksmanElite.MODID, "mouse_behavior"));

    @Override
    public Type<MouseClickPayload> type() {
        return TYPE;
    }

    public static void handleDataOnServer(MouseClickPayload payload, IPayloadContext context) {
        PlayerGunState data = context.player().getData(MarksmanElite.PLAYER_GUN_STATE_ATTACHMENT);
        data.handle(payload,context.player());
    }
}
