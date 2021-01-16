package com.example.suitupdaily.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.suitupdaily.styling.CodiArticle;
import com.example.suitupdaily.R;
import com.example.suitupdaily.api.ResponsePOJO;
import com.example.suitupdaily.api.RetrofitClient;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class BestLikeFragment extends Fragment {
    // Store instance variables
    private String title, codi_idx;
    private String user_id, nick, like_num, comment_num, photo_url, codi_img_url, view_num, hash_tags, date, memo, who_liked;
    private int page;
    private CircleImageView iv_user_profile;
    private CardView card_view;
    private ImageView img_codi;
    private TextView tv_user_nick, tv_like_num, tv_comment_num, tv_hash_tags;

    // newInstance constructor for creating fragment with arguments
    public static BestLikeFragment newInstance(int page, String title) {
        BestLikeFragment fragment = new BestLikeFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_best_like, container, false);
        // xml 연결
        img_codi = (ImageView) view.findViewById(R.id.iv_article_codi);
        iv_user_profile = (CircleImageView) view.findViewById(R.id.civ_article_profile);
        tv_user_nick = (TextView) view.findViewById(R.id.tv_article_writer);
        tv_like_num = (TextView) view.findViewById(R.id.tv_article_views);
        tv_comment_num = (TextView) view.findViewById(R.id.tv_article_comments);
        tv_hash_tags = (TextView) view.findViewById(R.id.tv_article_hash);
        card_view = (CardView) view.findViewById(R.id.card_view_codi);

        // 서버에서 코디 데이터 받아오는 메소드
        getBestCodi();
        // 카드뷰 클릭 이벤트
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent: 화면 전환 to Codi Board
                Intent intent = new Intent (getActivity(), CodiArticle.class);
                intent.putExtra("userID", "sinsy8989@gmail.com");
                intent.putExtra("idx", codi_idx);
                intent.putExtra("nick", nick);
                intent.putExtra("like_num", like_num);
                intent.putExtra("comment_count", comment_num);
                intent.putExtra("profile_pic", photo_url);
                intent.putExtra("tags", hash_tags);
                intent.putExtra("picture", codi_img_url);
                intent.putExtra("view", view_num);
                intent.putExtra("date", date);
                intent.putExtra("memo", memo);
                intent.putExtra("who_liked", who_liked);
                startActivity(intent);
            }
        });
        return view;
    }

    private void getBestCodi() {
//        String id = user_id;
        String id = "sinsy8989@gmail.com";
        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().getBestCodi(id);
        call.enqueue(new Callback<ResponsePOJO>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<ResponsePOJO> call, retrofit2.Response<ResponsePOJO> response) {
//                Toast.makeText(ShareCodi.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
                if(response.body().isStatus()) {
                    // 서버에서 받아온 데이터
                    nick = response.body().getNick();
                    like_num = String.valueOf(response.body().getLike());
                    comment_num = String.valueOf(response.body().getCommentCount());
                    photo_url = response.body().getPhotoUrl();
                    hash_tags = response.body().getTags();
                    codi_img_url = response.body().getImgURL();
                    codi_idx = response.body().getIdx();
                    view_num = response.body().getView();
                    date = response.body().getDate();
                    user_id = "sinsy8989@gmail.com";
                    memo = response.body().getMemo();
                    who_liked = response.body().getWhoLiked();
                    // 이미지 데이터에 문제생겼을 경우 표시될 대체 이미지.
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.skipMemoryCache(true);
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                    requestOptions.placeholder(R.drawable.ic_clothes_hanger);
                    requestOptions.error(R.drawable.ic_baseline_accessibility_24);
                    // 서버에서 받아온 데이터를 view 에 뿌려줌
                    Glide.with(BestLikeFragment.this).load(photo_url).apply(requestOptions).circleCrop().into(iv_user_profile);
                    Glide.with(BestLikeFragment.this).load(codi_img_url).apply(requestOptions).into(img_codi);
                    tv_user_nick.setText(nick);
                    tv_like_num.setText(like_num);
                    tv_comment_num.setText(comment_num);
                    tv_like_num.setText(like_num);
                    tv_hash_tags.setText(hash_tags);
                } else {

                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
//                Toast.makeText(BestLikeFragment.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
