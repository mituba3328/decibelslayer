# Decibel Slayer
Decibel Slayerは、Minecraftのゲーム内で周囲の音量をリアルタイムで測定し、音量に基づいたアクションを提供するFabric Mod

## 機能
リアルタイム音量測定
Java標準ライブラリのjavax.sound.sampledを使用して周囲の音量を取得

静かにしていないとクリーパーをスポーン

音量バーの表示
音量を画面上に視覚化し、音量に応じた色で表示します。

### 音量アクション
- 最大音量の30%よりもうるさくなると，クリーパーがスポーン

追加したかった
- かくれんぼで静かにしていないとエフェクトが出る
- 大きな声を出せば出すほど攻撃力が上がる or 能力が付く

## 必要環境
Minecraft: バージョン 1.21
Fabric Loader: 最新バージョン 推奨
Mod Menu: Modの設定を行う

# 使用方法
Mod Menu からModの設定を開く
![image](https://github.com/user-attachments/assets/dbda3f67-b00b-4b24-b1ba-338d427db3bb)
マイクの設定からマイクを選択する
![Screenshot from 2024-11-30 22-18-47](https://github.com/user-attachments/assets/d8ad34ff-2400-4ee2-8324-f178e1cbdfd1)
感度調整から，最もうるさい状態と静かな状態をそれぞれ計測する
![image](https://github.com/user-attachments/assets/4943ece3-c870-44c4-9cd8-381709ee5317)
