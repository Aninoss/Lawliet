package MySQL.Modules.FisheryUsers;

import Constants.FishingCategoryInterface;
import Constants.Settings;
import Core.DiscordApiCollection;
import MySQL.Modules.Server.ServerBean;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import java.util.Optional;

public class FisheryUserPowerUpBean {

    private long serverId, userId;
    private int powerUpId, level;
    private long startPrice, effect;
    private boolean changed = false;

    public FisheryUserPowerUpBean(long serverId, long userId, int powerUpId, int level) {
        this.serverId = serverId;
        this.userId = userId;
        this.powerUpId = powerUpId;
        this.level = level;
        this.startPrice = FishingCategoryInterface.START_PRICE[powerUpId];
        this.effect = FishingCategoryInterface.EFFECT[powerUpId];
    }


    /* Getters */

    public long getServerId() {
        return serverId;
    }

    public Optional<Server> getServer() { return DiscordApiCollection.getInstance().getServerById(serverId); }

    public long getUserId() { return userId; }

    public Optional<User> getUser() { return getServer().flatMap(server -> server.getMemberById(userId)); }

    public int getPowerUpId() { return powerUpId; }

    public int getLevel() { return level; }

    public boolean checkChanged() {
        boolean changedTemp = changed;
        changed = false;
        return changedTemp;
    }


    /* Setters */

    void levelUp() {
        level++;
        changed = true;
    }


    /* Tools */

    public long getPrice() {
        return Math.round(Math.pow(getValue(level), 1.02) * startPrice);
    }

    public long getEffect() {
        return getValue(level) * effect;
    }

    public long getDeltaEffect() {
        return (getValue(level + 1) - getValue(level)) * effect;
    }

    public static long getValue(long level) {
        long value = 0;
        for(int i = 1; i <= level + 1; i++) {
            value += i;
        }

        return value;
    }

}
