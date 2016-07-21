package com.itheima.apisflorea.entity;

import com.itheima.apisflorea.ApisFlorea;
import com.itheima.apisflorea.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.content.Context;
import android.view.animation.LinearInterpolator;

public class Bullet extends BaseEntity{
	
	public Bullet(Context context) {
		super(context);
	}
	private boolean isCanShot = true;
	private Hero hero;

	public void setCanShot(boolean isCanShot){
		this.isCanShot = isCanShot;
	}
	
	public boolean isCanShot(){
		return isCanShot;
	}
	
	@Override
	protected int getImageRes() {
		return R.drawable.bullet;
	}
	
	private ViewPropertyAnimator animator;

	public void shot(float targetY,long duration) {
		if(animator==null){
			animator = ViewPropertyAnimator.animate(this).translationY(targetY)
					.setDuration(duration)
					.setInterpolator(new LinearInterpolator())
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							bitmap.recycle();
							ApisFlorea war = (ApisFlorea) getParent();
							if(war!=null){
								war.removeView(Bullet.this);
							}
						}
						@Override
						public void onAnimationCancel(Animator animation) {
							bitmap.recycle();
							ApisFlorea war = (ApisFlorea) getParent();
							if(war!=null){
								war.removeView(Bullet.this);
							}
						}
					});
		}
		animator.start();
		
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}
	
	@Override
	public void stopAnimation() {
		if(animator!=null){
			animator.cancel();
		}
	}
}
