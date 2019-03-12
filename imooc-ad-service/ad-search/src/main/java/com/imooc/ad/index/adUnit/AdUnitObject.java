package com.imooc.ad.index.adUnit;


import com.imooc.ad.index.adplan.AdPlanObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitObject {
    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;
    private AdPlanObject adPlanObject;

    void update(AdUnitObject newObject) {

        if (null != newObject.getUnitId()) {
            this.unitId = newObject.getUnitId();
        }
        if (null != newObject.getUnitStatus()) {
            this.unitStatus = newObject.getUnitStatus();
        }
        if (null != newObject.getPositionType()) {
            this.positionType = newObject.getPositionType();
        }
        if (null != planId) {
            this.planId = newObject.getPlanId();
        }
        if (null != newObject.getAdPlanObject()) {
            this.adPlanObject = newObject.getAdPlanObject();
        }
    }

    private static boolean isKanPing(int positionType) {
        return (positionType & AdUnitConstants.POSITION_YPE.KAIPING) > 0;
    }

    private static boolean isTiePian(int positionType) {
        return (positionType & AdUnitConstants.POSITION_YPE.TIEPIAN) > 0;
    }

    private static boolean isTiePianMiddel(int positionType) {
        return (positionType & AdUnitConstants.POSITION_YPE.TIEPIAN_MIDDEL) > 0;
    }

    private static boolean isTiePianPause(int positionType) {
        return (positionType & AdUnitConstants.POSITION_YPE.TIEPIAN_PAUSE) > 0;
    }

    private static boolean isTiePianPost(int positionType) {
        return (positionType & AdUnitConstants.POSITION_YPE.TIEPIAN_POST) > 0;
    }

    public static boolean isAdSlotTypeOk(int adSlopType, int positionType) {
        switch (adSlopType) {
            case AdUnitConstants.POSITION_YPE.KAIPING:
                return isKanPing(positionType);
            case AdUnitConstants.POSITION_YPE.TIEPIAN :
                return isTiePian(positionType);
            case AdUnitConstants.POSITION_YPE.TIEPIAN_MIDDEL:
                return isTiePianMiddel(positionType);
            case AdUnitConstants.POSITION_YPE.TIEPIAN_PAUSE:
                return isTiePianPause(positionType);
            case AdUnitConstants.POSITION_YPE.TIEPIAN_POST:
                return isTiePianPost(positionType);
                default:
                    return false;
        }
    }
}
