# README

$\rm\Large{\color{#C6FFDD}{SUSTech}}^{\large{\color{#D5FFD4}{2024}}}\_{~~~~\color{#FBD786}{大一}}\color{#FBD08D}{秋}\large{\color{#FBC994}{~java}}^{\color{#FBC29B}{期}_{\color{#FBAC9E}{末}}}\_{\scriptsize{\color{#FBA5A5}{project}}}\tiny{\color{#f7797d}{}}$

## 玩法
### 经典模式
- 选关界面点击屏幕让猫走过去 站在关卡上按enter进入
- wasd/方向键/屏幕上的方向键操控移动。
- 鼠标移动到左上角数字部分可以展开玩~~危机合约~~困难模式
- 关卡中途可以按下ctrl+S保存（如果登陆了）

### 无尽模式
- bug还挺多，不过倒也是能玩，更像是个半成品（打多了可能会卡）。按esc退出

### 双人模式
- 没什么好说的）
- 聊天模式好，聊天再好不过了

### 主界面
- 主题
  - 可以改颜色
- 用户
  - 一些基本的存档功能。
- 设置
  - 动画速度
  - 音量
  - A*：
    - 选上后会每一步都跑一遍a*看是否有解。只要无解自动判负。不过会占用一些资源
    - 不选的话只有基础的判断，例如箱子到墙角了。
  - 种子：
    - 选关部分的地图是使用生命游戏的方法随机生成的，随机种子可以在这里手动设定。

## 一些说明
- Releases
  - 解压后运行bin文件夹里的app
  - release里的所有存档功能都失效了会报错）

- ai解方面参考了[[https://github.com/KnightofLuna/sokoban-solver]]
