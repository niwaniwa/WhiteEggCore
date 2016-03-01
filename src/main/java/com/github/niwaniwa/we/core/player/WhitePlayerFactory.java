
package com.github.niwaniwa.we.core.player;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.database.DataBase;
import com.github.niwaniwa.we.core.database.mongodb.MongoDataBaseCollection;
import com.github.niwaniwa.we.core.database.mongodb.MongoDataBaseManager;
import com.github.niwaniwa.we.core.json.JsonManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * WhitePlayerのfactoryクラス
 *
 * @author niwaniwa
 */
public class WhitePlayerFactory {

    private WhitePlayerFactory() {
    }

    private static final List<WhitePlayer> players = new ArrayList<>();
    private static boolean isLock = WhiteEggCore.isLock;
    private static boolean enable = WhiteEggCore.getConf().getConfig().getBoolean("setting.player.enable", true);

    /**
     * プレイヤーのインスタンスを返します
     *
     * @param player プレイヤー
     * @return プレイヤー
     */
    public static WhitePlayer getInstance(Player player) {
        if (isLock || !enable) {
            throw new IllegalStateException("Cannot use getInstance for data load.");
        }
        if (player == null) {
            throw new NullPointerException("player is null");
        }
        WhiteEggPlayer white = new WhiteEggPlayer(player);
        white.loadTask();
        players.add(white);
        return white;
    }

    /**
     * instance
     *
     * @param <T>    WhitePlayerを継承したクラス
     * @param clazz  取得するクラス
     * @param player プレイヤー
     * @return T 指定したクラスのinstance
     */
    public static <T extends WhitePlayer> T getInstance(Player player, Class<T> clazz) {
        if (player == null) {
            throw new NullPointerException("player is null");
        }
        if (clazz.isInterface()) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " is Interface");
        }
        if (Modifier.isAbstract(clazz.getModifiers())) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " is Abstract class");
        }
        T instance = null;
        try {
            Constructor<T> c = clazz.getConstructor(Player.class);
            instance = c.newInstance(player);
        } catch (Exception e) {
        }
        if (instance == null) {
            return null;
        }
        if (!isLock && enable) {
            instance.saveVariable(new Gson().toJson(getInstance(player).serialize()));
        } else {
            instance.load();
        }
        return instance;
    }


    /**
     * 型を指定したクラスへ変換する
     *
     * @param <T>  WhitePlayerを継承したクラス
     * @param from 変換前のinstance
     * @param to   変換後のクラス
     * @return 指定したクラスのinstance(データ引き継ぎ)
     */
    public static <T extends WhitePlayer> T cast(WhitePlayer from, Class<T> to) {
        if (Modifier.isAbstract(to.getModifiers()) || from.getClass().getSimpleName().equals(to.getSimpleName())) {
            throw new IllegalArgumentException(to.getSimpleName() + " is Abstract class or Same as '" + to.getSimpleName() + "'");
        }
        from.save();
        T instance = null;
        try {
            instance = (T) to.getConstructor(Player.class).newInstance(from.getPlayer());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        instance.saveVariable(new Gson().toJson(from.serialize()));
        return instance;
    }

    /**
     * プレイヤーデータの保存
     */
    public static void saveAll() {
        WhitePlayerFactory.getPlayers().forEach(p -> p.save());
    }

    /**
     * プレイヤーデータの読み込み
     */
    public static void load() {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                WhiteEggAPI.getOnlinePlayers().forEach(p -> p.reload());
            }
        }.runTaskAsynchronously(WhiteEggCore.getInstance());
    }

    /**
     * サーバー起動時から取得時までに生成されたプレイヤーのインスタンスの取得
     *
     * @return instance
     */
    public static List<WhitePlayer> getPlayers() {
        return players;
    }

    /**
     * プレイヤーのデータを取得します
     *
     * @param uuid  uuid
     * @param clazz クラス
     * @return json
     */
    public static <T extends WhitePlayer> String getPlayerData(String uuid, Class<T> clazz) {
        if (WhiteEggAPI.useDataBase()) {
            try {
                DataBase database = WhiteEggCore.getDataBase();
                if (database.getName().equalsIgnoreCase("mongodb")) { // MongoDB
                    MongoDataBaseManager mongo = (MongoDataBaseManager) database;
                    MongoDataBaseCollection collection = new MongoDataBaseCollection(mongo, mongo.getDatabase("WhiteEgg"), "player");
                    Object object = collection.get(new Document("uuid", uuid), clazz.getSimpleName());
                    if (object != null) {
                        return ((Document) object).toJson();
                    }
                }
                // mysql
            } catch (Exception ex) {
            }
            return new String();
        }
        // local
        File path = new File(WhiteEggCore.getConf().getConfig().getString("setting.player.savePlayerData", WhiteEggCore.getInstance().getDataFolder() + File.separator + "players" + File.separator));
        JsonObject json = new JsonManager().getJson(new File(path + File.separator + uuid + ".json"));
        if (json == null) {
            return new String();
        }
        return json.toString();
    }

}
