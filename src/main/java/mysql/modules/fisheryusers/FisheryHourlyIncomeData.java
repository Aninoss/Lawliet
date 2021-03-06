package mysql.modules.fisheryusers;

import java.time.Instant;
import constants.Settings;
import core.assets.MemberAsset;

public class FisheryHourlyIncomeData implements MemberAsset {

    private final long timeEpochSecond;
    private long fishIncome;
    private boolean changed = false;
    private final long guildId;
    private final long memberId;

    public FisheryHourlyIncomeData(long guildId, long memberId, Instant time, long fishIncome) {
        this.guildId = guildId;
        this.memberId = memberId;
        this.timeEpochSecond = time.getEpochSecond();
        this.fishIncome = fishIncome;
    }

    @Override
    public long getGuildId() {
        return guildId;
    }

    @Override
    public long getMemberId() {
        return memberId;
    }

    public Instant getTime() {
        return Instant.ofEpochSecond(timeEpochSecond);
    }

    public long getFishIncome() {
        return fishIncome;
    }

    public boolean checkChanged() {
        boolean changedTemp = changed;
        changed = false;
        return changedTemp;
    }

    void add(long fish) {
        if (fish > 0) {
            this.fishIncome = Math.min(Settings.FISHERY_MAX, this.fishIncome + fish);
            changed = true;
        }
    }

    public void setChanged() {
        changed = true;
    }

}