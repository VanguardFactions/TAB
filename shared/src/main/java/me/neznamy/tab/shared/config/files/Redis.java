package me.neznamy.tab.shared.config.files;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.neznamy.tab.shared.TAB;
import me.neznamy.tab.shared.config.file.ConfigurationFile;
import me.neznamy.tab.shared.config.file.YamlConfigurationFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

@Getter
public class Redis {

  private final String host;
  private final int port;
  private final String password;
  private final int timeout;

  private final ConfigurationFile config;

  public Redis() throws IOException {
    this.config = new YamlConfigurationFile(
        getClass().getClassLoader().getResourceAsStream("config/redis.yml"),
        new File(TAB.getInstance().getDataFolder(), "redis.yml")
    );
    this.host = config.getString("host", "localhost");
    this.port = config.getInt("port", 6379);
    this.password = config.getString("password", "");
    this.timeout = config.getInt("timeout", 1000);
  }
}
