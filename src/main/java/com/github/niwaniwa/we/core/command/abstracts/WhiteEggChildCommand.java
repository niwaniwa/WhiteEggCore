package com.github.niwaniwa.we.core.command.abstracts;

public abstract class WhiteEggChildCommand extends AbstractWhiteEggCoreCommand {

	/**
	 * 親コマンド
	 * @return String
	 */
	public abstract String getParentCommand();

}
