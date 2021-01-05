package com.example.suitupdaily;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {
    // 베스트 코디 불러오는 메소드
    @FormUrlEncoded
    @POST("codi/best_codi.php")
    Call<ResponsePOJO> getBestCodi(
            @Field("id") String id
    );
    // 댓글 수정하는 메소드
    @FormUrlEncoded
    @POST("codi/edit_comment.php")
    Call<ResponsePOJO> editComment(
            @Field("id") String id,
            @Field("idx") String idx,
            @Field("content") String content
    );

    // 댓글 삭제하는 메소드
    @FormUrlEncoded
    @POST("codi/delete_comment.php")
    Call<ResponsePOJO> deleteComment(
            @Field("id") String id,
            @Field("comment_idx") String comment_idx,
            @Field("codi_idx") String codi_idx
    );
    // 댓글 업로드 하는 메소드
    @FormUrlEncoded
    @POST("codi/upload_comment.php")
    Call<ResponsePOJO> uploadComment(
            @Field("id") String id,
            @Field("idx") String idx,
            @Field("content") String content
    );

    @FormUrlEncoded
    @POST("codi/upload_view.php")
    Call<ResponsePOJO> uploadView(
            @Field("id") String id,
            @Field("idx") String idx
    );

    @FormUrlEncoded
    @POST("user/edit_profile.php")
    Call<ResponsePOJO> editProfile(
            @Field("id") String id,
            @Field("nick") String nick,
            @Field("birth") String birth,
            @Field("sex") String sex,
            @Field("EN_IMAGE") String picture
    );

    @FormUrlEncoded
    @POST("user/get_profile.php")
    Call<ResponsePOJO> getProfile(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("user/upload_basic_info.php")
    Call<ResponsePOJO> uploadBasicInfo(
            @Field("id") String id,
            @Field("nick") String nick,
            @Field("birth") String birth,
            @Field("sex") String sex
    );

    @FormUrlEncoded
    @POST("user/upload_user_list.php")
    Call<ResponsePOJO> uploadUserList(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("closet/read_liked_list.php")
    Call<ResponsePOJO> readLikedList(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("closet/upload_like2.php")
    Call<ResponsePOJO> uploadLike(
            @Field("id") String id,
            @Field("idx") String idx
    );

    @FormUrlEncoded
    @POST("closet/codi_upload.php")
    Call<ResponsePOJO> uploadCodi(
            @Field("id") String id,
            @Field("EN_IMAGE") String encodedImage,
            @Field("season") String season,
            @Field("hash_tag") String tags_no_hash,
            @Field("memo") String memo
    );

    @FormUrlEncoded
    @POST("closet/codi_delete.php")
    Call<ResponsePOJO> deleteCodi(
            @Field("key") String key,
            @Field("picture") String picture
    );

    @FormUrlEncoded
    @POST("closet/codi_modify.php")
    Call<ResponsePOJO> modifyCodi(
            @Field("key") String key,
            @Field("id") String id,
            @Field("season") String season,
            @Field("hash_tag") String hash_tag,
            @Field("memo") String memo
    );
    // 댓글 리스트 가져오는 메소드
    @FormUrlEncoded
    @POST("codi/get_comment.php")
    Call<List<ResponsePOJO>> getComment(
            @Field("codi_idx") String idx
    );

    @FormUrlEncoded
    @POST("codi/codi_read.php")
    Call<List<ResponsePOJO>> getCodi(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("codi/get_search.php")
    Call<List<ResponsePOJO>> searchCodi(
            @Field("id") String id,
            @Field("search_word") String search_word
    );

    // 코디 공유 페이지에서 서버로 게시글 filter 데이터 요청
    @FormUrlEncoded
    @POST("codi/set_filter.php")
    Call<List<ResponsePOJO>> setFilter(
            @Field("update") String recently,
            @Field("sex") String sex,
            @Field("age") String age
    );

    @FormUrlEncoded
    @POST("closet/codi_board.php")
    Call<List<ResponsePOJO>> getCodiBoard(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("closet/read.php")
    Call<List<ResponsePOJO>> getCloth(
            @Field("id") String id,
            @Field("type") String type,
            @Field("season") String season,
            @Field("color") String color
    );

    @FormUrlEncoded
    @POST("closet/closet_upload.php")
    Call<ResponsePOJO> uploadImage (
            @Field("id") String id,
            @Field("category") String category,
            @Field("EN_IMAGE") String encodedImage,
            @Field("date") String date,
            @Field("color") String color,
            @Field("season") String season,
            @Field("type") String type,
            @Field("fit") String fit,
            @Field("color_integer") int color_integer
    );

    // 삭제
    @FormUrlEncoded
    @POST("closet/delete.php")
    Call<ResponsePOJO> delete(
            @Field("key") String key,
            @Field("picture") String picture
    );

    //수정
    @FormUrlEncoded
    @POST("closet/modify.php")
    Call<ResponsePOJO> modify(
            @Field("key") String key,
            @Field("userID") String user_id,
            @Field("picture") String picture,
            @Field("main_category") String category,
            @Field("color") String color,
            @Field("date") String date,
            @Field("season") String season,
            @Field("type") String type,
            @Field("fit") String fit
    );
}
