package cn.snowflake.rose.utils.discord;



import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

public class Discord {
    private static String channel = "verify";
    private static JDA discordBot;
    private static String token = "ODA3OTY4ODg3ODIzMTM4ODI2.YB_tqg.4KCQpySMojGwFrGwZRDUybghisA";
    private static DiscordUtil discordUtils;

    public static void init() {
        discordUtils = new DiscordUtil(token, Activity.playing(channel), channel);
        discordBot = discordUtils.getDiscordBot();
    }

    public static void setChannel(String channel) {
        Discord.channel = channel;
    }

    public static void setToken(String token) {
        Discord.token = token;
    }

    public static String getChannel() {
        return channel;
    }

    public static String getToken() {
        return token;
    }

    public static DiscordUtil getDiscordUtil() {
        return discordUtils;
    }

    public static JDA getBot() {
        return discordBot;
    }

    public static void sendMessage(String message) {
        discordUtils.sendMessage(Discord.getDiscordUtil().getTextChannelByName(channel), message);
    }

    public static Boolean getState() {
        return Discord.getBot() != null;
    }

    public static void shutdown() {
        if(Discord.getBot() != null) {
            Discord.getBot().shutdownNow();
            Discord.discordBot = null;
        }
    }
}
