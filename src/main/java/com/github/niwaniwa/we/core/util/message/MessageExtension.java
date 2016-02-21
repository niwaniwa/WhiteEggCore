package com.github.niwaniwa.we.core.util.message;

public interface MessageExtension {

    /**
     * 実行
     *
     * @param message ソース
     * @return 機能によって改変された文字列
     */
    public abstract String execute(String message);

}
