package com.yzm.sleep.refresh;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;

public class MaterialRefreshLayout extends FrameLayout {

	public static final String Tag = "cjj_log";

	private MaterialHeadView materialHeadView;

	private MaterialFoodView materialFoodView;

	private boolean isOverlay;

	private int waveType;

	private int DEFAULT_WAVE_HEIGHT = 140;

	private int HIGHER_WAVE_HEIGHT = 180;

	private int DEFAULT_HEAD_HEIGHT = 70;

	private int hIGHER_HEAD_HEIGHT = 100;

	private int DEFAULT_PROGRESS_SIZE = 50;

	private int BIG_PROGRESS_SIZE = 60;

	private int PROGRESS_STOKE_WIDTH = 3;

	private int waveColor;

	protected float mWaveHeight;

	protected float mHeadHeight;

	private View mChildView;

	protected FrameLayout mHeadLayout;

	protected boolean isRefreshing;

	private float mTouchY;

	private float mCurrentY;

	private DecelerateInterpolator decelerateInterpolator;

	private float headHeight;

	private float waveHeight;

	private int[] colorSchemeColors;

	private int colorsId;

	private int progressTextColor;

	private int progressValue, progressMax;

	private boolean showArrow;

	private int textType;

	private MaterialRefreshListener refreshListener;

	private boolean showProgressBg;

	private int progressBg;

	private boolean isShowWave;

	private int progressSizeType;

	private int progressSize = 0;

	private boolean isLoadMoreing;

	private boolean isLoadMore;

	public MaterialRefreshLayout(Context context) {
		this(context, null, 0);
	}

	public MaterialRefreshLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MaterialRefreshLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	private void init(Context context, AttributeSet attrs, int defstyleAttr) {
		if (isInEditMode()) {
			return;
		}

		if (getChildCount() > 1) {
			throw new RuntimeException("can only have one child widget");
		}

		decelerateInterpolator = new DecelerateInterpolator(10);

		TypedArray t = context.obtainStyledAttributes(attrs,
				R.styleable.MaterialRefreshLayout, defstyleAttr, 0);
		isOverlay = t.getBoolean(R.styleable.MaterialRefreshLayout_overlay,
				false);
		/** attrs for materialWaveView */
		waveType = t.getInt(R.styleable.MaterialRefreshLayout_wave_height_type,
				0);
		if (waveType == 0) {
			headHeight = DEFAULT_HEAD_HEIGHT;
			waveHeight = DEFAULT_WAVE_HEIGHT;
			MaterialWaveView.DefaulHeadHeight = DEFAULT_HEAD_HEIGHT;
			MaterialWaveView.DefaulWaveHeight = DEFAULT_WAVE_HEIGHT;
		} else {
			headHeight = hIGHER_HEAD_HEIGHT;
			waveHeight = HIGHER_WAVE_HEIGHT;
			MaterialWaveView.DefaulHeadHeight = hIGHER_HEAD_HEIGHT;
			MaterialWaveView.DefaulWaveHeight = HIGHER_WAVE_HEIGHT;
		}
		waveColor = t.getColor(R.styleable.MaterialRefreshLayout_wave_color,
				Color.WHITE);
		isShowWave = t.getBoolean(R.styleable.MaterialRefreshLayout_wave_show,
				true);

		/** attrs for circleprogressbar */
		// colorsId =
		// t.getResourceId(R.styleable.MaterialRefreshLayout_progress_colors,
		// R.array.material_colors);
		// colorSchemeColors = context.getResources().getIntArray(colorsId);
		showArrow = t.getBoolean(
				R.styleable.MaterialRefreshLayout_progress_show_arrow, true);
		textType = t.getInt(
				R.styleable.MaterialRefreshLayout_progress_text_visibility, 1);
		progressTextColor = t.getColor(
				R.styleable.MaterialRefreshLayout_progress_text_color,
				Color.BLACK);
		progressValue = t.getInteger(
				R.styleable.MaterialRefreshLayout_progress_value, 0);
		progressMax = t.getInteger(
				R.styleable.MaterialRefreshLayout_progress_max_value, 100);
		showProgressBg = t
				.getBoolean(
						R.styleable.MaterialRefreshLayout_progress_show_circle_backgroud,
						true);
		progressBg = t.getColor(
				R.styleable.MaterialRefreshLayout_progress_backgroud_color,
				CircleProgressBar.DEFAULT_CIRCLE_BG_LIGHT);
		progressSizeType = t.getInt(
				R.styleable.MaterialRefreshLayout_progress_size_type, 0);
		if (progressSizeType == 0) {
			progressSize = DEFAULT_PROGRESS_SIZE;
		} else {
			progressSize = BIG_PROGRESS_SIZE;
		}
		isLoadMore = t.getBoolean(R.styleable.MaterialRefreshLayout_isLoadMore,
				false);
		t.recycle();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		Context context = getContext();

		FrameLayout headViewLayout = new FrameLayout(context);
		LayoutParams layoutParams = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 0);
		layoutParams.gravity = Gravity.TOP;
		headViewLayout.setLayoutParams(layoutParams);

		mHeadLayout = headViewLayout;

		this.addView(mHeadLayout);

		mChildView = getChildAt(0);

		if (mChildView == null) {
			return;
		}

		setWaveHeight(Util.dip2px(context, waveHeight));
		setHeaderHeight(Util.dip2px(context, headHeight));

		// 进度颜色替换
		colorSchemeColors = new int[] { 0xff6f9ef6, 0xff4571ff, 0xff48afea };
		// 进度的背景色
		progressBg = 0xffe0e0f6;
		// 大的背景是否显示和颜色
		isShowWave = true;
		waveColor = getResources().getColor(R.color.cbg_color_7f);

		materialHeadView = new MaterialHeadView(context);
		materialHeadView.setWaveColor(isShowWave ? waveColor : Color.WHITE);
		materialHeadView.showProgressArrow(showArrow);
		materialHeadView.setProgressSize(progressSize);
		materialHeadView.setProgressColors(colorSchemeColors);
		materialHeadView.setProgressStokeWidth(PROGRESS_STOKE_WIDTH);
		materialHeadView.setTextType(textType);
		materialHeadView.setProgressTextColor(progressTextColor);
		materialHeadView.setProgressValue(progressValue);
		materialHeadView.setProgressValueMax(progressMax);
		materialHeadView.setIsProgressBg(showProgressBg);
		materialHeadView.setProgressBg(progressBg);
		setHeaderView(materialHeadView);

		materialFoodView = new MaterialFoodView(context);
		LayoutParams layoutParams2 = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(context,
						hIGHER_HEAD_HEIGHT));
		layoutParams2.gravity = Gravity.BOTTOM;
		materialFoodView.setLayoutParams(layoutParams2);
		materialFoodView.showProgressArrow(showArrow);
		materialFoodView.setProgressSize(progressSize);
		materialFoodView.setProgressColors(colorSchemeColors);
		materialFoodView.setProgressStokeWidth(PROGRESS_STOKE_WIDTH);
		materialFoodView.setTextType(textType);
		materialFoodView.setProgressValue(progressValue);
		materialFoodView.setProgressValueMax(progressMax);
		materialFoodView.setIsProgressBg(showProgressBg);
		materialFoodView.setProgressBg(progressBg);
		materialFoodView.setVisibility(View.GONE);
		setFooderView(materialFoodView);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isRefreshing)
			return true;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTouchY = ev.getY();
			mCurrentY = mTouchY;
			break;
		case MotionEvent.ACTION_MOVE:
			float currentY = ev.getY();
			float dy = currentY - mTouchY;
			if (dy > 0 && !canChildScrollUp()) {
				if (materialHeadView != null) {
					materialHeadView.onBegin(this);
				}
				return true;
			} else if (dy < 0 && !canChildScrollDown() && isLoadMore) {
				if (materialFoodView != null && !isLoadMoreing) {
					soveLoadMoreLogic();
				}
				return super.onInterceptTouchEvent(ev);
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	private void soveLoadMoreLogic() {
		isLoadMoreing = true;
		materialFoodView.setVisibility(View.VISIBLE);
		materialFoodView.onBegin(this);
		materialFoodView.onRefreshing(this);
		if (refreshListener != null) {
			refreshListener.onRefreshLoadMore(MaterialRefreshLayout.this);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (isRefreshing) {
			return super.onTouchEvent(e);
		}

		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			mCurrentY = e.getY();
			float dy = mCurrentY - mTouchY;
			dy = Math.min(mWaveHeight * 2, dy);
			dy = Math.max(0, dy);
			if (mChildView != null) {
				float offsetY = decelerateInterpolator.getInterpolation(dy
						/ mWaveHeight / 2)
						* dy / 2;
				float fraction = offsetY / mHeadHeight;
				mHeadLayout.getLayoutParams().height = (int) offsetY;
				mHeadLayout.requestLayout();

				if (materialHeadView != null) {
					materialHeadView.onPull(this, fraction);

				}
				if (!isOverlay)
					ViewHelper.setTranslationY(mChildView, offsetY);

			}
			return true;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (mChildView != null) {
				if (isOverlay) {
					if (mHeadLayout.getLayoutParams().height > mHeadHeight) {

						updateListener();

						mHeadLayout.getLayoutParams().height = (int) mHeadHeight;
						mHeadLayout.requestLayout();

					} else {
						mHeadLayout.getLayoutParams().height = 0;
						mHeadLayout.requestLayout();
					}

				} else {
					if (ViewHelper.getTranslationY(mChildView) >= mHeadHeight) {
						createAnimatorTranslationY(mChildView, mHeadHeight,
								mHeadLayout);
						updateListener();
					} else {
						createAnimatorTranslationY(mChildView, 0, mHeadLayout);
					}
				}

			}
			return true;
		}
		return super.onTouchEvent(e);
	}

	public void autoRefresh() {
		this.post(new Runnable() {

			@Override
			public void run() {
				if (!isRefreshing) {
					updateListener();
					if (isOverlay) {
						mHeadLayout.getLayoutParams().height = (int) mHeadHeight;
						mHeadLayout.requestLayout();
					} else {
						createAnimatorTranslationY(mChildView, mHeadHeight,
								mHeadLayout);
					}
				}

			}
		});
	}

	public void autoRefreshLoadMore() {
		if (isLoadMore) {
			soveLoadMoreLogic();
		} else {
			throw new RuntimeException("you must  setLoadMore ture");
		}
	}

	public void updateListener() {
		isRefreshing = true;

		if (materialHeadView != null) {
			materialHeadView.onRefreshing(MaterialRefreshLayout.this);
		}

		if (refreshListener != null) {
			refreshListener.onRefresh(MaterialRefreshLayout.this);
		}

	}

	public void setLoadMore(boolean isLoadMore) {
		this.isLoadMore = isLoadMore;
	}

	public void setProgressColors(int[] colors) {
		this.colorSchemeColors = colors;
	}

	public void setShowArrow(boolean showArrow) {
		this.showArrow = showArrow;
	}

	public void setShowProgressBg(boolean showProgressBg) {
		this.showProgressBg = showProgressBg;
	}

	public void setWaveColor(int waveColor) {
		this.waveColor = waveColor;
	}

	public void setWaveShow(boolean isShowWave) {
		this.isShowWave = isShowWave;
	}

	public void setIsOverLay(boolean isOverLay) {
		this.isOverlay = isOverLay;
	}

	public void setProgressValue(int progressValue) {
		this.progressValue = progressValue;
		materialHeadView.setProgressValue(progressValue);
	}

	@SuppressLint("NewApi")
	public void createAnimatorTranslationY(final View v, final float h,
			final FrameLayout fl) {
		ObjectAnimator viewPropertyAnimatorCompat = ObjectAnimator.ofFloat(v,
				"translationY", h);
		viewPropertyAnimatorCompat.setDuration(200);
		viewPropertyAnimatorCompat
				.setInterpolator(new DecelerateInterpolator());
		viewPropertyAnimatorCompat.start();
		viewPropertyAnimatorCompat
				.addUpdateListener(new AnimatorUpdateListener() {

					@Override
					public void onAnimationUpdate(ValueAnimator arg0) {
						// TODO Auto-generated method stub
						float height = ViewHelper.getTranslationY(v);
						fl.getLayoutParams().height = (int) height;
						fl.requestLayout();
					}
				});
	}

	/**
	 * @return Whether it is possible for the child view of this layout to
	 *         scroll up. Override this if the child view is a custom view.
	 */
	public boolean canChildScrollUp() {
		if (mChildView == null) {
			return false;
		}
		if (Build.VERSION.SDK_INT < 14) {
			if (mChildView instanceof AbsListView) {
				final AbsListView absListView = (AbsListView) mChildView;
				return absListView.getChildCount() > 0
						&& (absListView.getFirstVisiblePosition() > 0 || absListView
								.getChildAt(0).getTop() < absListView
								.getPaddingTop());
			} else {
				return ViewCompat.canScrollVertically(mChildView, -1)
						|| mChildView.getScrollY() > 0;
			}
		} else {
			return ViewCompat.canScrollVertically(mChildView, -1);
		}
	}

	public boolean canChildScrollDown() {
		if (mChildView == null) {
			return false;
		}
		if (android.os.Build.VERSION.SDK_INT < 14) {
			if (mChildView instanceof AbsListView) {
				final AbsListView absListView = (AbsListView) mChildView;
				if (absListView.getChildCount() > 0) {
					int lastChildBottom = absListView.getChildAt(
							absListView.getChildCount() - 1).getBottom();
					return absListView.getLastVisiblePosition() == absListView
							.getAdapter().getCount() - 1
							&& lastChildBottom <= absListView
									.getMeasuredHeight();
				} else {
					return false;
				}

			} else {
				return ViewCompat.canScrollVertically(mChildView, 1)
						|| mChildView.getScrollY() > 0;
			}
		} else {
			return ViewCompat.canScrollVertically(mChildView, 1);
		}
	}

	public void setWaveHigher() {
		headHeight = hIGHER_HEAD_HEIGHT;
		waveHeight = HIGHER_WAVE_HEIGHT;
		MaterialWaveView.DefaulHeadHeight = hIGHER_HEAD_HEIGHT;
		MaterialWaveView.DefaulWaveHeight = HIGHER_WAVE_HEIGHT;
	}

	@SuppressLint("NewApi")
	public void finishRefreshing() {
		if (mChildView != null) {
			PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("y",
					ViewHelper.getTranslationY(mChildView));
			PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(
					"translationY", 0f);

			ObjectAnimator viewPropertyAnimatorCompat = ObjectAnimator
					.ofPropertyValuesHolder(mChildView, pvhX, pvhY);
			viewPropertyAnimatorCompat.setDuration(200);
			viewPropertyAnimatorCompat
					.setInterpolator(new DecelerateInterpolator());
			viewPropertyAnimatorCompat.start();

			if (materialHeadView != null) {
				materialHeadView.onComlete(MaterialRefreshLayout.this);
			}

			if (refreshListener != null) {
				refreshListener.onfinish();
			}
		}
		isRefreshing = false;
		progressValue = 0;
		setProgressValue(0);
	}

	public void finishRefresh() {
		this.post(new Runnable() {
			@Override
			public void run() {
				finishRefreshing();
			}
		});
	}

	public void finishRefreshLoadMore() {
		this.post(new Runnable() {
			@Override
			public void run() {
				if (materialFoodView != null && isLoadMoreing) {
					isLoadMoreing = false;
					materialFoodView.onComlete(MaterialRefreshLayout.this);
				}
			}
		});

	}

	public void setHeaderView(final View headerView) {
		post(new Runnable() {
			@Override
			public void run() {
				mHeadLayout.addView(headerView);
			}
		});
	}

	public void setFooderView(final View fooderView) {
		this.addView(fooderView);
	}

	public void setWaveHeight(float waveHeight) {
		this.mWaveHeight = waveHeight;
	}

	public void setHeaderHeight(float headHeight) {
		this.mHeadHeight = headHeight;
	}

	public void setMaterialRefreshListener(
			MaterialRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
	}

	private View noMore, loading, noNet, noData;
	private OnClickRequestListener listener;

	public void setOnClickRequestListener(OnClickRequestListener listener) {
		this.listener = listener;
	}

	/**
	 * 
	 */
	public void setText(int type, String text) {
		switch (type) {
			case Constant.NO_DATA:
				if(noData != null){
					TextView textV=(TextView) noData.findViewById(R.id.text);
					textV.setText(text);
				}
				break;
			default:
				break;
		}
	}

	/**
	 * 
	 * @param listview
	 * @param type
	 * @param listener
	 *            可以为空。当没有网络的时候,点击事件的监听
	 */
	public void addListViewState(ListView listview, int type) {
		if (noMore == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			noMore = inflater.inflate(R.layout.listview_footer_nomore, null);
		}
		if (loading == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			loading = inflater.inflate(R.layout.listview_loading_footer, null);
		}
		if (noNet == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			noNet = inflater.inflate(R.layout.layout_no_net, null);
			RelativeLayout rlP = (RelativeLayout) noNet.findViewById(R.id.rl_p);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlP.getLayoutParams();
			params.height = Constant.screenHeight - Util.dip2px(getContext(), 98);
			rlP.setLayoutParams(params);
		}
		if (noData == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			noData = inflater.inflate(R.layout.no_auto_layout, null);
			((TextView)noData.findViewById(R.id.text)).setText("暂无数据");
			RelativeLayout rlP = (RelativeLayout) noNet.findViewById(R.id.rl_p);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlP.getLayoutParams();
			params.height = Constant.screenHeight - Util.dip2px(getContext(), 98);
			rlP.setLayoutParams(params);
		}
		try {
			listview.removeFooterView(noMore);
			listview.removeFooterView(loading);
			listview.removeFooterView(noNet);
			listview.removeFooterView(noData);
			switch (type) {
			case Constant.LOADING:
				listview.addFooterView(loading, null, false);
				break;
			case Constant.NO_MORE:
				listview.addFooterView(noMore, null, false);
				break;
			case Constant.NO_NET:
				listview.addFooterView(noNet, null, false);
				noNet.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (listener != null)
							listener.setRequest();
					}
				});
				break;
			case Constant.NO_DATA:
				listview.addFooterView(noData, null, false);
				break;
			default:
				break;
			}
		} catch (Exception e) {
		}
	}
}
