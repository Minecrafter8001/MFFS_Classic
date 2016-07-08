package com.mffs;

import com.mffs.api.IBlockFrequency;
import com.mffs.api.event.ChunkModifiedEvent;
import com.mffs.api.fortron.FrequencyGrid;
import com.mffs.api.fortron.IFortronFrequency;
import com.mffs.api.vector.Vector3D;
import com.mffs.model.blocks.BlockForceField;
import com.mffs.model.fluids.Fortron;
import com.mffs.model.tile.type.TileForceField;
import com.mffs.model.tile.type.TileForceFieldProjector;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * @author Calclavia
 */
public class ModEventHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void preTextureHook(TextureStitchEvent event) {
        if (event.map.getTextureType() == 0)
            Fortron.fluidIcon = event.map.registerIcon(MFFS.MODID + ":fortron");
    }

    @SubscribeEvent
    public void chunkModify(ChunkModifiedEvent event) {
        if(event.world.isRemote || event.block == null)
            return;
        for(IBlockFrequency freq : FrequencyGrid.instance().get()) {
            if(freq instanceof TileForceFieldProjector && ((TileEntity)freq).getWorldObj() == event.world) {
                TileForceFieldProjector proj = (TileForceFieldProjector) freq;
                if(proj.getCalculatedField() != null && proj.getCalculatedField().contains(new Vector3D(event.x, event.y, event.z)))
                    proj.markFieldUpdate = true;
            }
        }
    }

    @SubscribeEvent
    public void playerInteraction(PlayerInteractEvent event) {
        if(event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
            return;

        if(event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && event.world.getBlock(event.x, event.y, event.z) instanceof BlockForceField) {
            event.setCanceled(true);
            return;
        }

        if(event.entityPlayer.capabilities.isCreativeMode)
            return;

        //TODO: interdiction matrix.
    }
}
