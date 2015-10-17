# WhiteEggCore - v1.0.3

このプラグインは[WhiteBird](https://github.com/niwaniwa/WhiteBirdPvP "WhiteBirdPvP")系統の後継版として開発されたBukkit用Pluginです。

## 動作

* Spigot最新版にて動作を確認しています

## 主な機能
* Twitterへのツイート、情報を取得
* Tab、Click可能なテキストを表示できる機能など

## コマンド
コマンド | 説明 | 権限 | 備考
-------|------|------|-----
`/WhiteEggCore` | プラグイン情報など | whiteegg.core.command.* |  
`/toggle` | 各種設定を確認、設定できます| whiteegg.core.command.toggle |
`/head <Player名>` | プレイヤーの頭を取得します | whiteegg.core.command.toggle |
`/tweet <呟き>` | Twitterに呟きを送信します | whiteegg.core.command.twitter | 初実行時は/registerを行ってください
`/register` | Twitterに接続するための登録を行います | whiteegg.core.command.twitter.register |
`whisper` <Player> <メッセージ> | 特定の相手にプライベートメッセージを送信します | whiteegg.core.command.whisper |
`replay` <メッセージ> | 返信します | whiteegg.core.command.replay |

## Config.yml

要素名 | 説明 | type | 説明
------|------|------|-----
lock | プラグインのロック | boolean | false
consumerkey | Twitter app | String |
consumerSecret | Twitter app | String |

## その他

### APIについて
WhiteEggAPIを使用することで他のプラグインからアクセスすることができます

### Eventについて

現在3つのイベントを使用することができます

1. WhiteEggPreTweetEvent
2. WhiteEggPostTweetEvent
3. WhiteEggToggleCommandEvent

詳細はJavadocをご覧ください

## Library

* [Twitter4j](http://twitter4j.org/)

* [JSON-lib](http://json-lib.sourceforge.net/) (gsonへ移行予定)

* [Apache HttpComponents](https://hc.apache.org/)

## リンク

Twitter : @[haniwa_koke](https://twitter.com/haniwa_koke "haniwa_koke")

進捗どうですか
>進捗どうですか
>>進捗どうですか
>>>進捗どうですか
>>>>進捗どうですか
