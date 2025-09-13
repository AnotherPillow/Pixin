package com.anotherpillow.pixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    private static final String FILE_NAME = "pixin.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path fp;
    private JsonObject configData;

    public Config() {
        this.fp = FabricLoader.getInstance()
                .getConfigDir()
                .resolve(FILE_NAME);

        if (!Files.exists(fp)) {
            createDefaultConfig();
        }

        loadConfig();
    }

    private void createDefaultConfig() {
        JsonObject defaultConfig = new JsonObject();
        defaultConfig.addProperty("url", "wss://connect.pixie.rip/v1");
        try {
            Files.createDirectories(fp.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(fp, StandardCharsets.UTF_8)) {
                GSON.toJson(defaultConfig, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create default config at " + fp, e);
        }
    }

    private void loadConfig() {
        try (BufferedReader reader = Files.newBufferedReader(fp, StandardCharsets.UTF_8)) {
            configData = GSON.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config from " + fp, e);
        }
    }

    public String getUrl() {
        return configData.get("url").getAsString();
    }

    public void setUrl(String url) {
        configData.addProperty("url", url);
        saveConfig();
    }

    private void saveConfig() {
        try (BufferedWriter writer = Files.newBufferedWriter(fp, StandardCharsets.UTF_8)) {
            GSON.toJson(configData, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config to " + fp, e);
        }
    }
}
