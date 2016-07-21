package com.itheima.apisflorea.entity;

import com.itheima.apisflorea.R;

import android.content.Context;

public class Hero extends BaseEntity{

	public Hero(Context context) {
		super(context);
	}

	@Override
	protected int getImageRes() {
		return R.drawable.hero1;
	}

}
