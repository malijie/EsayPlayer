package com.easy.player.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VpSimpleFragment extends Fragment {
	private String mTitle;//???????????????title
	private static final String BUNDLE_TITLE = "title";//????bundle??key

	/**
	 * fragment??????newInstance????new?????
	 *
	 * @param title
	 * @return
	 */
	public static VpSimpleFragment newInstance(String title) {
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_TITLE, title);

		VpSimpleFragment fragment = new VpSimpleFragment();
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if(bundle != null){
			mTitle = bundle.getString(BUNDLE_TITLE);
		}
		TextView textView = new TextView(getActivity());
		textView.setText(mTitle);
		textView.setGravity(Gravity.CENTER);
		
		return textView;
	}
}
