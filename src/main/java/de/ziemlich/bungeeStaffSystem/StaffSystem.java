package de.ziemlich.bungeeStaffSystem;

import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.plugin.Plugin;

public class StaffSystem extends Plugin {

    private static StaffSystem instance;

    public static StaffSystem getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        StaffSystemManager.ssm.loadStaffSystem();
    }



    @Override
    public void onDisable() {

    }
}
