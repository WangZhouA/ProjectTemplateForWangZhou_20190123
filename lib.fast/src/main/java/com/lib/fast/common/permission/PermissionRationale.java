package com.lib.fast.common.permission;

/**
 * 项目：智能控制     SmartLock
 */
public class PermissionRationale {

    private String[] permission;//权限
    private String rationale;//为什么需要权限的介绍

    public PermissionRationale(String rationale, String... permission) {
        this.permission = permission;
        this.rationale = rationale;
    }

    public String[] getPermission() {
        return permission;
    }

    public void setPermission(String... permission) {
        this.permission = permission;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }
}
