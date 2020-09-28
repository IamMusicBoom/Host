package com.optima.plugin.host.download.module;

import com.optima.plugin.repluginlib.base.BaseModule;

/**
 * create by wma
 * on 2020/9/23 0023
 */
public class HostModule extends BaseModule {
    final String TAG = HostModule.class.getSimpleName();
    private final String VERIFY_HOST_API = "";

    /**
     * 版本号
     */
    private String code;

    /**
     * 版本名字
     */
    private String name;

    /**
     * 版本描述
     */
    private String description;

    /**
     * 下载地址
     */
    private String downloadUrl;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

//    /**
//     * 验证宿主版本
//     */
//    VerifyHostVersionCallBack listener = new VerifyHostVersionCallBack();
//    class VerifyHostVersionCallBack implements OptimaHttpUtils.OnOptimaHttpUtilsListener{
//
//        @Override
//        public void onCancelled(String methodName, String requestId) {
//            Logger.e(TAG, "onCancelled: methodName = " + methodName);
//        }
//
//        @Override
//        public void onSuccessful(String methodName, String requestId, JSONObject jsonObject) {
//            Logger.d(TAG, "onSuccessful: methodName = " + methodName);
//            HostModule hostModule = new Gson().fromJson(jsonObject.toString(), HostModule.class);
//            int currentVersionCode = AppUtil.getCurrentVersionCode(P_Context.getContext());
//            try {
//                Integer integer = Integer.valueOf(hostModule.getCode());
//                if (currentVersionCode < integer) {
//                    List<DownloadModule> downloadModules = new ArrayList<>();
//                    downloadModules.add(DownloadModule.build(hostModule));
//                    ConfigSP.putNeedDownloadList(new Gson().toJson(downloadModules));
//                    DownloadActivity.upDataHost(P_Context.getContext(), LoginConstans.ACTION_UPDATE_HOST);
//                } else {
//                    Logger.d(TAG, "onSuccessful: 不需要更新：localVersionCode = " + currentVersionCode + " netVersionCode = " + hostModule.getCode());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Logger.e(TAG, "验证宿主版本失败 e = " + e.getMessage());
//                Logger.e(TAG, "验证宿主版本失败 versionCode = " + hostModule.getCode());
//            }
//
//        }
//
//        @Override
//        public void onFailure(String methodName, String requestId, int failureCode, String message) {
//            Logger.e(TAG, "onFailure: methodName = " + methodName);
//
//            try {
//                AssetManager assets = P_Context.getContext().getResources().getAssets();
//                InputStream open = assets.open("host.txt");
//                BufferedReader reader = new BufferedReader(new InputStreamReader(open));
//                Gson gson = new Gson();
//                List<DownloadModule> icons = gson.fromJson(reader, new TypeToken<List<DownloadModule>>() {
//                }.getType());
//                for (int i = 0; i < icons.size(); i++) {
//                    DownloadModule icon = icons.get(i);
//                    if (icon.getFileName().contains("apk")) {
//                        icon.setFileType(P_FileUtil.HOST_FOLDER);
//                    } else {
//                        icon.setFileType(P_FileUtil.ICON_FOLDER);
//                    }
//                }
//                ConfigSP.putNeedDownloadList(gson.toJson(icons));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            DownloadActivity.upDataHost(P_Context.getContext(), LoginConstans.ACTION_UPDATE_HOST);
//
//
//        }
//    }
//    public void verifyHostVersion(OptimaHttpUtils.OnOptimaHttpUtilsListener listener) {
//        if(listener == null){
//            listener = this.listener;
//        }
//        OptimaHttpUtils httpUtils = new OptimaHttpUtils("", listener);
//        Map<String, String> bodyParameterMap = new HashMap<>();
//        cancelable = httpUtils.requestPost(VERIFY_HOST_API, VERIFY_HOST_API, bodyParameterMap);
//    }


}
