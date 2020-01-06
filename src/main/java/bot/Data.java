package bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains arrays of data to be used by the bot.
 */
public class Data {
    public static List<String> searchKeys = new ArrayList<>(List.of(
        "checkra1n", "unc0ver", "jailbreak", "ios jb"
    ));
    
    public static List<String> blockedWords = new ArrayList<>(List.of(
        "roblox", "@roblox"
    ));

    public static List<String> blockedAccounts = new ArrayList<>(List.of(
        "badimo", "asimo3089", "badccvoid"
    ));
}