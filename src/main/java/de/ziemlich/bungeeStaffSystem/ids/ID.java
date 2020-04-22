package de.ziemlich.bungeeStaffSystem.ids;

import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.nio.charset.Charset;
import java.util.Random;

public class ID {

    private IDTypes idType;

    public ID(IDTypes idType) {
        this.idType = idType;
    }

    public String createID() {
        int STRING_LENGTH = 12;
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < STRING_LENGTH; i++) {
            sb.append((char) ((int) (Math.random() * 26) + 97));
        }

        if(StaffSystemManager.ssm.getMainSQL().countResult("reports", "id", sb) == 1) {
            createID();
        }

        return sb.toString();
    }

}
