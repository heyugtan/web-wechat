package com.github.emailtohl.web.wechat.util;

import static com.github.emailtohl.web.wechat.config.Constant.GET_WEBSITE_AUTHORIZED_ACCESS_TOKEN_URL;
import static com.github.emailtohl.web.wechat.config.Constant.REFRESH_WEBSITE_AUTHORIZED_ACCESS_TOKEN_URL;
import static com.github.emailtohl.web.wechat.config.Constant.SNS_USERINFO_URL;
import static com.github.emailtohl.web.wechat.config.Constant.USERINFO_URL;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.emailtohl.web.wechat.domain.auth.SNSUserInfo;
import com.github.emailtohl.web.wechat.domain.auth.WeixinOauth2Token;
import com.github.emailtohl.web.wechat.domain.auth.WeixinUserInfo;
import com.github.emailtohl.web.wechat.service.TokenService;
import com.google.gson.Gson;
/**
 * 获取用户信息的工具
 * @author HeLei
 */
@Component
public class OauthUtil {
	@Inject
	HttpsClient httpsClient;
	@Inject
	private Gson gson;
	@Inject
	private TokenService accessTokenService;
	@Value("${appID}")
	private String appID;
	@Value("${appsecret}")
	private String appsecret;
	
	/**
	 * 获取网页授权凭证
	 * 
	 * @param appId 公众账号的唯一标识
	 * @param appSecret 公众账号的密钥
	 * @param code
	 * @return WeixinAouth2Token
	 */
	public WeixinOauth2Token getOauth2AccessToken(String code) {
		// 拼接请求地址
		String requestUrl = GET_WEBSITE_AUTHORIZED_ACCESS_TOKEN_URL.replace("APPID", appID)
				.replace("SECRET", appsecret)
				.replace("CODE", code);
		// 获取网页授权凭证
		String json = httpsClient.get(requestUrl);
		return gson.fromJson(json, WeixinOauth2Token.class);
	}

	/**
	 * 刷新网页授权凭证
	 * 
	 * @param appId 公众账号的唯一标识
	 * @param refreshToken
	 * @return WeixinAouth2Token
	 */
	public WeixinOauth2Token refreshOauth2AccessToken(String refreshToken) {
		// 拼接请求地址
		String requestUrl = REFRESH_WEBSITE_AUTHORIZED_ACCESS_TOKEN_URL.replace("APPID", appID)
				.replace("REFRESH_TOKEN", refreshToken);
		// 刷新网页授权凭证
		String json = httpsClient.get(requestUrl);
		return gson.fromJson(json, WeixinOauth2Token.class);
	}

	/**
	 * 通过网页授权获取用户信息
	 * 
	 * @param accessToken 网页授权接口调用凭证
	 * @param openId 用户标识
	 * @return SNSUserInfo
	 */
	public SNSUserInfo getSNSUserInfo(String accessToken, String openId) {
		// 拼接请求地址
		String requestUrl = SNS_USERINFO_URL.replace("ACCESS_TOKEN", accessToken)
				.replace("OPENID", openId);
		// 通过网页授权获取用户信息
		String json = httpsClient.get(requestUrl);
		return gson.fromJson(json, SNSUserInfo.class);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param accessToken 接口访问凭证
	 * @param openId 用户标识
	 * @return WeixinUserInfo
	 */
	public WeixinUserInfo getUserInfo(String openId) {
		// 拼接请求地址
		String requestUrl = USERINFO_URL.replace("ACCESS_TOKEN", accessTokenService.getAccessToken())
				.replace("OPENID", openId);
		// 获取用户信息
		String json = httpsClient.get(requestUrl);
		return gson.fromJson(json, WeixinUserInfo.class);
	}

}
