package dev.su5ed.mffs.util;

import dev.su5ed.mffs.MFFSMod;
import dev.su5ed.mffs.api.module.Module;
import dev.su5ed.mffs.api.module.ProjectorMode;
import dev.su5ed.mffs.blockentity.ProjectorBlockEntity;
import dev.su5ed.mffs.setup.ModModules;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import one.util.streamex.StreamEx;

import java.util.Set;

/**
 * A thread that allows multi-threading calculation of projector fields.
 *
 * @author Calclavia
 */
public class ProjectorCalculationThread extends Thread {
    private final ProjectorBlockEntity projector;

    public ProjectorCalculationThread(ProjectorBlockEntity projector) {
        this.projector = projector;
    }

    @Override
    public void run() {
        this.projector.isCalculating = true;

        try {
            ProjectorMode mode = this.projector.getMode().orElseThrow();
            Set<Vec3> fieldPoints = this.projector.hasModule(ModModules.INVERTER) ? mode.getInteriorPoints(this.projector) : mode.getExteriorPoints(this.projector);

            BlockPos translation = this.projector.getTranslation();
            int rotationYaw = this.projector.getRotationYaw();
            int rotationPitch = this.projector.getRotationPitch();
            int rotationRoll = this.projector.getRotationRoll();

            StreamEx.of(fieldPoints)
                .map(pos -> rotationYaw != 0 || rotationPitch != 0 || rotationRoll != 0 ? ModUtil.rotateByAngleExact(pos, rotationYaw, rotationPitch, rotationRoll) : pos)
                .map(pos -> {
                    BlockPos projPos = this.projector.getBlockPos();
                    return pos.add(projPos.getX(), projPos.getY(), projPos.getZ()).add(translation.getX(), translation.getY(), translation.getZ());
                })
                .filter(pos -> pos.y() <= this.projector.getLevel().getHeight())
                .forEach(pos -> this.projector.getCalculatedField().add(new BlockPos(Math.round(pos.x), Math.round(pos.y), Math.round(pos.z))));

            for (Module module : this.projector.getModules()) {
                module.onCalculate(this.projector, this.projector.getCalculatedField());
            }
        } catch (Exception e) {
            MFFSMod.LOGGER.error("Error calculating force field", e);
        }

        this.projector.isCalculating = false;
        this.projector.isCalculated = true;
    }
}