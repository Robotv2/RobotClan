package fr.robotv2.robotclan;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import fr.robotv2.robotclan.command.ClanCommand;
import fr.robotv2.robotclan.data.OrmData;
import fr.robotv2.robotclan.listeners.*;
import fr.robotv2.robotclan.manager.ClaimManager;
import fr.robotv2.robotclan.manager.ClanManager;
import fr.robotv2.robotclan.objects.Claim;
import fr.robotv2.robotclan.objects.Clan;
import fr.robotv2.robotclan.util.FileUtil;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.util.UUID;

public final class RobotClan extends JavaPlugin {

    private static RobotClan instance;

    private final ClaimManager claimManager = new ClaimManager();
    private final ClanManager clanManager = new ClanManager();

    private OrmData<Claim, UUID> claimData;
    private OrmData<Clan, UUID> clanData;

    public static RobotClan get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.setupCommands();
        this.setupListeners();
        this.setupDataManager();
    }

    @Override
    public void onDisable() {
        getClanManager().getClans().forEach(getClanData()::save);
        getClaimManager().getClaims().stream().filter(Claim::isValid).forEach(getClaimData()::save);
        getClaimData().closeConnection();
        instance = null;
    }

    // <<- GETTERS ->>

    public ClanManager getClanManager() {
        return this.clanManager;
    }

    public OrmData<Clan, UUID> getClanData() {
        return this.clanData;
    }

    public ClaimManager getClaimManager() {
        return this.claimManager;
    }

    public OrmData<Claim, UUID> getClaimData() {
        return this.claimData;
    }

    // <<- SETUP ->>

    private void setupCommands() {
        final BukkitCommandHandler handler = BukkitCommandHandler.create(this);
        handler.registerDependency(ClanManager.class, this.clanManager);
        handler.registerDependency(ClaimManager.class, this.claimManager);
        handler.register(new ClanCommand());
    }

    private void setupListeners() {
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockListeners(this), this);
        pm.registerEvents(new EntityListeners(this), this);
        pm.registerEvents(new InteractListeners(this), this);
        pm.registerEvents(new ItemListeners(this), this);
        pm.registerEvents(new ClaimBoundariesListeners(this), this);
    }

    private void setupDataManager() {
        try {

            final File file = FileUtil.createFile(getDataFolder().getPath(), "database.db");
            final String databaseURL = "jdbc:sqlite:".concat(file.getPath());
            final ConnectionSource connectionSource = new JdbcConnectionSource(databaseURL);

            this.clanData = new OrmData<>();
            this.clanData.initialize(connectionSource, Clan.class);
            getClanData().getValues().forEach(this.clanManager::registerClan);

            this.claimData = new OrmData<>();
            this.claimData.initialize(connectionSource, Claim.class);
            getClaimData().getValues().forEach(this.claimManager::registerClaim);

        } catch (Exception e) {
            e.printStackTrace();
            getLogger().warning("Couldn't connect to the database. Shutting down the plugin.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }
}
