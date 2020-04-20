package de.ziemlich.bungeeStaffSystem.mySql;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class SQLConfig {

    private String path;
    private String file;

    public SQLConfig(String path, String file) {
        this.path = path;
        this.file = file;
        this.setStandard();
    }

    public void setStandard() {
        if (!new File(path).exists()) {
            new File(path).mkdir();
        }
        if (!getFile().exists()) {
            try {
                getFile().createNewFile();
                Configuration cfg = this.getConfiguration();
                cfg.set("host", (Object)"localhost");
                cfg.set("port", (Object)"3306");
                cfg.set("database", (Object)"database");
                cfg.set("username", (Object)"username");
                cfg.set("password", (Object)"password");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, this.getFile());
                System.out.println("   ");
                System.out.println("Es muss erst eine mysql-Config erstellt werden!");
                System.out.println("     ");
                ProxyServer.getInstance().stop();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File getFile() {
        return new File(this.path, this.file);
    }

    private Configuration getConfiguration() {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.getFile());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, String> readData() {

        Configuration cfg = getConfiguration();

        HashMap<String, String> config = new HashMap<>();
        config.put("host", cfg.getString("host"));
        config.put("port", cfg.getString("port"));
        config.put("database", cfg.getString("database"));
        config.put("username", cfg.getString("username"));
        config.put("password", cfg.getString("password"));

        return config;
    }
}
