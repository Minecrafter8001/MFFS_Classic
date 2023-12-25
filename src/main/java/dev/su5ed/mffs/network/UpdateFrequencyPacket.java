package dev.su5ed.mffs.network;

import dev.su5ed.mffs.setup.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.NetworkEvent;

public record UpdateFrequencyPacket(BlockPos pos, int frequency) {
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.frequency);
    }

    public static UpdateFrequencyPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int frequency = buf.readInt();
        return new UpdateFrequencyPacket(pos, frequency);
    }

    public void processServerPacket(NetworkEvent.Context ctx) {
        Level level = ctx.getSender().level();
        Network.findBlockEntity(ModCapabilities.FORTRON, level, this.pos)
            .ifPresent(be -> be.setFrequency(this.frequency));
    }
}
