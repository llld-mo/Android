package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    // UI组件
    private TextView scoreText, timeText, gameOverText;
    private Button startButton, restartButton, buttonPause, buttonRestart;
    private ImageView[] holes = new ImageView[9]; // 3x3网格的9个洞穴

    // 游戏状态
    private int score = 0;
    private boolean gameRunning = false;
    private boolean gamePaused = false; // 添加暂停状态
    private CountDownTimer gameTimer;
    private Handler moleHandler = new Handler();
    private Random random = new Random();
    private List<Integer> activeMoles = new ArrayList<>(); // 当前活跃的地鼠位置

    // 暂停相关变量
    private long remainingTime = 0; // 剩余时间（毫秒）

    // 游戏配置
    private static final int GAME_DURATION = 20000; // 60秒
    private static final int MOLE_SHOW_DURATION = 2000; // 地鼠显示2秒
    private static final int MOLE_SPAWN_INTERVAL = 1000; // 每1秒可能生成新地鼠
    private static final int HIT_ANIMATION_DURATION = 500; // 被击中动画持续0.5秒

    // 地鼠状态枚举
    private enum MoleState {
        EMPTY, NORMAL, HIT
    }

    private MoleState[] moleStates = new MoleState[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whack_mole);

        initViews();
        initGame();
    }

    private void initViews() {
        // 初始化UI组件
        scoreText = findViewById(R.id.scoreText);
        timeText = findViewById(R.id.timeText);
        gameOverText = findViewById(R.id.gameOverText);
        startButton = findViewById(R.id.startButton);
        restartButton = findViewById(R.id.restartButton);

        // 绑定新添加的按钮
        buttonPause = findViewById(R.id.buttonPause);
        buttonRestart = findViewById(R.id.buttonRestart);

        // 初始化9个洞穴ImageView
        holes[0] = findViewById(R.id.hole0);
        holes[1] = findViewById(R.id.hole1);
        holes[2] = findViewById(R.id.hole2);
        holes[3] = findViewById(R.id.hole3);
        holes[4] = findViewById(R.id.hole4);
        holes[5] = findViewById(R.id.hole5);
        holes[6] = findViewById(R.id.hole6);
        holes[7] = findViewById(R.id.hole7);
        holes[8] = findViewById(R.id.hole8);

        // 设置点击监听
        for (int i = 0; i < 9; i++) {
            final int index = i;
            holes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onHoleClicked(index);
                }
            });
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });

        // 添加暂停按钮监听器
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameRunning && !gamePaused) {
                    pauseGame();
                } else if (gameRunning && gamePaused) {
                    resumeGame();
                }
            }
        });

        // 添加重新开始按钮监听器
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });
    }

    private void initGame() {
        // 初始化游戏状态
        score = 0;
        gameRunning = false;
        gamePaused = false;
        remainingTime = GAME_DURATION;
        activeMoles.clear();

        // 初始化所有洞穴为空
        for (int i = 0; i < 9; i++) {
            moleStates[i] = MoleState.EMPTY;
            holes[i].setImageResource(R.drawable.hole_empty);
        }

        updateUI();
        gameOverText.setVisibility(View.GONE);
        restartButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);

        // 隐藏游戏中的按钮
        buttonPause.setVisibility(View.GONE);
        buttonRestart.setVisibility(View.GONE);
    }

    private void startGame() {
        gameRunning = true;
        gamePaused = false;
        score = 0;
        remainingTime = GAME_DURATION;
        activeMoles.clear();

        startButton.setVisibility(View.GONE);
        gameOverText.setVisibility(View.GONE);
        restartButton.setVisibility(View.GONE);

        // 显示游戏中的按钮
        buttonPause.setVisibility(View.VISIBLE);
        buttonRestart.setVisibility(View.VISIBLE);
        buttonPause.setText("暂停");

        // 开始游戏倒计时
        startGameTimer();

        // 开始生成地鼠
        startMoleSpawner();
    }

    private void startGameTimer() {
        gameTimer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                updateTimeDisplay((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                endGame();
            }
        }.start();
    }

    private void pauseGame() {
        if (!gameRunning || gamePaused) return;

        gamePaused = true;
        buttonPause.setText("继续");

        // 停止计时器
        if (gameTimer != null) {
            gameTimer.cancel();
        }

        // 停止地鼠生成
        moleHandler.removeCallbacksAndMessages(null);

        Toast.makeText(this, "游戏已暂停", Toast.LENGTH_SHORT).show();
    }

    private void resumeGame() {
        if (!gameRunning || !gamePaused) return;

        gamePaused = false;
        buttonPause.setText("暂停");

        // 重新开始计时器
        startGameTimer();

        // 重新开始地鼠生成
        startMoleSpawner();

        Toast.makeText(this, "游戏继续", Toast.LENGTH_SHORT).show();
    }

    private void startMoleSpawner() {
        if (!gameRunning || gamePaused) return;

        // 随机决定是否生成新地鼠
        if (random.nextFloat() < 0.6f && activeMoles.size() < 3) {
            spawnMole();
        }

        // 继续调度下一次检查
        moleHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMoleSpawner();
            }
        }, MOLE_SPAWN_INTERVAL);
    }

    private void spawnMole() {
        if (!gameRunning || gamePaused) return;

        // 找到空的洞穴
        List<Integer> emptyHoles = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (moleStates[i] == MoleState.EMPTY) {
                emptyHoles.add(i);
            }
        }

        if (emptyHoles.isEmpty()) return;

        // 随机选择一个空洞穴
        int holeIndex = emptyHoles.get(random.nextInt(emptyHoles.size()));

        // 设置地鼠状态
        moleStates[holeIndex] = MoleState.NORMAL;
        holes[holeIndex].setImageResource(R.drawable.mole_normal);
        activeMoles.add(holeIndex);

        // 设置地鼠自动消失
        moleHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (moleStates[holeIndex] == MoleState.NORMAL) {
                    hideMole(holeIndex);
                }
            }
        }, MOLE_SHOW_DURATION);
    }

    private void hideMole(int holeIndex) {
        moleStates[holeIndex] = MoleState.EMPTY;
        holes[holeIndex].setImageResource(R.drawable.hole_empty);
        activeMoles.remove(Integer.valueOf(holeIndex));
    }

    private void onHoleClicked(int holeIndex) {
        if (!gameRunning || gamePaused) return;

        if (moleStates[holeIndex] == MoleState.NORMAL) {
            // 击中地鼠
            moleStates[holeIndex] = MoleState.HIT;
            holes[holeIndex].setImageResource(R.drawable.mole_hit);

            // 增加分数
            score += 10;
            updateScoreDisplay();

            // 显示击中动画后隐藏地鼠
            moleHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideMole(holeIndex);
                }
            }, HIT_ANIMATION_DURATION);

            // 显示击中提示
            Toast.makeText(this, "击中! +10分", Toast.LENGTH_SHORT).show();
        }
    }

    private void endGame() {
        gameRunning = false;
        gamePaused = false;
        moleHandler.removeCallbacksAndMessages(null);

        // 隐藏所有地鼠
        for (int i = 0; i < 9; i++) {
            moleStates[i] = MoleState.EMPTY;
            holes[i].setImageResource(R.drawable.hole_empty);
        }
        activeMoles.clear();

        // 显示游戏结束信息
        gameOverText.setText("游戏结束!\n最终得分: " + score);
        gameOverText.setVisibility(View.VISIBLE);
        restartButton.setVisibility(View.VISIBLE);

        // 隐藏游戏中的按钮
        buttonPause.setVisibility(View.GONE);
        buttonRestart.setVisibility(View.GONE);

        Toast.makeText(this, "游戏结束! 得分: " + score, Toast.LENGTH_LONG).show();
    }

    private void restartGame() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        moleHandler.removeCallbacksAndMessages(null);
        initGame();
    }

    private void updateUI() {
        updateScoreDisplay();
        updateTimeDisplay((int) (remainingTime / 1000));
    }

    private void updateScoreDisplay() {
        scoreText.setText("得分: " + score);
    }

    private void updateTimeDisplay(int seconds) {
        timeText.setText("时间: " + seconds + "s");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        moleHandler.removeCallbacksAndMessages(null);
    }
}