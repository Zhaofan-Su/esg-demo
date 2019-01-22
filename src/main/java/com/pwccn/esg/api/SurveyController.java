package com.pwccn.esg.api;

import com.pwccn.esg.dto.CompanyDTO;
import com.pwccn.esg.model.CompanyEntity;
import com.pwccn.esg.model.DomainEntity;
import com.pwccn.esg.model.TopicEntity;
import com.pwccn.esg.repository.CompanyRepository;
import com.pwccn.esg.repository.DomainRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/survey")
@PreAuthorize("hasRole('ROLE_ADMIN1')")
public class SurveyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DomainRepository domainRepository;

    @ApiOperation(value ="Get all the companies/stakeholders by industry.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get all the companies successfully."),
            @ApiResponse(code = 204, message = "No company exits."),
            @ApiResponse(code = 404, message = "Not such an industry.")
    })
    @GetMapping(value = "/Stakeholders/{industry}")
    public ResponseEntity<List<String>> getAllStakeholders(@PathVariable String industry){
        if(!indudtries().contains(industry)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<CompanyEntity> entities = companyRepository.findByIndustry(industry);
        List<String> result = new ArrayList<>();
        if(entities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        for(CompanyEntity entity : entities) {
            result.add(entity.getName());
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @ApiOperation(value = "Get all topics of a domain(环境/社会/治理).")
    @ApiResponses({
            @ApiResponse(code = 200, message = "All topics have been found successful."),
            @ApiResponse(code = 204, message = "No topics for this domain."),
            @ApiResponse(code = 404, message = "No such a domain.")
    })
    @GetMapping(value = "/topics/{domain}")
    public ResponseEntity<List<String>> getTopics(@PathVariable String domain){
        DomainEntity domainEntity = domainRepository.findByName(domain);
        if(domainEntity!= null){
            List<String> result = new ArrayList<>();
            List<TopicEntity> topics = domainEntity.getTopics();
            if(topics.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            for(TopicEntity topicEntity : topics) {
                result.add(topicEntity.getName());
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @ApiOperation(value = "Get all industries.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Find all industries successfully."),
            @ApiResponse(code = 204, message = "No industry exits.")
    })
    @GetMapping("/indutries")
    public ResponseEntity<List<String>> getIndustries() {
        List<String> result = indudtries();
        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    private List<String> indudtries(){
        List<String> result = new ArrayList<>();
        List<CompanyEntity> companyEntities = companyRepository.findAll();
        for(CompanyEntity companyEntity : companyEntities) {
            if(!result.contains(companyEntity.getIndustry())) {
                result.add(companyEntity.getIndustry());
            }
        }

        return result;
    }
}
