package com.minetrone.mlib.connection;

import com.minetrone.mlib.adventure.AdventureAPI;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Base64;

public class ConnectionAPI {

    @Getter
    private final String id;

    @Getter
    private final String name;

    @Getter
    private final String version;

    @Getter
    private final String resource;

    @Getter
    private final String token;

    @Getter
    private final String nonce;

    @Getter
    private final String agent;

    @Getter
    private final String time;

    public ConnectionAPI() {
        this.id = "%%__USER__%%";
        this.name = "";
        this.version = "";
        this.resource = "";
        this.token = "";
        this.nonce = "";
        this.agent = "";
        this.time = "";
    }

    public ConnectionAPI(String id, String name, String version, String resource, String token, String nonce, String agent, String time) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.resource = resource;
        this.token = token;
        this.nonce = nonce;
        this.agent = agent;
        this.time = time;
    }

    /**
    public static void applyPatch(JavaPlugin plugin, String patchID, String patch1) {
        AdventureAPI adventureAPI = new AdventureAPI(plugin);
        adventureAPI.consoleMessage(decode(patch1));

        String p = "%%__POLYMART__%%";
        String b = "%%__MCMARKET__%%";
        String s = "%%__SONGODA__%%";
        String m = "https://www.minetrone.com/app/api/license.php?product="+ patchID +"&ip=";

        String user_id = "%%__USER__%%";
        String user_name = "%%__USERNAME__%%";

        String inject_version = p.equalsIgnoreCase("1") ? "%%__INJECT_VER__%%" : "%%__VERSION__%%";

        String resource_id = "%%__RESOURCE__%%";
        String plugin_id = "%%__PLUGIN__%%";

        String download_token = "%%__VERIFY_TOKEN__%%";
        String download_agent = "%%__AGENT__%%";
        String download_time = "%%__TIMESTAMP__%%";

        String nonce = "%%__NONCE__%%";


        if (p.equalsIgnoreCase("1")) {
            Main.getInstance().setFurnitureManager(new ConnectionAPI(user_id, user_name, inject_version, resource_id, download_token, nonce, download_agent, download_time));
        } else if (b.equalsIgnoreCase("true")) {
            Main.getInstance().setFurnitureManager(new ConnectionAPI(user_id, user_name, inject_version, resource_id, download_token, nonce, download_agent, download_time));
        } else if (s.equalsIgnoreCase("true")) {
            Main.getInstance().setFurnitureManager(new ConnectionAPI(user_id, user_name, inject_version, plugin_id, download_token, nonce, download_agent, download_time));
        }
    }*/

    private static String decode(String s) {
        return new String(Base64.getDecoder().decode(s));
    }
}