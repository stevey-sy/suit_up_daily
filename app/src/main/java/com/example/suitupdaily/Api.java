package com.example.suitupdaily;

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

public interface Api {

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

    @FormUrlEncoded
    @POST("codi/codi_read.php")
    Call<List<ResponsePOJO>> getCodi(
            @Field("id") String id
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
            @Field("season") String season
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
