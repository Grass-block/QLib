package org.atcraftmc.qlib.bukkit;

import org.atcraftmc.qlib.PluginConcept;
import org.atcraftmc.qlib.QLibContext;
import org.atcraftmc.qlib.bukkit.task.TaskManager;
import org.atcraftmc.qlib.config.ConfigContainer;
import org.atcraftmc.qlib.language.LanguageContainer;
import org.atcraftmc.qlib.language.MinecraftLocale;
import org.atcraftmc.qlib.platform.PluginPlatform;
import org.atcraftmc.qlib.text.pipe.LocaleHandler;
import org.atcraftmc.qlib.text.pipe.MessageHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class QLibBukkitContext extends QLibContext {
    private static QLibBukkitContext GLOBAL;

    private final BukkitPluginConcept plugin;
    private final LanguageContainer language;
    private final ConfigContainer config;
    private final BukkitAudienceService audienceService;
    private final TaskManager taskManager;

    public QLibBukkitContext(BukkitPluginConcept plugin) {
        this.plugin = plugin;
        this.audienceService = BukkitAudienceService.create(plugin, this.textEngine);
        this.language = new LanguageContainer(this, plugin, plugin.configId());
        this.config = new ConfigContainer();

        registerAPI();

        this.taskManager = TaskManager.getInstance(this.plugin);

        if (GLOBAL == null) {
            createGlobal();
        }
    }

    public static QLibBukkitContext global() {
        return GLOBAL;
    }

    private void createGlobal() {
        GLOBAL = this;
    }

    private void registerAPI() {
        this.textEngine.getMessagePipeline().addLast("qlib:bukkit", MessageHandler.wrap((audience, message, ctx) -> {
            var a = audience.getPointer(CommandSender.class);
            BukkitTextAPI.sendMessage(a, message);
        }));

        this.textEngine.getActionBarPipeline().addLast("qlib:bukkit", MessageHandler.wrap((audience, message, ctx) -> {
            if (audience.getPointer(CommandSender.class) instanceof Player p) {
                BukkitTextAPI.sendActionbar(p, message);
                return;
            }

            ctx.handle(audience, message);
        }));

        this.textEngine.getLocalePipeline().addLast("qlib:bukkit", LocaleHandler.wrap((audience, ctx) -> {
            if (audience.getPointer(CommandSender.class) instanceof Player p) {
                return MinecraftLocale.minecraft(p.getLocale());
            }

            return ctx.get(audience);
        }));
    }

    @Override
    public BukkitAudienceService audiences() {
        return this.audienceService;
    }

    @Override
    public LanguageContainer language() {
        return this.language;
    }

    @Override
    public ConfigContainer config() {
        return this.config;
    }

    @Override
    public PluginConcept plugin() {
        return this.plugin;
    }

    @Override
    public PluginPlatform platform() {
        return plugin().platform();
    }

    public TaskManager taskManager() {
        return this.taskManager;
    }

    @Override
    public void close() {
        this.audienceService.close();
        this.taskManager.cleanup();
        this.taskManager.runFinalizeTask();
    }
}
