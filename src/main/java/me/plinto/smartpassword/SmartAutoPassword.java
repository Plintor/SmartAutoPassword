package me.plinto.smartpassword;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.io.*;
import java.security.SecureRandom;
import java.util.*;

@Mod(modid = "smartpassword", name = "SmartPassword", version = "0.7")
public class SmartAutoPassword {
    private static Minecraft mc=Minecraft.getMinecraft();
    private static boolean enabled=false;
    private String plutilsdir="";
    private static String dir="";
    private String config_file="AutoPassword.on.top";// in file saves your passwords (nick:server:password)

    private static final String prefix =
            ChatFormatting.BLUE + ChatFormatting.BOLD.toString() + "["
                    + ChatFormatting.GOLD + ChatFormatting.BOLD + "SP"
                    + ChatFormatting.BLUE + ChatFormatting.BOLD + "]"
                    + ChatFormatting.WHITE + ChatFormatting.BOLD + " ";//[SP] (SmartPassword)
    private String my_password;// not public

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new SmartAutoPassword());
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            plutilsdir = "C:/PlintoUtilsCfg";
            dir = "C:/PlintoUtilsCfg/AutoPassword";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            plutilsdir = "/PlintoUtilsCfg";
            dir = "/PlintoUtilsCfg/AutoPassword";
        } else {
            plutilsdir = "C:/PlintoUtilsCfg";
            dir = "C:/PlintoUtilsCfg/AutoPassword";
        }
        createcfg();
        loadcfg();
    }

    @SubscribeEvent
    public void chat(ClientChatEvent ev) {
        String m = ev.getMessage();
        if (m.startsWith("-ap")) {
            ev.setCanceled(true);
            if (enabled) {
                enabled = false;
                updatecfg(false);
                mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix+"\u0410\u0432\u0442\u043e\u041f\u0430\u0440\u043e\u043b\u044c \u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d"));
            } else {
                enabled = true;
                updatecfg(true);
                mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix+"\u0410\u0432\u0442\u043e\u041f\u0430\u0440\u043e\u043b\u044c \u0432\u043a\u043b\u044e\u0447\u0435\u043d"));
            }
        }
        if (m.startsWith("/login ") || m.startsWith("/l ") || m.startsWith("/reg ") || m.startsWith("/register ")) {
            String[] splmess = m.split(" ");
            if (splmess.length == 2) {
                String origpass = splmess[1];
                if (checkisonserver()) {
                    if (!issaved(mc.getSession().getUsername(), mc.getCurrentServerData().serverIP.replace(":", "_"))) {
                        asksave(origpass);
                        my_password = origpass;
                    } else {
                        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + "\u041f\u0430\u0440\u043e\u043b\u044c \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442 \u0434\u043b\u044f \u044d\u0442\u043e\u0433\u043e \u0441\u0435\u0440\u0432\u0435\u0440\u0430"));}}}
        }

        if (m.startsWith("-spass")) {
            ev.setCanceled(true);
            if (checkisonserver()) {
                String password = get(mc.getSession().getUsername(), mc.getCurrentServerData().serverIP.replace(":", "_"));
                if (password != null) {
                    mc.player.sendChatMessage("/login "+decrypt(password));
                } else {
                    mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix+"\u041f\u0430\u0440\u043e\u043b\u044c \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d \u0434\u043b\u044f \u044d\u0442\u043e\u0433\u043e \u0441\u0435\u0440\u0432\u0435\u0440\u0430"));}}
        }

        if (m.startsWith("-sreg")) {
            ev.setCanceled(true);
            if (checkisonserver()) {
                String[] splmess = m.split(" ");
                int passlength = 22;
                if (splmess.length > 1) {
                    try {
                        passlength = Integer.parseInt(splmess[1]);
                    } catch (NumberFormatException ex) {
                        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + "\u041d\u0435\u0432\u0435\u0440\u043d\u044b\u0439 \u0430\u0440\u0433\u0443\u043c\u0435\u043d\u0442: " + splmess[1]));}}
                String charss = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_1234567890";
                StringBuilder randompass = new StringBuilder();
                SecureRandom random = new SecureRandom();
                randompass.append(mc.getSession().getUsername());
                for (int i = 0; i < passlength; i++) {
                    randompass.append(charss.charAt(random.nextInt(charss.length())));}
                my_password = randompass.toString();
                if (!issaved(mc.getSession().getUsername(), mc.getCurrentServerData().serverIP.replace(":", "_"))) {
                    mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix+" "+my_password));
                    mc.player.sendChatMessage("/register " + my_password + " " + my_password);
                    save(mc.getSession().getUsername(), mc.getCurrentServerData().serverIP.replace(":", "_"), encrypt(my_password));
                    mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + "\u041f\u0430\u0440\u043e\u043b\u044c \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d"));
                } else {
                    mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + "\u041f\u0430\u0440\u043e\u043b\u044c \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442"));}}
        }

        try {
            if (m.startsWith("-yes")) {
                ev.setCanceled(true);
                if (checkisonserver()) {
                    if (!issaved(mc.getSession().getUsername(), mc.getCurrentServerData().serverIP.replace(":", "_"))) {
                        save(mc.getSession().getUsername(), mc.getCurrentServerData().serverIP.replace(":", "_"), encrypt(my_password));
                        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + "\u041f\u0430\u0440\u043e\u043b\u044c \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d"));
                        my_password = "";
                    } else {
                        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + "\u041d\u0430\u043f\u043e\u043c\u0438\u043d\u0430\u043d\u0438\u0435: \u041f\u0430\u0440\u043e\u043b\u044c \u0443\u0436\u0435 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d!"));}}
            }
        } catch (NullPointerException e) {
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + "\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430, \u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0432\u044b \u043f\u044b\u0442\u0430\u0435\u0442\u0435\u0441\u044c \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043f\u0430\u0440\u043e\u043b\u044c \u043d\u0435 \u043d\u0430\u043f\u0438\u0441\u0430\u0432 /login <\u043f\u0430\u0440\u043e\u043b\u044c>"));
        }

        if (m.startsWith("-no")) {
            ev.setCanceled(true);
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + "\u041f\u0430\u0440\u043e\u043b\u044c \u043d\u0435 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d"));
            my_password = "";
        }
    }

    @SubscribeEvent
    public void RealChatEvent(ClientChatReceivedEvent e) {
        String m = e.getMessage().getUnformattedText();
        if (checkisonserver()) {
            if ((m.contains("/login ") || m.contains("/reg ") || m.contains("/register ") || m.contains("/l ")) &&
                    enabled && get(mc.getSession().getUsername(), mc.getCurrentServerData().serverIP.replace(":", "_")) != null) {
                String pass = get(mc.getSession().getUsername(), mc.getCurrentServerData().serverIP.replace(":", "_"));
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mc.player.sendChatMessage("/login "+decrypt(pass));
                    }
                }, 200);}}
    }

    private void asksave(String p) {
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043f\u0430\u0440\u043e\u043b\u044c? \u041d\u0430\u043f\u0438\u0448\u0438 '-yes' \u0434\u043b\u044f \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u044f \u0438\u043b\u0438 '-no' \u0434\u043b\u044f \u043e\u0442\u043c\u0435\u043d\u044b"));
    }

    private boolean issaved(String nick, String serv) {
        File file = new File(dir + "/" + config_file);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 3 && parts[0].equals(nick) && parts[1].equals(checkserver(serv))) {
                        return true;}}
            } catch (IOException e) {
                e.printStackTrace();}}
        return false;
    }


    private void save(String nick, String serv, String pass) {
        String aptext = nick + ":" + checkserver(serv) + ":" + pass;
        if (aptext.isEmpty()) {
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + "\u041d\u0443\u0436\u043d\u043e \u043f\u0440\u043e\u043f\u0438\u0441\u0430\u0442\u044c /login <\u0432\u0430\u0448 \u043f\u0430\u0440\u043e\u043b\u044c> \u0438 \u043f\u043e\u0442\u043e\u043c \u043d\u0430\u043f\u0438\u0441\u0430\u0442\u044c '-yes' (\u0431\u0435\u0437 ' \u0438 '), \u0434\u043b\u044f \u0442\u043e\u0433\u043e \u0447\u0442\u043e\u0431\u044b \u0435\u0433\u043e \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c"));
            return;}

        File pldir = new File(plutilsdir);
        if (!pldir.exists()) {
            pldir.mkdir();}

        File dirr = new File(dir);
        if (!dirr.exists()) {
            dirr.mkdir();}
        File file = new File(dirr, config_file);
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(aptext + "\n");
        } catch (IOException e) {
            e.printStackTrace();}
    }

    private String get(String nick, String serv) {
        File file = new File(dir + "/" + config_file);
        if (file.exists()) {
            try (BufferedReader read = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = read.readLine()) != null) {
                    String[] p = line.split(":");
                    if (p.length == 3 && p[0].equals(nick) && p[1].equals(checkserver(serv))) {
                        return p[2];}}
            } catch (IOException e) {
                mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix+"\u041d\u0435\u0442\u0443 \u043f\u0430\u0440\u043e\u043b\u044f \u0434\u043b\u044f \u044d\u0442\u043e\u0433\u043e \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u0430."));}}
        return null;
    }

    public static String checkserver(String serv) {// for 2b2t.org.ru and 6g6s :)
        String server = serv.toLowerCase();
        if (server.contains("2b2t.org.ru") ||
                server.contains("mc.2b2t.org.ru") ||
                server.contains("2b2t.org.ua") ||
                server.contains("join.2b2t.org.ru") ||
                server.contains("shield.2b2t.org.ru") ||
                server.contains("novosib.2b2t.org.ru") ||
                server.contains("pl.2b2t.org.ru") ||
                server.contains("staging.2b2t.org.ru")) {
            return "2bru";
        } else if (
                server.contains("6g6s.xyz") ||
                server.contains("eu.6g6s.xyz")) {
            return "6g6s";
        } else {
            return serv;}
    }

    private static boolean checkisonserver() {
        if (mc.getCurrentServerData() != null) {
            return true;}
        return false;
    }

    private void createcfg() {
        try {
            File cfg = new File(dir, "config.ap");
            if (!cfg.exists()) {
                cfg.getParentFile().mkdirs();
                cfg.createNewFile();
                try (FileWriter write = new FileWriter(cfg)) {
                    write.write("enabled:false");}}
        } catch (IOException e) {
            e.printStackTrace();}
    }

    private void loadcfg() {
        try {
            File cfg = new File(dir, "config.ap");
            if (cfg.exists()) {
                Properties proj = new Properties();
                try (FileReader reader = new FileReader(cfg)) {
                    proj.load(reader);
                    enabled = Boolean.parseBoolean(proj.getProperty("enabled", "false"));}}
        } catch (IOException e) {
            e.printStackTrace();}
    }

    private void updatecfg(boolean val) {
        try {
            File cfg = new File(dir, "config.ap");
            if (cfg.exists()) {
                Properties proj = new Properties();
                try (FileReader read = new FileReader(cfg)) {
                    proj.load(read);
                    proj.setProperty("enabled", String.valueOf(val));}
                try (FileWriter writer = new FileWriter(cfg)) {
                    proj.store(writer, null);}}
        } catch (IOException e) {
            e.printStackTrace();}
    }

    private static String encrypt(String t) {// ohh shit dont angry :(
        String hehehee = t
                .replace("A", "_;.......;.;;........_").replace("a", "_.;......;......;...._")
                .replace("B", "_;......;.....;..;..._").replace("b", "_....;............;._")
                .replace("C", "_..;..........;....._").replace("c", "_.;......;;.........._")
                .replace("D", "_......;...;;;......_").replace("d", "_................._")
                .replace("E", "_.......;.........._").replace("e", "_..;.....;.....;...._")
                .replace("F", "_.......;....;......_").replace("f", "_........;........._")
                .replace("G", "_...;..........;...._").replace("g", "_,,...;....;...._")
                .replace("H", "_.,,.;........._").replace("h", "_..,,.;.....;...._")
                .replace("I", "_...,,........._").replace("i", "_...,,..:......;_")
                .replace("J", "_;...:.,,........_").replace("j", "_.....,,..;....._")
                .replace("K", "_..:....,,......_").replace("k", "_.......,,....._")
                .replace("L", "_..:......,,...._").replace("l", "_...:......,,..._")
                .replace("M", "_....:....:..,,.._").replace("m", "_...:........,,._")
                .replace("N", "_...:.........,,_").replace("n", "_,,,...:........._")
                .replace("O", "_.,,,..........._").replace("o", "_..,,,.....:....._")
                .replace("P", "_...,:,,....:....._").replace("p", "_....,,,.......:._")
                .replace("Q", "_.....,,,......._").replace("q", "_..:....,,,......_")
                .replace("R", "_.......,,,....._").replace("r", "_....:....,,,...._")
                .replace("S", "_.........,,,.._").replace("s", "_..........,,,..._")
                .replace("T", "_.....::......,,,.._").replace("t", "_..........:..,,,._")
                .replace("U", "_..:...........,,,_").replace("u", "_.:...:..::......_")
                .replace("V", "_.:.....:...:...._").replace("v", "_.:..........:.._")
                .replace("W", "_....:........._").replace("w", "_.:....:..::..:...._")
                .replace("X", "_...:..::....:...._").replace("x", "_...:..::....:...:._")
                .replace("Y", "_..:.:....:......_").replace("y", "_.:...::...:....:.._")
                .replace("Z", "_...::..:...:....._").replace("z", "_..:.:.:::.....:...._");
        byte[] encript = Base64.getEncoder().encode(hehehee.getBytes());
        return new String(encript);
    }

    private static String decrypt(String t) {
        byte[] decript = Base64.getDecoder().decode(t.getBytes());
        String hehehe = new String(decript)
                .replace("_;.......;.;;........_", "A").replace("_.;......;......;...._", "a")
                .replace("_;......;.....;..;..._", "B").replace("_....;............;._", "b")
                .replace("_..;..........;....._", "C").replace("_.;......;;.........._", "c")
                .replace("_......;...;;;......_", "D").replace("_................._", "d")
                .replace("_.......;.........._", "E").replace("_..;.....;.....;...._", "e")
                .replace("_.......;....;......_", "F").replace("_........;........._", "f")
                .replace("_...;..........;...._", "G").replace("_,,...;....;...._", "g")
                .replace("_.,,.;........._", "H").replace("_..,,.;.....;...._", "h")
                .replace("_...,,........._", "I").replace("_...,,..:......;_", "i")
                .replace("_;...:.,,........_", "J").replace("_.....,,..;....._", "j")
                .replace("_..:....,,......_", "K").replace("_.......,,....._", "k")
                .replace("_..:......,,...._", "L").replace("_...:......,,..._", "l")
                .replace("_....:....:..,,.._", "M").replace("_...:........,,._", "m")
                .replace("_...:.........,,_", "N").replace("_,,,...:........._", "n")
                .replace("_.,,,..........._", "O").replace("_..,,,.....:....._", "o")
                .replace("_...,:,,....:....._", "P").replace("_....,,,.......:._", "p")
                .replace("_.....,,,......._", "Q").replace("_..:....,,,......_", "q")
                .replace("_.......,,,....._", "R").replace("_....:....,,,...._", "r")
                .replace("_.........,,,.._", "S").replace("_..........,,,..._", "s")
                .replace("_.....::......,,,.._", "T").replace("_..........:..,,,._", "t")
                .replace("_..:...........,,,_", "U").replace("_.:...:..::......_", "u")
                .replace("_.:.....:...:...._", "V").replace("_.:..........:.._", "v")
                .replace("_....:........._", "W").replace("_.:....:..::..:...._", "w")
                .replace("_...:..::....:...._", "X").replace("_...:..::....:...:._", "x")
                .replace("_..:.:....:......_", "Y").replace("_.:...::...:....:.._", "y")
                .replace("_...::..:...:....._", "Z").replace("_..:.:.:::.....:...._", "z");
        return hehehe;
    }
}