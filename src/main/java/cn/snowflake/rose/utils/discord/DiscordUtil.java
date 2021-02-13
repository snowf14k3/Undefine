package cn.snowflake.rose.utils.discord;

//import cn.snowflake.rose.utils.MessageListener;
//import net.dv8tion.jda.api.JDA;
//import net.dv8tion.jda.api.JDABuilder;
//import net.dv8tion.jda.api.entities.Activity;
//import net.dv8tion.jda.api.entities.TextChannel;
//import net.dv8tion.jda.api.utils.Compression;
//import net.dv8tion.jda.api.utils.cache.CacheFlag;
//
//import javax.security.auth.login.LoginException;

public class DiscordUtil {
//    public JDA discordBot;
//    public MessageListener msgListener;
//
//    public DiscordUtil(String token, Activity ac, String Channel) {
//        JDABuilder builder = JDABuilder.createDefault(token);
//        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
//        builder.setBulkDeleteSplittingEnabled(false);
//        builder.setCompression(Compression.NONE);
//        builder.setActivity(ac);
//        try {
//            this.discordBot = builder.build();
//        } catch (LoginException e) {
//            e.printStackTrace();
//        }
//        if (this.discordBot != null) {
//            msgListener = new MessageListener(Channel);
//            this.discordBot.addEventListener(msgListener);
//        }
//    }
//
//    public TextChannel getTextChannelByName(String channel_name) {
//        for (TextChannel c : discordBot.getTextChannels()) {
//            if (c.getName().equalsIgnoreCase(channel_name)) {
//                return c;
//            }
//        }
//        return null;
//    }
//
//    public void sendMessage(TextChannel channel, String msg) {
//        channel.sendMessage(msg).queue();
//    }
//
//    public JDA getDiscordBot() {
//        return discordBot;
//    }
//
//    public MessageListener getMsgListener() {
//        return msgListener;
//    }
}
