package com.mjhaytercawatkins.pokermate;

import android.view.View;

// Sv == Scroll Value - Regarding the method of linear changing of a value by circular motion
/**
 * Provides the strategy for the scrollable value - i.e. handles the onScroll events
 */
interface SvStrategy {
	public abstract void onStart(View v);
	public abstract void onCwGrad(View v);
	public abstract void onCcwGrad(View v);
	public abstract void onFinish(View v);
}
