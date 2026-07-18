package awa.Aether_254.create_unrestricted_vault.mixin;

import awa.Aether_254.create_unrestricted_vault.UnrestrictedVaultConfig;
import com.simibubi.create.content.logistics.vault.ItemVaultBlockEntity;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemVaultBlockEntity.class)
abstract class ItemVaultBlockEntityMixin {
    @Inject(method = "getMainConnectionAxis()Lnet/minecraft/core/Direction$Axis;", at = @At("HEAD"), cancellable = true, remap = false)
    private void createUnrestrictedVault$useActualVaultAxis(CallbackInfoReturnable<Direction.Axis> cir) {
        ItemVaultBlockEntity self = (ItemVaultBlockEntity) (Object) this;
        Direction.Axis axis = ItemVaultBlock.getVaultBlockAxis(self.getBlockState());
        if (axis != null)
            cir.setReturnValue(axis);
    }

    @Inject(method = "updateComparators()V", at = @At("HEAD"), cancellable = true, remap = false)
    private void createUnrestrictedVault$updateVerticalComparators(CallbackInfo ci) {
        ItemVaultBlockEntity self = (ItemVaultBlockEntity) (Object) this;
        ItemVaultBlockEntity controller = self.getControllerBE();
        if (controller == null || controller.getMainConnectionAxis() != Direction.Axis.Y)
            return;

        Level level = controller.getLevel();
        BlockPos origin = controller.getBlockPos();
        Block block = controller.getBlockState().getBlock();
        for (int y = 0; y < controller.getHeight(); y++) {
            for (int x = 0; x < controller.getWidth(); x++) {
                for (int z = 0; z < controller.getWidth(); z++)
                    level.updateNeighbourForOutputSignal(origin.offset(x, y, z), block);
            }
        }
        ci.cancel();
    }

    @Inject(method = "getMaxWidth()I", at = @At("RETURN"), cancellable = true, remap = false)
    private void createUnrestrictedVault$configureMaximumWidth(CallbackInfoReturnable<Integer> cir) {
        UnrestrictedVaultConfig.Data config = UnrestrictedVaultConfig.get();
        if (config.enabled)
            cir.setReturnValue(config.maxWidth);
    }

    @Inject(method = "getMaxLength(Lnet/minecraft/core/Direction$Axis;I)I", at = @At("RETURN"), cancellable = true, remap = false)
    private void createUnrestrictedVault$configureMaximumLength(
        Direction.Axis longAxis,
        int width,
        CallbackInfoReturnable<Integer> cir
    ) {
        if (UnrestrictedVaultConfig.get().enabled)
            cir.setReturnValue(UnrestrictedVaultConfig.maxLength(width));
    }

    @Redirect(
        method = "initCapability()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/BlockPos;offset(III)Lnet/minecraft/core/BlockPos;",
            ordinal = 0,
            remap = true
        ),
        remap = false
    )
    private BlockPos createUnrestrictedVault$locateVerticalPart(BlockPos origin, int x, int y, int z) {
        ItemVaultBlockEntity self = (ItemVaultBlockEntity) (Object) this;
        if (ItemVaultBlock.getVaultBlockAxis(self.getBlockState()) == Direction.Axis.Y)
            return origin.offset(y, x, z);
        return origin.offset(x, y, z);
    }

    @Redirect(
        method = "initCapability()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/BlockPos;offset(III)Lnet/minecraft/core/BlockPos;",
            ordinal = 1,
            remap = true
        ),
        remap = false
    )
    private BlockPos createUnrestrictedVault$locateVerticalFarCorner(BlockPos origin, int x, int y, int z) {
        ItemVaultBlockEntity self = (ItemVaultBlockEntity) (Object) this;
        if (ItemVaultBlock.getVaultBlockAxis(self.getBlockState()) == Direction.Axis.Y)
            return origin.offset(y, x, z);
        return origin.offset(x, y, z);
    }
}
