# WhiteEggCore - v1.0.4

このプラグインは[WhiteBird](https://github.com/niwaniwa/WhiteBirdPvP"WhiteBirdPvP")系統の後継版として開発されたBukkit用Pluginです。

java勉強中...

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
`/tweet <呟き>` | Twitterに呟きを送信します | whiteegg.core.command.twitter | 初実行時は/registerを行ってください
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


## Javadoc

 Javadocは[こちら](http://niwaniwa.github.io/WhiteEggCore/doc/)

## Library

* [Twitter4j](http://twitter4j.org/ "Twitter4j")

## licenseなどなど

* Twitter4jはApache License 2.0licenseを使用しています

>Copyright 2007 Yusuke Yamamoto

>Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

>      http://www.apache.org/licenses/LICENSE-2.0

>Unless required by applicable law or agreed to in writing, software
Distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

>>Twitter4J は JSON レスポンスの解析のため JSON.org のソフトウェアを含んでいます。JSON.org のソフトウェアのライセンスについてはThe JSON Licenseをご覧ください。

## その他

### プレイヤーについて
#### データ
- プレイヤーのデータは`plugins/WhiteEggCore/players/<uuid>.json`に出力されます
- プレイヤーデータは[WhitePlayer](http://niwaniwa.github.io/WhiteEggCore/doc/com/github/niwaniwa/we/core/player/WhitePlayer.html)から取得することができます
- 取得は[WhitePlayerFactory](http://niwaniwa.github.io/WhiteEggCore/doc/com/github/niwaniwa/we/core/player/WhitePlayerFactory.html)クラスを使用してください

### APIについて

このプラグインはAPIを利用することが可能です

詳しくは[Javadoc](http://niwaniwa.github.io/WhiteEggCore/doc/com/github/niwaniwa/we/core/api/WhiteEggAPI.html)をご覧ください

### Eventについて

現在3つのイベントを使用することができます

- [WhiteEggPreTweetEvent](http://niwaniwa.github.io/WhiteEggCore/doc/com/github/niwaniwa/we/core/event/WhiteEggPreTweetEvent.html)
- [WhiteEggPostTweetEvent](http://niwaniwa.github.io/WhiteEggCore/doc/com/github/niwaniwa/we/core/event/WhiteEggPostTweetEvent.html)
- [WhiteEggToggleCommandEvent](http://niwaniwa.github.io/WhiteEggCore/doc/com/github/niwaniwa/we/core/event/WhiteEggToggleCommandEvent.html)

詳細は[Javadoc](http://niwaniwa.github.io/WhiteEggCore/doc/)をご覧ください

### 言語ファイルについて

`plugins/WhiteEggCore/lang/`
階層内に設置されている言語ファイルを編集することができます

- カラーコードは`&<code>`とすることで装飾が出来ます


## 謝辞

### アドバイス、修正
- Nekoneko様

御礼を申し上げます


## リンク

Twitter : @[haniwa_koke](https://twitter.com/haniwa_koke "haniwa_koke")

不備がありましたらこちらへ連絡をしていただけると幸いです

javajava勉強中...
