package mod.linguardium.zombifier.mixin;

import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin {
    @Redirect(method="onKilledOther", at=@At(value="INVOKE",target="net/minecraft/world/World.getDifficulty()Lnet/minecraft/world/Difficulty;",ordinal=1))
    private Difficulty getHard(World world) {
        return Difficulty.HARD;
    }
    @Redirect(method="onKilledOther", at=@At(value="INVOKE",target="net/minecraft/world/World.getDifficulty()Lnet/minecraft/world/Difficulty;",ordinal=2))
    private Difficulty getHarder(World world) {
        return Difficulty.HARD;
    }
}
