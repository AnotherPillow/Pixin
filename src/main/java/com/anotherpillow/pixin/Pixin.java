package com.anotherpillow.pixin;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.impl.util.LoaderUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Objects;

public class Pixin implements ModInitializer {

    public void copyEssentialLoader() {
        final FabricLoader fabricLoader = FabricLoader.getInstance();
        Collection<ModContainer> mods = fabricLoader.getAllMods();
        mods.forEach(modContainer -> {
            ModMetadata meta = modContainer.getMetadata();
            if (Objects.equals(meta.getId(), "essential-container")) { // container is the one you download from modrinth, etc
                modContainer.getRootPaths().forEach(path -> {
                    try {
                        Files.list(path).forEach( filepath -> {
                            String filename = filepath.toString().replaceFirst("/", "");
                            if (filename.endsWith(".jar") && filename.startsWith("essential-") && !filename.startsWith("essential-loader")) {
                                System.out.println("[Pixin] Found essential loader " + filename);
                                try {
                                    Files.copy(filepath, Paths.get(fabricLoader.getGameDir().toString(), "mods", "essential-loader-pixin-extracted.jar"), StandardCopyOption.REPLACE_EXISTING);
                                    System.out.println("[Pixin] Exiting game to install Essential.");
                                    for (int i =0; i < 15; i++) {
                                        System.out.println("[Pixin] THIS IS NOT A CRASH.");
                                    }
                                    System.out.println("[Pixin] Reopen your game as usual.");
                                    throw new Error("");

                                } catch (IOException e) {
                                    System.out.println("[Pixin] IOException occured copying out Essential jar, will ignore. " + e.getMessage());
                                }
                            }
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

    @Override
    public void onInitialize() {
        this.copyEssentialLoader();

    }
}
