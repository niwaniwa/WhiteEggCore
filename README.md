# WhiteEggCore - v2.3

このプラグインは[WhiteBird](https://github.com/niwaniwa/WhiteBirdPvP "WhiteBirdPvP")系統の後継版として開発されたBukkit用Pluginです。

## 動作
- Java 8の機能を利用しています
- Spigot最新版にて動作を確認しています
- 不具合などについてはその他に記載されておりますTwitterまでお問い合わせください

## 主な機能
* jsonによるプレイデータの保存
* Twitterへのツイート、情報を取得
* Tab、Click可能なテキストを表示できる機能など

## 説明

### コマンド
コマンド | 説明 | 権限 | 備考
-------|------|------|-----
`/WhiteEggCore` | プラグイン情報など | whiteegg.core.command.* |  
`/toggle` | 各種設定を確認、設定できます| whiteegg.core.command.toggle |
`/head <Player名>` | プレイヤーの頭を取得します | whiteegg.core.command.toggle |
`/twitter <呟き>` | Twitterに呟きを送信します | whiteegg.core.command.twitter | 初実行時は/registerを行ってください
`/register` | Twitterに接続するための登録を行います | whiteegg.core.command.twitter.register |
`/whisper` <Player> <メッセージ> | 特定の相手にプライベートメッセージを送信します | whiteegg.core.command.whisper |
`/replay` <メッセージ> | 返信します | whiteegg.core.command.replay |

### Config.yml

要素名 | 説明 | type | 初期値
------|------|------|-----
lock | プラグインのロック | boolean | false
consumerkey | Twitter app | String | null
consumerSecret | Twitter app | String | null

### Script
JavaScriptを利用してゲーム内に干渉できます

[詳細はこちらをご覧ください](https://github.com/niwaniwa/WhiteEggCore/wiki/Script "Script")

### データベースについて

現在MongoDB(不具合あり)、~~MySQL~~(現在未定)に対応しております

## Javadoc

 Javadocは[こちら](http://niwaniwa.github.io/javadoc/whiteeggcore/)

## Library

* [Twitter4j](http://twitter4j.org/ "Twitter4j")

* [MongoDB](https://www.mongodb.org/ "MongoDB")

* ~~[MySQL](https://www.mysql.com/ "MySQL")~~ __現在使用されていません__

## license

* [MIT License](https://github.com/niwaniwa/WhiteEggCore/blob/master/License.txt "License")

* 本プラグインはTwitter4j、MongoDB driverを含んでおります(Apache License 2.0を使用しています)

## その他

### プレイヤーについて
#### データ
- プレイヤーのデータは`plugins/WhiteEggCore/players/<uuid>.json`に出力されます
- プレイヤーデータは[WhitePlayer](http://niwaniwa.github.io/javadoc/whiteeggcore/com/github/niwaniwa/we/core/player/WhitePlayer.html)から取得することができます
- 取得は[WhitePlayerFactory](http://niwaniwa.github.io/javadoc/whiteeggcore/com/github/niwaniwa/we/core/player/WhitePlayerFactory.html)クラスを使用してください

### APIについて

このプラグインはAPIを利用することが可能です

詳しくは[Javadoc](http://niwaniwa.github.io/javadoc/whiteeggcore/com/github/niwaniwa/we/core/api/WhiteEggAPI.html)をご覧ください

### Eventについて

現在3つのイベントを使用することができます

- [WhiteEggPreTweetEvent](http://niwaniwa.github.io/javadoc/whiteeggcore/com/github/niwaniwa/we/core/event/WhiteEggPreTweetEvent.html)
- [WhiteEggPostTweetEvent](http://niwaniwa.github.io/javadoc/whiteeggcore/com/github/niwaniwa/we/core/event/WhiteEggPostTweetEvent.html)
- [WhiteEggToggleCommandEvent](http://niwaniwa.github.io/javadoc/whiteeggcore/com/github/niwaniwa/we/core/event/WhiteEggToggleCommandEvent.html)

詳細は[Javadoc](http://niwaniwa.github.io/javadoc/whiteeggcore/)をご覧ください

### 言語ファイルについて

`plugins/WhiteEggCore/lang/`
階層内に設置されている言語ファイルを編集することができます

- カラーコードは`&<code>`とすることで装飾が出来ます

## 謝辞

### アドバイス、修正
- [Nekoneko](https://www.nekonekoserver.net/)様

御礼を申し上げます


## リンク

Twitter : @[haniwa_koke](https://twitter.com/haniwa_koke "haniwa_koke")

不備がありましたらこちらへ連絡をしていただけると幸いです

javajava勉強中...
