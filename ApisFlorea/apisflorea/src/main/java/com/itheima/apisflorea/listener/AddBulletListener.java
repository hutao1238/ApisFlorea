package com.itheima.apisflorea.listener;

import com.itheima.apisflorea.entity.Bullet;
import com.itheima.apisflorea.entity.Hero;

public class AddBulletListener extends AddEntityListener<Bullet>{

	public AddBulletListener(Bullet t) {
		super(t);
	}

	@Override
	protected void onLayoutFinish() {
		Bullet bullet = t;
		
		//初始化子弹的位置
		Hero hero = bullet.getHero();
		bullet.setTranslationX(hero.getTranslationX()+hero.getWidth()/2-25);
		bullet.setTranslationY(hero.getTranslationY()-bullet.getHeight());
//		Log.e("tag", "x : "+bullet.getTranslationX() +"  y:"+bullet.getTranslationY());
		//子弹开始射击
		float targetY = -bullet.getHeight();
		float distance = targetY - bullet.getTranslationY();
		long duration = (long) (Math.abs(distance)*1);
		bullet.shot(targetY,duration);
	}

}
