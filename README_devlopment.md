README_development
===================

## 環境

サーバ；
- Java 11
- Spigot 1.16.5
  
  リモートに paper-spigot が動いている前提。
  spigotは当面は素の状態。 jar を DL して起動、eula=trueしたのみ。

開発；
- Java 11
- IntelliJ IDEA
- macOS catalina
- nginx (APIの模擬用)


### Java11 の導入

- homebrew で brew install する。以下例；

```
$ brew tap homebrew/cask-versions

$ brew install --cask adoptopenjdk8

$ brew install java11

$ /usr/libexec/java_home -V

$ sudo ln -sfn /usr/local/opt/openjdk@11/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk

$ brew install jenv

$ jenv enable-plugin export

$ vi ~/.profile
  :
export JAVA_HOME=`/usr/libexec/java_home -v "1.8"`
export PATH="$HOME/.jenv/bin:$PATH"
eval "$(jenv init -)"
  :

  # logout then login

$ jenv add `/usr/libexec/java_home -v "1.8"`

$ jenv add `/usr/libexec/java_home -v "11"`

$ jenv global 1.8

$ jenv versions
  system
* 1.8 (set by /Users/sato1043/.jenv/version)
  1.8.0.292
  11
  11.0
  11.0.10
  openjdk64-1.8.0.292
  openjdk64-11.0.10

$ java -version
openjdk version "1.8.0_292"
OpenJDK Runtime Environment (AdoptOpenJDK)(build 1.8.0_292-b10)
OpenJDK 64-Bit Server VM (AdoptOpenJDK)(build 25.292-b10, mixed mode)

$ echo $JAVA_HOME
/Users/sato1043/.jenv/versions/1.8

$ cd ~/github.com/sato1043/try_mc-plugin/

$ cat .java-version 
11

$ jenv versions
  system
  1.8
  1.8.0.292
* 11 (set by /Users/sato1043/github.com_sato1043/test_plugin/.java-version)
  11.0
  11.0.10
  openjdk64-1.8.0.292
  openjdk64-11.0.10

$ java -version
openjdk version "11.0.10" 2021-01-19
OpenJDK Runtime Environment (build 11.0.10+9)
OpenJDK 64-Bit Server VM (build 11.0.10+9, mixed mode)

$ echo $JAVA_HOME
/Users/sato1043/.jenv/versions/11

```


### Intellijプラグイン；

Minecraft Development
- https://plugins.jetbrains.com/plugin/8327-minecraft-development


### Intellij: Remote JVM Debug

Run configuration を edit して、 Remote JVM Debug を追加する。
- Name: Debug Spigot
- Debugger mode: Listen to remote JVM (外のJVMが繋ぎにくる)
- Auto restart にチェック。IDEのデバッグサーバはデバッグセッションが途切れるたびに auto restart する。
- Host: は自機
- Port: は任意 (デフォルト 5005)。ファイアウォールを開けておく必要がある。 
- リモート側
    - コマンドラインargs をコピペして、リモート側の java の起動オプションに加える。
    - 再起動スクリプトを作っておく
    - mvn deploy 時に jar を scp して spigot を再起動する


### プロファイルごと設定

pom.xml に定義したプロファイルは product / staging / testing の３種。必要に応じて適宜追加。

プロジェクト直下に cp .example-profile.properties .testing.properties し、.testing.properties を各自の設定に変更する。

IDE でデバッグセッションを開始。その後 mvn deploy 。サーバが起動した際にデバッグセッションに繋いでくる。

mvn deploy でサーバにプラグインがデプロイされると、サーバの再起動コマンドがSSHで発行される。自分はサーバが再起動するようにしている。あるいはデバッグセッションが有効かつサーバ起動処理に手を入れていなかったなら IDEのビルドコマンドでjavaクラスがリロードされる。

適当な箇所にブレークを貼って、実行・停止・再実行できることを確認する。

***

spigot.mc へのプラグイン更新を模擬するために、環境ごとの properties に updateChecker.url を設定する。このURLはバージョン番号文字列を単純に返答すれば良い。具体的には「1.0.0」とだけ書かれたテキストファイルを nginx などHTTPサーバで配信するようにしておく。



## 資料

Spigot Plugin Development
- Using the Spigot-API
- https://www.spigotmc.org/wiki/spigot-plugin-development/

Plugin Snippets
- User-contributed code examples for simple plugins
- https://www.spigotmc.org/wiki/plugin-snippets/


## commit

### commit log

- https://github.com/angular/angular/blob/master/CONTRIBUTING.md#commit
- http://falsandtru.hatenablog.com/entry/git-commit-message

```
              type   scope(opt) verb title 
                 \      |        |     |
                 feat(android): add template url parameter to events   (<- without period)
(empty line) ->
        body ->  追加の情報あれば   (<- within 72 chars per line)
                 本文ではどのようにではなく何をとなぜを説明する
```

<type>:
- feat (new feature for the user, not a new feature for build script)
- fix (bug fix for the user, not a fix to a build script)
- docs (changes to the documentation)
- style (formatting, missing semi colons, etc; no production code change)
- refactor (refactoring production code, eg. renaming a variable)
- test (adding missing tests, refactoring tests; no production code change)
- chore (updating grunt tasks etc; no production code change)

<verb>:
- add A (to B)
- remove A (from B)
- move A (from B to C)
- replace A with B
- make A B
- change A to B
- update A to B
- ensure that A
- use A (instead of B for C)
- fix A


__END__
