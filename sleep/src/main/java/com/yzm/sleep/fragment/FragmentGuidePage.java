package com.yzm.sleep.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.R;

/**
 * Created by Administrator on 2016/4/8.
 */
public class FragmentGuidePage extends Fragment {

    private Activity activity;
    private String imgurl;
    private Button btnEnter;
    private OnClickEnter listener;

    public interface OnClickEnter{
        void onClick();
    }

    public void setListener(OnClickEnter listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        imgurl = bundle.getString("imgurl");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guidepage, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        ImageLoader.getInstance().displayImage(imgurl, imageView);

        btnEnter = (Button) view.findViewById(R.id.btn_enter);
        if(listener!= null){
            btnEnter.setVisibility(View.VISIBLE);
        }else {
            btnEnter.setVisibility(View.GONE);
        }
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick();
                }
            }
        });
    }
}
