package awa.Aether_254.create_unrestricted_vault;

import net.fabricmc.api.ModInitializer;

public final class CreateUnrestrictedVault implements ModInitializer {
    @Override
    public void onInitialize() {
        UnrestrictedVaultConfig.load();
    }
}
