package com.fiona.tiaozao.account.sinaWeibo;

import android.content.Context;
import android.util.SparseArray;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * Created by Administrator on 2016-03-07.
 */
public class UsersAPI extends AbsOpenAPI{

    private static final int READ_USER           = 0;
    private static final int READ_USER_BY_DOMAIN = 1;
    private static final int READ_USER_COUNT     = 2;

    private static final String API_BASE_URL = API_SERVER + "/users";

    private static final SparseArray<String> sAPIList = new SparseArray<String>();
    static {
        sAPIList.put(READ_USER,           API_BASE_URL + "/show.json");
        sAPIList.put(READ_USER_BY_DOMAIN, API_BASE_URL + "/domain_show.json");
        sAPIList.put(READ_USER_COUNT,     API_BASE_URL + "/counts.json");
    }

    public UsersAPI(Context context, String appid, Oauth2AccessToken accessToken) {
        super(context, appid, accessToken);
    }

    /**
     *  根据用户ID获取用户信息。
     * @param uid           需要查询的用户ID
     * @param listener      异步请求回调接口
     */
    public void show(Long uid, WeiboLoginActivity.SinaRequestListener listener) {

        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("uid", uid);
        requestAsync(sAPIList.get(READ_USER), params, HTTPMETHOD_GET, listener);
    }
    /**
     * 通过个性化域名获取用户资料以及用户最新的一条微博。
     *
     * @param domain   需要查询的个性化域名（请注意：是http://weibo.com/xxx后面的xxx部分）
     * @param listener 异步请求回调接口
     */
    public void domainShow(String domain, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("domain", domain);
        requestAsync(sAPIList.get(READ_USER_BY_DOMAIN), params, HTTPMETHOD_GET, listener);
    }
    /**
     * 批量获取用户的粉丝数、关注数、微博数。
     *
     * @param uids     需要获取数据的用户UID，多个之间用逗号分隔，最多不超过100个
     * @param listener 异步请求回调接口
     */
    public void counts(long[] uids, RequestListener listener) {
        WeiboParameters params = buildCountsParams(uids);
        requestAsync(sAPIList.get(READ_USER_COUNT), params, HTTPMETHOD_GET, listener);
    }

    /**
     * -----------------------------------------------------------------------
     * 请注意：以下方法匀均同步方法。如果开发者有自己的异步请求机制，请使用该函数。
     * -----------------------------------------------------------------------
     */

    /**
     * @see #show(long, RequestListener)
     */
    public String showSync(long uid) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("uid", uid);
        return requestSync(sAPIList.get(READ_USER), params, HTTPMETHOD_GET);
    }

    /**
     * @see #show(String, RequestListener)
     */
    public String showSync(String screen_name) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("screen_name", screen_name);
        return requestSync(sAPIList.get(READ_USER), params, HTTPMETHOD_GET);
    }

    /**
     * @see #domainShow(String, RequestListener)
     */
    public String domainShowSync(String domain) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("domain", domain);
        return requestSync(sAPIList.get(READ_USER_BY_DOMAIN), params, HTTPMETHOD_GET);
    }

    /**
     * @see #counts(long[], RequestListener)
     */
    public String countsSync(long[] uids) {
        WeiboParameters params = buildCountsParams(uids);
        return requestSync(sAPIList.get(READ_USER_COUNT), params, HTTPMETHOD_GET);
    }

    private WeiboParameters buildCountsParams(long[] uids) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        StringBuilder strb = new StringBuilder();
        for (long cid : uids) {
            strb.append(cid).append(",");
        }
        strb.deleteCharAt(strb.length() - 1);
        params.put("uids", strb.toString());
        return params;
    }
}
