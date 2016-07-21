package com.itheima.apisflorea.listener;

import java.util.Random;

import com.itheima.apisflorea.ApisFlorea;
import com.itheima.apisflorea.entity.Enemy;

public class AddEnemyListener extends AddEntityListener<Enemy> {

	public AddEnemyListener(Enemy t) {
		super(t);
	}

	@Override
	protected void onLayoutFinish() {
		Enemy enemy = t;
		 
		// 开始飞行
		ApisFlorea war = (ApisFlorea) enemy.getParent();
		if (war != null) {
			// 隐藏敌人
			Random random = new Random();
			enemy.setTranslationY(-1 * enemy.getHeight());
			float targetX = random.nextInt((war.getWidth() - enemy.getWidth()));
			enemy.setTranslationX(targetX);
			// Log.e("tag", "targetX:"+targetX);

			enemy.setDuration(4000 + new Random().nextInt(2000));
			// 飞行目标
			enemy.setTargetY(war.getHeight() + enemy.getHeight());

			enemy.fly();
		}
	}

}