package com.itheima.apisflorea.entity;

import com.itheima.apisflorea.ApisFlorea;
import com.itheima.apisflorea.R;
import com.itheima.apisflorea.animator.FlyAnimator;
import com.itheima.apisflorea.animator.FlyAnimator.OnEnemyFlyListener;
import com.itheima.apisflorea.dialog.GameDialog.OnRestartClickListener;
import com.itheima.apisflorea.util.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.RectF;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class Enemy extends BaseEntity implements OnEnemyFlyListener {
	protected ApisFlorea apisFlorea;
	protected final int MAX_SHOT_COUNT = 8;

	public ApisFlorea getApisFlorea() {
		return apisFlorea;
	}

	public void setApisFlorea(ApisFlorea apisFlorea) {
		this.apisFlorea = apisFlorea;
	}

	public Enemy(Context context) {
		super(context);
	}

	private long duration;// 飞行时间
	private int targetY;
	private int shotCount;// 被子弹命中的数量

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getTargetY() {
		return targetY;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}

	FlyAnimator flyAnimator;

	/**
	 * 飞行
	 */
	public void fly() {
		flyAnimator = new FlyAnimator(this, getTranslationY(), targetY);
		flyAnimator.setOnEnemyFlyListener(this);
		flyAnimator.setAnimationListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				ApisFlorea war = (ApisFlorea) getParent();
				if (war != null) {
					war.removeView(Enemy.this);
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				ApisFlorea war = (ApisFlorea) getParent();
				if (war != null) {
					war.removeView(Enemy.this);
				}
			}
		});
		flyAnimator.setInterpolator(new LinearInterpolator());
		flyAnimator.start(duration);
	}

	@Override
	protected int getImageRes() {
		return R.drawable.enemy1;
	}

	@Override
	public void onFly(float translationY) {
		// 处理敌人与英雄的碰撞
		Hero hero = apisFlorea.getHero();
		// Log.e("tag", Utils.getTranslationRect(this).toString());
		// 如果敌人与英雄发送碰撞
		if (hero != null && hero.getVisibility() == View.VISIBLE && getParent()!=null) {
			if (isShareRect(this, hero) || isInRect(this, hero)) {
				// 引爆敌人
				apisFlorea.boomEnemy(getTranslationX(), getTranslationY());
				// 取消敌人动画
				flyAnimator.cancel();

				// 引爆英雄和移除子弹
				apisFlorea.boomHero();
				apisFlorea.removeBullet();
				// 隐藏英雄
				apisFlorea.hideHero();
				//保存分数
				apisFlorea.saveScore();

				showDialog();
			}

			// 处理敌人与子弹的碰撞
			for (int i = 0; i < apisFlorea.getChildCount(); i++) {
				View view = apisFlorea.getChildAt(i);
				if (view instanceof Bullet) {
					Bullet bullet = (Bullet) view;

					// 如果子弹可以命中敌人
					if (bullet.isCanShot()) {
						// 如果子弹命中了敌人
						if (isShareRect(bullet, this) || isInRect(bullet, this)) {
							setShotCount(getShotCount() + 1);
							// 同时让子弹隐藏，并且不可再命中其他敌人
							bullet.setCanShot(false);
							bullet.setVisibility(View.INVISIBLE);
							bullet.stopAnimation();
							
						}

						// 如果子弹命中此时超过10，则引爆敌人
						if (getShotCount() >= MAX_SHOT_COUNT) {
							apisFlorea.boomEnemy(getTranslationX(),
									getTranslationY());
							// 取消动画
							flyAnimator.cancel();
							
							//增加当前分数
							apisFlorea.increaseScore();
							break;
						}
					}
				}
			}
		}

	}

	public boolean isInRect(View v1, View v2) {
		RectF rect1 = Utils.getTranslationRect(v1);
		RectF rect2 = Utils.getTranslationRect(v2);
		return rect1.left >= rect2.left && rect1.top >= rect2.top
				&& rect1.right <= rect2.right && rect1.bottom <= rect2.bottom;
	}

	/**
	 * 是否有交集
	 * 
	 * @return
	 */
	public boolean isShareRect(View v1, View v2) {
		RectF rect1 = Utils.getTranslationRect(v1);
		RectF rect2 = Utils.getTranslationRect(v2);
		boolean isLeftIn = rect1.left >= rect2.left
				&& rect1.left <= rect2.right;
		boolean isTopIn = rect1.top >= rect2.top && rect1.top <= rect2.bottom;
		boolean isRightIn = rect1.right >= rect2.left
				&& rect1.right <= rect2.right;
		boolean isBottomIn = rect1.bottom >= rect2.top
				&& rect1.bottom <= rect2.bottom;

		return (isLeftIn && isTopIn) || (isLeftIn && isBottomIn)
				|| (isRightIn && isTopIn) || (isRightIn && isBottomIn)
				|| (isTopIn && isLeftIn) || (isTopIn && isRightIn)
				|| (isBottomIn && isLeftIn) || (isBottomIn && isRightIn);
	}

	public int getShotCount() {
		return shotCount;
	}

	public void setShotCount(int shotCount) {
		this.shotCount = shotCount;
	}

	@Override
	public void stopAnimation() {
		if (flyAnimator != null) {
			flyAnimator.cancel();
		}
	}

	private void showDialog() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Utils.showGameDialog(getContext(),
						new OnRestartClickListener() {
							@Override
							public void onRestartClick() {
								apisFlorea.clearAll();

								apisFlorea.start();
							}

							@Override
							public void onFinishClick() {
								apisFlorea.end();
							}
						});

			}
		}, 1500);
	}

}
