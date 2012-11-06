package com.mjhaytercawatkins.pokermate;

import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/** Class to be attached to views which will have their values updated by circular scrolling
 */
class SvListener implements OnTouchListener {
	private SvStrategy mStrategy;
	private float mScale;

	public SvListener(SvStrategy strategy, float density_scale) {
		mStrategy = strategy;
		mScale = density_scale;
	}
	
	private final static int GRAD_DEG = 90;
	private final static float POINT_DIST = 60.0f;
	private float[] x = new float[3];
	private float[] y = new float[3];
	private int cycler = 0;
	private boolean haveFullSet = false;
	private int mCurrGrads;
	private float degs = 0.0f;
	
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
			cycler = 0;
			x[cycler] = event.getX();
			y[cycler] = event.getY();
			haveFullSet = false;
			mCurrGrads = 0;
			degs = 0.0f;
			event.getX();
			event.getY();
			mStrategy.onStart(v);
		}
		if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
			handleMove(v, event);
		}
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {
			cycler = 0;
			haveFullSet = false;
			mStrategy.onFinish(v);
		}
		return true;
	}

	private void handleMove(View v, MotionEvent event) {

		// The distance of this touch from the last recorded point
		float lastPointDist = mag(event.getX()-x[cycler], event.getY()-y[cycler])/mScale;
		if (lastPointDist < POINT_DIST){
			return;
		}
		cycler = (cycler+1)%3;
		x[cycler] = event.getX();
		y[cycler] = event.getY();
		if (!haveFullSet) {
			if (cycler != 0) {
				return;
			}
			haveFullSet = true;
		}
		// a is the most recent pos, b is the second most-recent, c is the third
		final int c = (cycler+1)%3;
		final int b = (cycler+2)%3;
		final int a = cycler;
		// Unit vectors
		float xBA = (x[a] - x[b])/mag(x[a] - x[b], y[a] - y[b]); 
		float yBA = (y[a] - y[b])/mag(x[a] - x[b], y[a] - y[b]); 
		float xCB = (x[b] - x[c])/mag(x[b] - x[c], y[b] - y[c]); 
		float yCB = (y[b] - y[c])/mag(x[b] - x[c], y[b] - y[c]); 

		float zVec = xBA*yCB - yBA*xCB;
		float theta = (float)Math.toDegrees(Math.asin(zVec));
		degs += theta;
		int grads = ((int)degs)/GRAD_DEG;
		if (grads != mCurrGrads) {
			if (grads > mCurrGrads) {
				mStrategy.onCcwGrad(v); 
			} else {
				mStrategy.onCwGrad(v);
			}
			mCurrGrads = grads;
		}
	}

	private float mag(float ax, float ay) {
		return FloatMath.sqrt((float)(Math.pow(ax, 2) + Math.pow(ay, 2)));
	}

}