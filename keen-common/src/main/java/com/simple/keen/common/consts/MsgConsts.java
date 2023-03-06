package com.simple.keen.common.consts;

public interface MsgConsts {

    /*********************************系统异常属性***************************/
    int PARAMS_ERROR_CODE = 400;

    String PARAMS_ERROR_MSG = "params error";

    int SYSTEM_ERROR_CODE = 500;

    String SYSTEM_ERROR_MSG = "system error";

    String HAS_CHILDREN_ERROR_MSG = "当前选项下包含子选项，不可删除";

    String UPLOAD_IMG_ERROR_MSG = "图片上传失败";

    String UPLOAD_TYPE_ERROR_MSG = "上传失败，上传的格式错误";

    String LOGIN_ERROR_MSG = "账号或密码错误";

    String USER_NOT_LOGIN_MSG = "用户未登录";

    String USER_LOCK_MSG = "当前账号已被封禁，无法登录";

    String USER_NO_API_PERMISSION = "当前账号无访问此api权限";
}
