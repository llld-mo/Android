42311097 张广松
# 打地鼠游戏 

这是一款简单的打地鼠小游戏，使用 Java 开发。玩家需要在限定时间内尽可能多地击中出现的地鼠来获得高分。

## 游戏截图
![屏幕截图 2025-06-08 145912](https://github.com/user-attachments/assets/b55529a7-e62b-430e-85d0-dff37edd144a)

![image](https://github.com/user-attachments/assets/1a03be36-9742-4777-874b-74426883734b)


## 功能特性

*   **经典玩法**: 在九宫格（3x3）中随机出现地鼠。
*   **计时挑战**: 玩家需要在限定的游戏时间内尽可能多地击中地鼠。
    *   当前默认游戏时长：20秒 (可配置)
*   **分数系统**: 每次成功击中地鼠会增加分数。
*   **游戏控制**:
    *   **开始游戏**: 启动新的一局游戏。
    *   **暂停游戏**: 临时停止游戏进程，包括计时器和地鼠的活动。
    *   **继续游戏**: 从暂停状态恢复游戏。
    *   **重新开始**: 在游戏结束后或游戏中可以重新开始一局新游戏。
*   **视觉反馈**:
    *   地鼠出现、被击中、消失都有对应的图片变化。
    *   游戏结束和暂停状态有明确的文本提示。
*   **分数记录)**:
    *   记录玩家每次游戏的得分，并提供历史分数查看页面。

## 技术栈

*   **语言**: Java
*   **平台**: Android
*   **UI**: Android XML 布局
*   **核心逻辑**:
    *   `CountDownTimer` 用于游戏倒计时。
    *   `Handler` 和 `postDelayed` 用于控制地鼠的出现和消失逻辑。
    *   `ImageView` 数组表示地鼠洞。
    *   通过 `OnClickListener` 处理玩家的点击事件。
*    `Room Persistence Library` 用于本地存储分数数据。

## 如何运行

1.  **环境要求**:
    *   Android Studio 
    *   Android SDK
2.  **克隆或下载项目**:
3.  **导入项目**:
    *   打开 Android Studio。
    *   选择 "Open an Existing Project"。
    *   导航到项目根目录并选择它。
4.  **构建并运行**:
    *   等待 Gradle 同步完成。
    *   选择一个模拟器或连接一个真实的 Android 设备。
    *   点击 Android Studio 工具栏上的 "Run 'app'" 按钮 。

## 代码结构 (简要)

*   `MainActivity.java`: 游戏的主要逻辑和界面控制。
*   `activity_main.xml`: 游戏主界面的布局文件。
*   `drawable/`: 存放游戏所需的图片资源 (地鼠、洞穴、背景等)。
* 
    *   `db/`: 包含 Room数据库相关的类 (Entity, DAO, Database)。
    *   `ScoreHistoryActivity.java`:  显示分数历史的 Activity。
    *   `activity_score_history.xml`:  分数历史页面的布局。
    *   `ScoreAdapter.java`:  用于在 RecyclerView 中显示分数的适配器。

## 未来可能的改进

*   [ ] 增加不同类型的地鼠 (例如，加分地鼠、减分地鼠、炸弹等)。
*   [ ] 增加游戏难度等级。
*   [ ] 更丰富的动画效果和音效。
*   [ ] 在线排行榜。
*   [ ] 优化UI设计。
*   [ ] 将分数记录功能完善并集成。

欢迎提出建议或参与改进！

