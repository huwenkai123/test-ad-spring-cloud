package com.imooc.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlaneGetRequest {

    private Long userId;
    private List<Long> ids;

    public boolean vaildate() {
        return userId != null && !CollectionUtils.isEmpty(ids);
    }
}
