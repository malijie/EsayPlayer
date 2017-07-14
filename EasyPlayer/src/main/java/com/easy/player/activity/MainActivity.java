package com.easy.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.easy.player.R;
import com.easy.player.config.Profile;
import com.easy.player.entity.POMedia;
import com.easy.player.fragment.LocalVideoFragment;
import com.easy.player.fragment.NetworkVideoFragment;
import com.easy.player.fragment.VpSimpleFragment;
import com.easy.player.service.MediaScanService;
import com.easy.player.widget.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.easy.player.service.MediaScanService.SCAN_STATUS_END;
import static com.easy.player.service.MediaScanService.SCAN_STATUS_NORMAL;
import static com.easy.player.service.MediaScanService.SCAN_STATUS_RUNNING;
import static com.easy.player.service.MediaScanService.SCAN_STATUS_START;

/**
 * 
 * @author Administrator
 * 
 * 
 *         FragmentActivity is a special activity provided in the Support
 *         Library to handle fragments on system versions older than API level
 *         11. If the lowest system version you support is API level 11 or
 *         higher, then you can use a regular Activity.
 */
public class MainActivity extends FragmentActivity {

	private ViewPager mViewpager;
	private ViewPagerIndicator mViewPagerIndicator;
	private List<String> mTitles = Arrays.asList("本地视频", "网络视频");
	private List<Fragment> mContents = new ArrayList<Fragment>();// 装载ViewPager数据的List
	/**
	 * FragmentPagerAdapter，见名知意，这个适配器就是用来实现Fragment在ViewPager里面进行滑动切换的，因此，
	 * 如果我们想实现Fragment的左右滑动，可以选择ViewPager和FragmentPagerAdapter实现。
	 * FragmentPagerAdapter拥有自己的缓存策略
	 * ，当和ViewPager配合使用的时候，会缓存当前Fragment以及左边一个、右边一个，一共三个Fragment对象。
	 * 假如有三个Fragment
	 * ,那么在ViewPager初始化之后，3个fragment都会加载完成，中间的Fragment在整个生命周期里面只会加载一次
	 * ，当最左边的Fragment处于显示状态
	 * ，最右边的Fragment由于超出缓存范围，会被销毁，当再次滑到中间的Fragment的时候，最右边的Fragment会被再次初始化。
	 */
	private FragmentPagerAdapter mAdapter;// ViewPager适配器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initViews();
		initDatas();
		
		//动态设置tab
		mViewPagerIndicator.setVisibleTabCount(2);
		mViewPagerIndicator.setTabItemTitles(mTitles);
		
		mViewpager.setAdapter(mAdapter);
		mViewPagerIndicator.setViewPager(mViewpager, 0);
		startService(new Intent(this,MediaScanService.class).putExtra(Profile.getScanDirectoryKey(),Profile.getScanDirectory()));

	}

	/**
	 * 初始化视图
	 */
	private void initDatas() {
		mViewpager = (ViewPager) findViewById(R.id.viewpager);
		mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
	}

	/**
	 * 初始化数据
	 */
	private void initViews() {
		// 根据title初始化fragment
		for (String title : mTitles) {
			VpSimpleFragment fragment = VpSimpleFragment.newInstance(title);
//			mContents.add(fragment);
		}

		LocalVideoFragment localVideoFragment = new LocalVideoFragment();
		NetworkVideoFragment networkVideoFragment = new NetworkVideoFragment();
		mContents.add(localVideoFragment);
		mContents.add(networkVideoFragment);

		// getFragmentManager();
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mContents.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mContents.get(position);
			}
		};
	}

}
