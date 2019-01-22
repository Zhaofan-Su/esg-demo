package com.pwccn.esg.dto;

import com.pwccn.esg.model.DomainEntity;
import com.pwccn.esg.model.TopicEntity;

import java.util.List;

public class DomainDTO {

    private String name;
    private List<TopicEntity> topics;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTopics(List<TopicEntity> topics) {
        this.topics = topics;
    }

    public List<TopicEntity> getTopics() {
        return topics;
    }

    public DomainDTO(){

    }
    public DomainDTO(DomainEntity domainEntity){
        setName(domainEntity.getName());
        setTopics(domainEntity.getTopics());
    }
}
