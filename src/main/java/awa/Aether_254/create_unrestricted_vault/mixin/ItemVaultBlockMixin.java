package awa.Aether_254.create_unrestricted_vault.mixin;

import awa.Aether_254.create_unrestricted_vault.UnrestrictedVaultConfig;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemVaultBlock.class)
abstract class ItemVaultBlockMixin {
    @Shadow(remap = false)
    @Final
    @Mutable
    public static Property<Direction.Axis> HORIZONTAL_AXIS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void createUnrestrictedVault$allowVerticalAxis(CallbackInfo ci) {
        HORIZONTAL_AXIS = BlockStateProperties.AXIS;
    }

    @Inject(method = "getStateForPlacement", at = @At("HEAD"), cancellable = true)
    private void createUnrestrictedVault$placeVertical(
        BlockPlaceContext context,
        CallbackInfoReturnable<BlockState> cir
    ) {
        UnrestrictedVaultConfig.Data config = UnrestrictedVaultConfig.get();
        ItemVaultBlock self = (ItemVaultBlock) (Object) this;
        Player player = context.getPlayer();
        BlockState placedOn = context.getLevel().getBlockState(
            context.getClickedPos().relative(context.getClickedFace().getOpposite())
        );
        Direction.Axis preferredAxis = ItemVaultBlock.getVaultBlockAxis(placedOn);

        if ((!config.enabled || !config.verticalVaultsEnabled)
            && player != null
            && !player.isShiftKeyDown()
            && preferredAxis == Direction.Axis.Y) {
            cir.setReturnValue(self.defaultBlockState()
                .setValue(ItemVaultBlock.HORIZONTAL_AXIS, context.getHorizontalDirection().getAxis()));
            return;
        }

        if (config.enabled
            && config.verticalVaultsEnabled
            && player != null
            && player.isShiftKeyDown()
            && context.getClickedFace().getAxis() == Direction.Axis.Y) {
            cir.setReturnValue(self.defaultBlockState()
                .setValue(ItemVaultBlock.HORIZONTAL_AXIS, Direction.Axis.Y));
        }
    }
}
