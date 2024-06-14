package com.minetrone.mlib.connection;

import com.minetrone.mlib.MLib;
import com.minetrone.mlib.connection.backend.ConnectionJsonObjectGet;
import com.minetrone.mlib.connection.backend.ConnectionIPGetter;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    @Getter @Setter
    private String pluginName;

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

    public void postConnection(JavaPlugin plugin, String patchID) {
        setPluginName(secretMaker(MLib.getPrefix()));

        MLib.getAdventureAPI().consoleMessage(secret("VmVyaWZ5aW5nIHRoZSA=" + this.pluginName + " dXNlci4uLg=="));

        String p = "%%__POLYMART__%%";
        String b = "%%__MCMARKET__%%";
        String s = "%%__SONGODA__%%";
        String m = "http://www.iumproject.com/app/api/v1/check.php?product="+ patchID +"&ip=";

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
            MLib.setConnectionAPI(new ConnectionAPI(user_id, user_name, inject_version, resource_id, download_token, nonce, download_agent, download_time));
        } else if (b.equalsIgnoreCase("true")) {
            MLib.setConnectionAPI(new ConnectionAPI(user_id, user_name, inject_version, resource_id, download_token, nonce, download_agent, download_time));
        } else if (s.equalsIgnoreCase("true")) {
            MLib.setConnectionAPI(new ConnectionAPI(user_id, user_name, inject_version, plugin_id, download_token, nonce, download_agent, download_time));
        } else {
            try {
                String localIPAddress = ConnectionIPGetter.getLocalIPAddress();
                if (localIPAddress == null) {
                    MLib.getAdventureAPI().consoleMessage((secret("SW5jb3JyZWN0IElQIEFEUkVTUw==")));
                    return;
                }
                JSONObject response = ConnectionJsonObjectGet.getObjects(m + localIPAddress);
                if (response.getBoolean("status"))
                    MLib.setConnectionAPI(new ConnectionAPI(user_id, response.getString("username"), inject_version, resource_id, download_token, nonce, download_agent, download_time));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getConnection(ConnectionAPI connection) {
        if (connection != null) {
            MLib.getAdventureAPI().consoleMessage(secret("WW91ciBsaWNlbnNlIGRvZXMgbm90IGV4aXN0LCB3aG8gYXJlIHlvdSAhPw=="));
            MLib.getInstance().getServer().getPluginManager().disablePlugin(MLib.getInstance());
            return;
        }
        MLib.getAdventureAPI().consoleMessage(secret("IA=="));
        MLib.getAdventureAPI().consoleMessage(secret("SGVsbG8=") + ": " + connection.getName());
        MLib.getAdventureAPI().consoleMessage(secret("VGhhbmtzIGZvciBCdXlpbmcgYW5kIFVzaW5nIA==" + this.getPluginName() + " LSBNaW5lVHJvbmU="));
        MLib.getAdventureAPI().consoleMessage(secret("IA=="));
    }

    private static String secret(String s) {
        return new String(Base64.getDecoder().decode(s));
    }

    private static String secretMaker(String s) {
        return new String(Base64.getEncoder().encode(s.getBytes()));
    }
}