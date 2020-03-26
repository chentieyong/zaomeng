package com.kingpivot.common.gaodeUtil.gaodeDirection;

import java.util.List;

public class ApiGaodeDirectionPathsDto {
    private String distance;
    private List<ApiGaodeDirectionStepsDto> steps;


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<ApiGaodeDirectionStepsDto> getSteps() {
        return steps;
    }

    public void setSteps(List<ApiGaodeDirectionStepsDto> steps) {
        this.steps = steps;
    }
}
