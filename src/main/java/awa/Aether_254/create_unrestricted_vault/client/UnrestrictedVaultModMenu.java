package awa.Aether_254.create_unrestricted_vault.client;

import awa.Aether_254.create_unrestricted_vault.UnrestrictedVaultConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class UnrestrictedVaultModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return UnrestrictedVaultModMenu::createScreen;
    }

    private static Screen createScreen(Screen parent) {
        UnrestrictedVaultConfig.Data config = UnrestrictedVaultConfig.get();
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.literal("Create: Unrestricted Vault"));
        ConfigCategory limits = builder.getOrCreateCategory(Component.literal("Vault limits"));
        ConfigEntryBuilder entries = builder.entryBuilder();

        limits.addEntry(entries.startBooleanToggle(Component.literal("Enable mod"), config.enabled)
            .setDefaultValue(true)
            .setTooltip(Component.literal("When disabled, Create Fly's original width 3 and length 3x width limits are used."))
            .setSaveConsumer(value -> config.enabled = value)
            .build());
        limits.addEntry(entries.startBooleanToggle(Component.literal("Enable vertical vaults"), config.verticalVaultsEnabled)
            .setDefaultValue(true)
            .setTooltip(Component.literal("Sneak while placing a Vault against a top or bottom face to create a vertical Vault."))
            .setSaveConsumer(value -> config.verticalVaultsEnabled = value)
            .build());
        limits.addEntry(entries.startIntField(Component.literal("Maximum vault width"), config.maxWidth)
            .setDefaultValue(16)
            .setMin(1)
            .setMax(1024)
            .setTooltip(Component.literal("Maximum square cross-section width. Create Fly's original value is 3."))
            .setSaveConsumer(value -> config.maxWidth = value)
            .build());
        limits.addEntry(entries.startIntField(Component.literal("Maximum length multiplier"), config.maxLengthMultiplier)
            .setDefaultValue(16)
            .setMin(1)
            .setMax(4096)
            .setTooltip(Component.literal("Maximum length equals formed width times this value. Create Fly's original multiplier is 3."))
            .setSaveConsumer(value -> config.maxLengthMultiplier = value)
            .build());

        builder.setSavingRunnable(UnrestrictedVaultConfig::save);
        return builder.build();
    }
}
