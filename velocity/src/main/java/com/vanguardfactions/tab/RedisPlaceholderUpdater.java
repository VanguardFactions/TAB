package com.vanguardfactions.tab;

import me.neznamy.tab.shared.TAB;
import me.neznamy.tab.shared.TabConstants;
import me.neznamy.tab.shared.proxy.ProxyTabPlayer;
import me.neznamy.tab.shared.task.PluginMessageProcessTask;
import org.redisson.Redisson;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisPlaceholderUpdater {

  private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

  public static void initialize() {
    final var redis = TAB.getInstance().getConfiguration().getRedis();
    final var config = new Config();
    config.setExecutor(EXECUTOR);
    config.setCodec(new JsonJacksonCodec());
    config.useSingleServer()
        .setAddress("redis://" + redis.getHost() + ":" + redis.getPort())
        .setTimeout(redis.getTimeout())
        .setConnectTimeout(redis.getTimeout())
        .setConnectionPoolSize(64)
        .setConnectionMinimumIdleSize(24)
        .setPassword(redis.getPassword());

    final var client = Redisson.create(config);
    final var topic = client.getTopic(TabConstants.PLUGIN_MESSAGE_CHANNEL_NAME);

    topic.addListener(
        PlaceholderUpdateRequest.class,
        (channel, msg) -> {
          final var player = (ProxyTabPlayer) TAB.getInstance().getPlayer(msg.getPlayerId());
          if (player == null) return;
          for (final var update : msg.getUpdates()) {
            TAB.getInstance().getCpu().runMeasuredTask("Plugin message handling", TabConstants.CpuUsageCategory.PLUGIN_MESSAGE_PROCESS, new PluginMessageProcessTask(update, player));
          }
        }
    );
  }
}
