package com.pwccn.esg.api;

import com.pwccn.esg.dto.DataAnalysisDTO;
import com.pwccn.esg.model.CompanyEntity;
import com.pwccn.esg.model.IndicatorDataEntity;
import com.pwccn.esg.model.IndicatorEntity;
import com.pwccn.esg.repository.IndicatorDataRepository;
import com.pwccn.esg.repository.IndicatorRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/data")
public class DataController {

    @Autowired
    private IndicatorRepository indicatorRepository;

    @ApiOperation(value = "Analysis the datas of the indicators")
    @ApiResponse(code = 200, message = "OK")
    //@PreAuthorize("hasRole('ROLE_ADMIN1')")
    @GetMapping("/{indicatorName}")
    public ResponseEntity<List<DataAnalysisDTO>> doAnalysis(@PathVariable String indicatorName) {
        DecimalFormat format = new DecimalFormat("0.00");
        List<IndicatorEntity> indicatorEntities = indicatorRepository.findByName(indicatorName);
        List<DataAnalysisDTO> results = new ArrayList<>();
        for(IndicatorEntity indicatorEntity : indicatorEntities) {
            DataAnalysisDTO dataAnalysisDTO = new DataAnalysisDTO();

            dataAnalysisDTO.setCompanyName(indicatorEntity.getCompany().getName());
            dataAnalysisDTO.setUnit(indicatorEntity.getIndicatorData().getUnit());

            String data = indicatorEntity.getIndicatorData().getSections();
            double dataSum = 0.00;
            String[] stringDatas = data.split(",");
            for(int i = 0; i < stringDatas.length; i++) {
                dataSum = dataSum + Double.parseDouble(stringDatas[i]);
            }
            dataAnalysisDTO.setData(new BigDecimal(dataSum).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());

            results.add(dataAnalysisDTO);
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
