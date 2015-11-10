package com.github.niwaniwa.we.core.api;

public class Callback {

	/**
	 * Twitter関連クラスで呼び出されるコールバックメソッド
	 * @param twitter 成功したか
	 */
	public void onTwitter(Boolean twitter){ throw new UnsupportedOperationException(getClass().getSimpleName()); }

	/**
	 * その他で呼び出されるコールバックメソッド
	 * @param obj Object
	 */
	public void call(Object... obj){ throw new UnsupportedOperationException(getClass().getSimpleName()); }

}
