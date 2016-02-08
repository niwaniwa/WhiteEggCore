package com.github.niwaniwa.we.core.util.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.niwaniwa.we.core.command.abs.ConsoleCancellable;
import com.github.niwaniwa.we.core.command.abs.WhiteBaseCommandExeutor;
import com.github.niwaniwa.we.core.player.commad.WhiteConsoleSender;

/**
 * コマンド登録関係クラス
 * @author niwaniwa
 *
 */
public class CommandFactory {

	private final Plugin pluginInstance;
	private String commandName;
	private PluginCommand commandInstance;

	/**
	 * コンストラクター
	 * @param pluginInstance 登録するプラグイン
	 * @param commandName コマンド名
	 */
	public CommandFactory(Plugin pluginInstance, String commandName) {
		this.pluginInstance = pluginInstance;
		this.commandName = commandName;
		this.commandInstance = createInstance();
	}

	/**
	 * プラグインのインスタンスの取得
	 * @return Plugin プラグイン
	 */
	public Plugin getPluginInstance() {
		return pluginInstance;
	}

	private PluginCommand createInstance() {
		if(pluginInstance == null ){ throw new IllegalArgumentException("instance is null."); }
		if(commandName == null || commandName.isEmpty()){ throw new IllegalArgumentException("command is null."); }
		PluginCommand commandInstance = null;
		try{
			Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			constructor.setAccessible(true);
			commandInstance = constructor.newInstance(commandName, pluginInstance);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return commandInstance;
	}

	/**
	 * Aliasの設定
	 * @param aliases aliases
	 */
	public void setAliases(List<String> aliases){
		commandInstance.setAliases(aliases);
	}

	/**
	 * Aliasの設定
	 * @param aliases aliases
	 */
	public void setAliases(String[] aliases){
		this.setAliases(Arrays.asList(aliases));
	}

	/**
	 * コマンドの説明文を設定
	 * @param description 説明文
	 */
	public void setDescription(String description){
		commandInstance.setDescription(description);
	}

	/**
	 * コマンドを実行するインスタンスの設定
	 * @param executor CommandExecutorを継承しているクラスのインスタンス
	 */
	public void setExecutor(CommandExecutor executor){
		commandInstance.setExecutor(executor);
	}

	/**
	 * ラベルの設定
	 * @param label label
	 */
	public void setLabel(String label){
		commandInstance.setLabel(label);
	}

	/**
	 * コマンド名の設定
	 * @param commandName コマンド名
	 */
	public void setCommandName(String commandName){
		commandInstance.setName(commandName);
	}

	/**
	 * 権限の設定
	 * @param permission
	 */
	public void setPermission(String permission){
		commandInstance.setPermission(permission);
	}

	/**
	 * 権限を持たない場合に送信するエラーメッセージの設定
	 * @param permissionMessage メッセージ
	 */
	public void setPermissionMessage(String permissionMessage){
		commandInstance.setPermissionMessage(permissionMessage);
	}

	/**
	 * タブ補完を設定
	 * @param completer TabCompleterを実装したクラスのインスタンス
	 */
	public void setTabCompleter(TabCompleter completer){
		commandInstance.setTabCompleter(completer);
	}

	/**
	 * 使用方法の設定
	 * @param usage メッセージ
	 */
	public void setUsage(String usage){
		commandInstance.setUsage(usage);
	}

	/**
	 * コマンドの登録
	 * @param prefix prefix
	 */
	public void register(String prefix){
		registerCommand(prefix, commandInstance);
	}

	/**
	 * コマンドの登録 {@link #register(String)}
	 * prefixはプラグインの物が使用されます
	 */
	public void register(){
		this.register(pluginInstance.getDescription().getPrefix());
	}

	/**
	 * PluginCommandのインスタンスの取得
	 * @return PluginCommand instance
	 */
	public PluginCommand getCommandInstance(){ return commandInstance; }

	/**
	 * consoleをキャンセルするクラスか判定
	 * @param command 実行クラス
	 * @return boolean キャンセルする場合true、しない場合はfalse
	 */
	public static boolean isConsoleCancel(final WhiteBaseCommandExeutor command){
		Class<?>[] clazz = command.getClass().getInterfaces();
		if(clazz.length != 0){
			for(Class<?> s : clazz){
				if(s.equals(ConsoleCancellable.class)){ return true; }
			}
		}
		return false;
	}

	/**
	 * 実行者がconsoleか判定
	 * @param sender 実行者
	 * @return  boolean consoleの場合true、他はfalse
	 */
	public static boolean isConsole(Object sender){
		return (!(sender instanceof Player) || sender instanceof WhiteConsoleSender);
	}

	/**
	 * コマンドの登録
	 * @param prefix prefix
	 * @param instance PluginCommand
	 */
	public static void registerCommand(String prefix, PluginCommand instance){
//		((CraftServer) Bukkit.getServer()).getCommandMap().register(prefix, instance);
		try {
			Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			SimpleCommandMap map = SimpleCommandMap.class.cast(field.get(Bukkit.getServer()));
			map.register(prefix, instance);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * コマンド登録メソッド
	 * @param pluginInstance プラグイン
	 * @param prefix prefix
	 * @param commandName コマンド名
	 * @param aliases コマンドの他の名前
	 * @param using コマンドの使用方法
	 * @param description コマンドの説明文
	 * @param permission コマンドの権限
	 * @param commandExecutor コマンドの実行クラスのインスタンス
	 * @param tabInstance タブ補間のインスタンス
	 */
	public static void newCommand(Plugin pluginInstance, String prefix, String commandName, List<String> aliases, String using, String description, String permission, CommandExecutor commandExecutor, TabCompleter tabInstance){
		CommandFactory factory = new CommandFactory(pluginInstance, commandName);
		factory.setAliases((aliases == null ? new ArrayList<>(0) : aliases));
		factory.setDescription((description == null ? new String() : description));
		factory.setUsage((using == null ? new String() : using));
		factory.setPermission((permission == null ? new String() : permission));
		if(commandExecutor != null){ factory.setExecutor(commandExecutor); }
		if(tabInstance != null){ factory.setTabCompleter(tabInstance); }
	}

}
