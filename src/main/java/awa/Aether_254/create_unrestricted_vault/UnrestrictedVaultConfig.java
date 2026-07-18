package awa.Aether_254.create_unrestricted_vault;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class UnrestrictedVaultConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("create_unrestricted_vault.json");

    private static Data data = new Data();

    private UnrestrictedVaultConfig() {
    }

    public static Data get() {
        return data;
    }

    public static void load() {
        try {
            if (Files.isRegularFile(PATH)) {
                Data loaded = GSON.fromJson(Files.readString(PATH), Data.class);
                data = loaded == null ? new Data() : loaded;
            }
        } catch (IOException | RuntimeException ignored) {
            data = new Data();
        }
        sanitize();
        save();
    }

    public static void save() {
        sanitize();
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(data));
        } catch (IOException ignored) {
        }
    }

    private static void sanitize() {
        data.maxWidth = Math.max(1, Math.min(data.maxWidth, 1024));
        data.maxLengthMultiplier = Math.max(1, Math.min(data.maxLengthMultiplier, 4096));
    }

    public static int maxLength(int width) {
        long length = (long) width * data.maxLengthMultiplier;
        return (int) Math.min(length, Integer.MAX_VALUE);
    }

    public static final class Data {
        public boolean enabled = true;
        public boolean verticalVaultsEnabled = true;
        public int maxWidth = 16;
        public int maxLengthMultiplier = 16;
    }
}
