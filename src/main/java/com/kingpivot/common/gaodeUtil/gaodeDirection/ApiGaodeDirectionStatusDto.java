package com.kingpivot.common.gaodeUtil.gaodeDirection;

public class ApiGaodeDirectionStatusDto {
    private String status;
    private String info;
    private ApiGaodeDirectionRouteDto route;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ApiGaodeDirectionRouteDto getRoute() {
        return route;
    }

    public void setRoute(ApiGaodeDirectionRouteDto route) {
        this.route = route;
    }
}
