package com.local.vincent.service;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.local.vincent.properties.LineplatformProperties;

@Service
public class GetLineplatformService {

    private final LineplatformProperties lineplatformProperties;

    @Autowired
    public GetLineplatformService(LineplatformProperties lineplatformProperties){
        this.lineplatformProperties = lineplatformProperties;
    }

    /**
     * プロフィール情報を取得する
     */
    public String getUserinf(String userId, String AccessToken){
        String userName = "";

        LineMessagingClient client = LineMessagingClient
                .builder(AccessToken)
                .build();

        try {
            // apiでユーザーネームを取得する
            final UserProfileResponse userProfileResponse = client.getProfile(userId).get();

            // 値が取得できなければnullを返す
            if(userProfileResponse!=null){
                userName = userProfileResponse.getDisplayName();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return userName;
        }
        return userName;
    }
}
